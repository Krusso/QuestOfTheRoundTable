package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.QuestCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModel;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.QuestModel;

public class QuestSequenceManager extends SequenceManager {

	private QuestCard card;
	private Quest quest;

	final static Logger logger = LogManager.getLogger(QuestSequenceManager.class);

	public QuestSequenceManager(QuestCard card) { 
		this.card = card; 
	}

	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm) {
		logger.info("Starting quest sequence: " + this.card.getName());
		BattlePointCalculator bc = new BattlePointCalculator(pm);

		// Finding player who wants to sponsor quest
		Iterator<Player> players = pm.round();
		QuestModel qm = bmm.getQuestModel();
		BoardModel bm = bmm.getBoardModel();
		List<Player> potentialSponsors = new ArrayList<Player>();
		List<Player> cantSponsor = new ArrayList<Player>();
		players.forEachRemaining(i -> {
			if(bc.canSponsor(i, card)) {
				potentialSponsors.add(i);
			} else {
				cantSponsor.add(i);
			}
		});


		qm.questionSponsorPlayers(potentialSponsors, cantSponsor);

		// Wait for responses
		try {
			qm.cdl.await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// determining if anyone decided to sponsor
		Player sponsor = qm.getPlayerWhoSponsor();
		if(sponsor == null) {
			qm.setMessage("No Player Sponsored the Tournament");
			qm.setWinners(new ArrayList<Player>());
			return;
		}

		quest = new Quest(card, qm);
		quest.setUpQuest();

		// Finding players who want to join
		players = pm.round();
		List<Player> potentialQuestPlayers = new ArrayList<Player>();
		players.forEachRemaining(i -> {
			if(i != sponsor) {
				potentialQuestPlayers.add(i);
			}
		});

		qm.questionJoinQuest(potentialQuestPlayers);

		// Wait for responses
		try {
			qm.cdl.await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		List<Player> participants = qm.playerWhoJoined();

		List<Player> winners = new ArrayList<Player>(participants);
		
				if(participants.size() != 0) {
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
				}
				
		if(winners.size() > 0) {
			if(bm.isSetKingRecognition()) {
				bm.setSetKingRecognition(false);
				logger.info("King Recognition");
				pm.changeShields(winners, quest.getNumStages() + 2);	
			} else {
				pm.changeShields(winners, quest.getNumStages());
			}
		}
//				
//				pm.discardCards(participants);
//				pm.drawCards(sponsors, quest.getNumStages() + quest.getNumCards());
//				
//				if(participants.size() != 0) {
//					logger.info("Winners of the Quest: " + Arrays.toString(winners.stream().map(i -> i.getID()).toArray(Integer[]::new)));
//					pm.passQuest(winners);
//				} else {
//					logger.info("No player join the tournament");
//					pm.sendContinue("No Player Joined the Tournament");
//				}
//				
		logger.info("finished quest sequence: " + this.card.getName());
	}
}