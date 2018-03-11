package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestSequenceManager {

//	QuestCard card;
//	Quest quest;
//	Player sponsor;
//
//	final static Logger logger = LogManager.getLogger(QuestSequenceManager.class);
//	
//	public QuestSequenceManager(QuestCard card) { this.card = card; }
//
//	@Override
//	public void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
//		logger.info("Starting quest sequence: " + this.card.getName());
//		BattlePointCalculator bc = new BattlePointCalculator(pm);
//		// Finding player who wants to sponsor quest
//		Iterator<Player> players = pm.round();
//		while(players.hasNext()) {
//			Player next = players.next();
//			pm.setPlayer(next);
//			if(bc.canSponsor(next, card)) {
//				pm.setState(next, Player.STATE.QUESTQUESTIONED);	
//			} else {
//				pm.setState(next, Player.STATE.QUESTQUESTIONEDCANT);
//			}
//			QuestSponsorClient qsc = actions.take(QuestSponsorClient.class, MESSAGETYPES.SPONSERQUEST);
//			if(qsc.sponser) {
//				pm.setState(next, Player.STATE.SPONSORING, card.getNumStages());
//				break;
//			} else {
//				pm.setState(next, Player.STATE.NO);
//			}
//		}
//
//		// determining if anyone decided to sponsor
//		List<Player> sponsors = pm.getAllWithState(Player.STATE.SPONSORING);
//		if(sponsors.size() == 0) {
//			pm.sendContinue("No Player Sponsored the Tournament");
//			return;
//		} else if(sponsors.size() == 1) {
//			sponsor = sponsors.get(0);
//			quest = new Quest(card, sponsor);
//			quest.setUpQuest(actions,pm);
//		} // never more than one sponsor
//
//		pm.flushState();
//
//		// Finding players who want to join
//		players = pm.round();
//		while(players.hasNext()) {
//			Player curr = players.next();
//			if(curr == sponsor) continue;
//			pm.setPlayer(curr);
//			pm.setState(curr, Player.STATE.QUESTJOINQUESTIONED);
//			QuestJoinClient qjc = actions.take(QuestJoinClient.class, MESSAGETYPES.JOINQUEST);
//			if(qjc.joined) {
//				pm.setState(curr, Player.STATE.YES);
//			} else {
//				pm.setState(curr, Player.STATE.NO);
//			}
//		}
//
//		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
//		if(participants.size() != 0 && participants.get(participants.size() - 1).getID() > sponsor.getID()) {
//			while(participants.get(0).getID() < sponsor.getID()) {
//				participants.add(participants.remove(0));
//			}
//		}
//		List<Player> winners = new ArrayList<Player>(participants);
//
//		if(participants.size() != 0) {
//			while(winners.size() > 0 && quest.getCurrentStage() < quest.getNumStages()) {
//				logger.info("Stage: " + quest.getCurrentStage() + " participants: " + winners.size());
//				pm.drawCards(winners, 1);
//				if (quest.currentStageType() == Quest.TYPE.FOE) {
//					players = winners.iterator();
//					// some constraints when choosing cards... can be done on client side c:
//					while(players.hasNext()) {
//						Player pick = players.next();
//						pm.setPlayer(pick);
//						pm.setState(pick, Player.STATE.QUESTPICKING);
//						QuestPickCardsClient qpcc = actions.take(QuestPickCardsClient.class, MESSAGETYPES.PICKQUEST);
//						pm.currentFaceDown(qpcc.cards); 
//					}
//					pm.flipStage(sponsor, quest.getCurrentStage());
//					quest.battleFoe(winners, pm);
//					pm.discardWeapons(participants);
//				} else if (quest.currentStageType() == Quest.TYPE.TEST) {
//					pm.flipStage(sponsor, quest.getCurrentStage());
//					players = winners.iterator();
//					Pair bidWinner = questionPlayersForBid(players, pm, actions, card, (TestCard) quest.getFoeOrTest());
//					logger.info("Bid winner: " + bidWinner.player);
//					if(bidWinner.player == null) {
//						winners.clear();
//						break;
//					}
//					pm.setPlayer(bidWinner.player);
//					pm.setDiscarding(bidWinner.player, Player.STATE.TESTDISCARD, Math.max(0, bidWinner.cardsToBid(card)));
//					QuestDiscardCardsClient qdcc = actions.take(QuestDiscardCardsClient.class, MESSAGETYPES.DISCARDQUEST);
//					pm.discardFromHand(bidWinner.player, qdcc.cards);
//					winners.clear();
//					winners.add(bidWinner.player);
//				}
//				logger.info("# of Players still in quest: " + winners.size());
//				quest.advanceStage();
//				pm.passStage(winners);
//			}
//		}
//		
//		if(winners.size() > 0) {
//			if(bm.isSetKingRecognition()) {
//				bm.setSetKingRecognition(false);
//				logger.info("King Recognition");
//				pm.changeShields(winners, quest.getNumStages() + 2);	
//			} else {
//				pm.changeShields(winners, quest.getNumStages());
//			}
//		}
//		
//		pm.discardCards(participants);
//		pm.drawCards(sponsors, quest.getNumStages() + quest.getNumCards());
//		
//		if(participants.size() != 0) {
//			logger.info("Winners of the Quest: " + Arrays.toString(winners.stream().map(i -> i.getID()).toArray(Integer[]::new)));
//			pm.passQuest(winners);
//		} else {
//			logger.info("No player join the tournament");
//			pm.sendContinue("No Player Joined the Tournament");
//		}
//		
//		logger.info("finished quest sequence: " + this.card.getName());
//	}
}