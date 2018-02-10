package src.sequence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import src.game_logic.BoardModel;
import src.game_logic.QuestCard;
import src.messages.QOTRTQueue;
import src.messages.quest.QuestBidClient;
import src.messages.tournament.TournamentPickCardsClient;
import src.player.BidCalculator;
import src.player.Player;
import src.player.PlayerManager;

public abstract class SequenceManager {

	public abstract void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm);

	protected void questionPlayersTournament(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions) {
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.PICKING);
			TournamentPickCardsClient cards = actions.take(TournamentPickCardsClient.class);
			pm.currentFaceDown(cards.cards);
		}
	}

	protected Player questionPlayersForBid(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions, QuestCard card) {
		Queue<Player> notDropped = new LinkedList<Player>();
		players.forEachRemaining(i -> notDropped.add(i));
		int maxBidValue = Integer.MIN_VALUE;
		BidCalculator bc = new BidCalculator();
		while(notDropped.size() > 1) {
			Player next = notDropped.poll();
			pm.setPlayer(next);
			int playerMaxBid = bc.maxBid(next, card);			
			pm.setBidAmount(next, Player.STATE.BIDDING, playerMaxBid, Math.max(3, maxBidValue));
			QuestBidClient qbc = actions.take(QuestBidClient.class);
			if(qbc.bid != -1) {
				maxBidValue = qbc.bid;
				notDropped.add(next);
			}
		}
		
		return notDropped.poll();
	}
}
