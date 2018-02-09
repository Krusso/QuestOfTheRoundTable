package src.sequence;

import java.util.Iterator;

import src.game_logic.BoardModel;
import src.messages.Message;
import src.messages.QOTRTQueue;
import src.messages.quest.QuestBidClient;
import src.messages.quest.QuestPickCardsClient;
import src.messages.tournament.TournamentPickCardsClient;
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

	protected Player questionPlayersForBid(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions) {
		Player maxBid = null;
		// will be used when no longer hotseat
		int maxBidValue = Integer.MIN_VALUE;
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.BIDDING);
			QuestBidClient qbc = actions.take(QuestBidClient.class);
			if(qbc.bid !=  -1) {
				maxBid = next; 
				maxBidValue = qbc.bid;
			}
		}
		return maxBid;
	}
}
