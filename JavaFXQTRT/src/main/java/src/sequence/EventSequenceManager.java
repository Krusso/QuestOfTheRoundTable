package src.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.BoardModel;
import src.game_logic.EventCard;
import src.game_logic.Rank;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.events.EventDiscardCardsClient;
import src.player.Player;
import src.player.PlayerManager;

public class EventSequenceManager extends SequenceManager {

	EventCard card;

	public EventSequenceManager(EventCard card2) {
		this.card = card2;
	}

	final static Logger logger = LogManager.getLogger(EventSequenceManager.class);

	@Override
	public void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
		logger.info("Event Sequence Manager starting: " + card.getName());
		// The next player to complete a Quest will receive 2 extra shields
		if(card.getName().equals("King's Recognition")) {
			bm.setSetKingRecognition(true);
			logger.info("Set Kings Recognition");
		} 
		// The lowest ranked player(s) immediately receives 2 Adventure Cards
		else if(card.getName().equals("Queen's Favor")) {
			Iterator<Player> players = pm.round();
			Rank.RANKS rank = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
			ArrayList<Player> lowest = new ArrayList<Player>();
			while(players.hasNext()) {
				Player player = players.next();
				if(player.getRank().compareTo(rank) < 0) {
					rank = player.getRank();
					lowest.clear();
					lowest.add(player);
				} else if(player.getRank().compareTo(rank) == 0) {
					lowest.add(player);
				}
			}
			logger.info("Giving cards to: " + Arrays.toString(lowest.stream().map(i -> i.getID()).toArray(Integer[]::new)));
			pm.drawCards(lowest, 2);
		} 
		// All Allies in play must be discarded
		else if(card.getName().equals("Court Called to Camelot")) {
			List<Player> list = new ArrayList<Player>();
			pm.round().forEachRemaining(i -> list.add(i));
			pm.discardAllies(list);
		} 
		// All players except the player drawing this card lose 1 shield
		else if(card.getName().equals("Pox")) {
			List<Player> list = new ArrayList<Player>();
			Iterator<Player> iter = pm.round();
			iter.next();
			iter.forEachRemaining(i -> list.add(i));
			pm.changeShields(list, -1);
		} 
		// Drawer loses 2 shields if possible
		else if(card.getName().equals("Plague")) {
			List<Player> list = new ArrayList<Player>();
			list.add(pm.round().next());
			pm.changeShields(list, -2);
		} 
		// Players with both lowest rank and least amount of shields, receives 3 shields
		else if(card.getName().equals("Chivalrous Deed")) {
			Iterator<Player> players = pm.round();
			ArrayList<Player> lowest = new ArrayList<Player>();
			lowest.add(players.next());
			while(players.hasNext()) {
				Player player = players.next();
				if(player.getRank().compareTo(lowest.get(0).getRank()) < 0 ||
						(player.getRank().compareTo(lowest.get(0).getRank()) == 0 && 
						player.getShields() < lowest.get(0).getShields())) {
					lowest.clear();
					lowest.add(player);
				} else if(player.getRank().compareTo(lowest.get(0).getRank()) == 0 &&
						player.getShields() == lowest.get(0).getShields()) {
					lowest.add(player);
				}
			}
			pm.changeShields(lowest, 3);
			logger.info("Players receiving shields: " + Arrays.toString(lowest.stream().map(i -> i.getID()).toArray(Integer[]::new)));
		} 
		// All players may immediately draw 2 adventure cards
		else if(card.getName().equals("Prosperity Throughout the Realm")) {
			ArrayList<Player> list = new ArrayList<Player>();
			pm.round().forEachRemaining(i -> list.add(i));
			pm.drawCards(list, 2);
		} 
		// The highest ranked players must place 1 weapon in the discard pile. If unable to do so, 2 Foe cards must be discarded
		else if(card.getName().equals("King's Call to Arms")) {
			Iterator<Player> players = pm.round();
			Rank.RANKS rank = Rank.RANKS.SQUIRE;
			ArrayList<Player> highest = new ArrayList<Player>();
			while(players.hasNext()) {
				Player player = players.next();
				if(player.getRank().compareTo(rank) > 0) {
					rank = player.getRank();
					highest.clear();
					highest.add(player);
				} else if(player.getRank().compareTo(rank) == 0) {
					highest.add(player);
				}
			}
			
			highest.forEach(player -> {
				if(player.getTypeCount(TYPE.WEAPONS) >= 1) {
					pm.setPlayer(player);
					pm.setState(player, Player.STATE.EVENTDISCARDING, 1, AdventureCard.TYPE.WEAPONS);
				} else if(player.getTypeCount(TYPE.FOES) >= 1) {
					pm.setPlayer(player);
					pm.setState(player, Player.STATE.EVENTDISCARDING, Math.min(2, player.getTypeCount(TYPE.FOES)), AdventureCard.TYPE.FOES);
				} else {
					return;
				}
				EventDiscardCardsClient edc = actions.take(EventDiscardCardsClient.class, MESSAGETYPES.EVENTDISCARD);
				pm.discardFromHand(player, edc.cards);
			});
		}
		pm.sendContinue("Event Over");
		logger.info("Event Sequence Manager over");
	}
}
