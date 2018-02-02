package src.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import src.game_logic.AdventureCard;
import src.game_logic.BoardModel;
import src.game_logic.EventCard;
import src.game_logic.Rank;
import src.player.Player;
import src.player.PlayerManager;

public class EventSequenceManager extends SequenceManager {

	EventCard card;

	public EventSequenceManager(EventCard card2) {
		this.card = card2;
	}


	@Override
	public void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm) {

		// Holy this is pretty dumb.... not sure of a better way though
		// The next player to complete a Quest will receive 2 extra shields
		if(card.getName().equals("King's Recognition")) {
			bm.setSetKingRecognition(true);
		} 
		// The lowest ranked player(s) immediately receives 2 Adventure Cards
		else if(card.getName().equals("Queen's Favor")) {
			Iterator<Player> players = pm.round();
			Rank.RANKS rank = Rank.RANKS.SQUIRE;
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
				if(player.getWeaponCount() >= 1) {
					pm.setPlayer(player);
					pm.currentDiscard(1, AdventureCard.TYPE.WEAPONS);
					String cards = null;
					try {
						cards = actions.take();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pm.discardFromHand(player, cards);
				} else if(player.getFoeCount() >= 1) {
					pm.setPlayer(player);
					pm.currentDiscard(Math.min(2, player.getFoeCount()), AdventureCard.TYPE.FOES);
					String cards = null;
					try {
						cards = actions.take();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pm.discardFromHand(player, cards);
				} else {
					//sheet you got no cards gg
				}
			});
			
		}  
	}
}
