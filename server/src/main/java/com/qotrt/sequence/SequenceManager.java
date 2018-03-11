package com.qotrt.sequence;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.QuestCard;
import com.qotrt.cards.TestCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.messages.Message.MESSAGETYPES;
import com.qotrt.model.BoardModel;
import com.qotrt.model.BoardModelMediator;


public abstract class SequenceManager {

	final static Logger logger = LogManager.getLogger(SequenceManager.class);
	
	public abstract void start(PlayerManager pm, BoardModelMediator bmm);
//
//	protected void questionPlayersTournament(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions) {
//		while(players.hasNext()) {
//			Player next = players.next();
//			pm.setPlayer(next);
//			pm.setState(next, Player.STATE.PICKING);
//			TournamentPickCardsClient cards = actions.take(TournamentPickCardsClient.class, MESSAGETYPES.PICKTOURNAMENT);
//			logger.info("Cards user picked: " + Arrays.toString(cards.cards));
//			pm.currentFaceDown(cards.cards);
//		}
//	}
//
//	protected Pair questionPlayersForBid(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions, QuestCard card, TestCard testCard) {
//		Queue<Player> notDropped = new LinkedList<Player>();
//		players.forEachRemaining(i -> notDropped.add(i));
//		int maxBidValue = Integer.MIN_VALUE;
//		int oneLoop = notDropped.size();
//		BidCalculator bc = new BidCalculator(pm);
//		if(notDropped.size() == 1) {
//			Player next = notDropped.poll();
//			pm.setPlayer(next);
//			int playerMaxBid = bc.maxBid(next, card);			
//			pm.setBidAmount(next, Player.STATE.BIDDING, playerMaxBid, (testCard.getName().equals("Test of the Questing Beast") &&
//					card.getName().equals("Search for the Questing Beast") ? 4 : 3));
//			QuestBidClient qbc = actions.take(QuestBidClient.class, MESSAGETYPES.BIDQUEST);
//			if(qbc.bid != -1) {
//				notDropped.add(next);
//			}
//			return new Pair(qbc.bid, notDropped.poll(), bc);
//		}
//		while(notDropped.size() > 1 || oneLoop > 0) {
//			oneLoop--;
//			Player next = notDropped.poll();
//			pm.setPlayer(next);
//			int playerMaxBid = bc.maxBid(next, card);			
//			pm.setBidAmount(next, Player.STATE.BIDDING, playerMaxBid,Math.max(
//					(testCard.getName().equals("Test of the Questing Beast") && card.getName().equals("Search for the Questing Beast")
//							? 4 : (testCard.getName().equals("Test of Morgan Le Fey") ? 3 : 1)), maxBidValue + 1));
//			QuestBidClient qbc = actions.take(QuestBidClient.class, MESSAGETYPES.BIDQUEST);
//			if(qbc.bid != -1) {
//				maxBidValue = qbc.bid;
//				notDropped.add(next);
//			}
//		}
//		
//		return new Pair(maxBidValue, notDropped.poll(), bc);
//	}
}

class Pair{
//	public Player player;
//	public int toBid;
//	private BidCalculator bc;
//	public Pair(int toBid, Player player, BidCalculator bc) {
//		this.player = player;
//		this.bc = bc;
//		this.toBid = toBid;
//	}
//	public int cardsToBid(QuestCard quest) {
//		return bc.cardsToBid(toBid, player, quest);
//	}
}
