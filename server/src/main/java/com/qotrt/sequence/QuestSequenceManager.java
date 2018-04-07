package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.calculator.BidCalculator;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.messages.quest.QuestWinServer.WINTYPES;
import com.qotrt.model.BoardModel;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.QuestModel;

public class QuestSequenceManager extends SequenceManager {

	private QuestCard card;
	private Quest quest;
	private PlayerManager pm;
	private QuestModel qm;
	private BoardModel bm;
	private BattlePointCalculator bc;
	private boolean racing;

	final static Logger logger = LogManager.getLogger(QuestSequenceManager.class);

	public QuestSequenceManager(QuestCard card) { 
		this.card = card; 
	}

	private void getSponsor() {
		// Finding player who wants to sponsor quest
		Iterator<Player> players = pm.round();
		List<Player> potentialSponsors = new ArrayList<Player>();
		players.forEachRemaining(i -> {
			if(bc.canSponsor(i, card)) {
				logger.info("Potential sponsor: " + i.getID() + " player: " + i + " list: " + potentialSponsors);
				potentialSponsors.add(i);
				logger.info("Potential sponsor: " + i.getID() + " player: " + i + " list: " + potentialSponsors);
			}
		});

		logger.info("Players asking to sponsor: " + potentialSponsors);
		qm.questionSponsorPlayers(potentialSponsors);

		// Wait for responses
		try {
			if(racing) {
				qm.sponsorLatch().await(60, TimeUnit.SECONDS);	
			} else {
				qm.sponsorLatch().await();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private void joinQuest(Player sponsor) {
		// Finding players who want to join
		Iterator<Player> players = pm.round();
		List<Player> potentialQuestPlayers = new ArrayList<Player>();
		players.forEachRemaining(i -> {
			if(i != sponsor) {
				potentialQuestPlayers.add(i);
			}
		});

		logger.info("questioning players about joining");
		qm.questionJoinQuest(potentialQuestPlayers);

		// Wait for responses
		try {
			if(racing) {
				qm.participateLatch().await(60, TimeUnit.SECONDS);	
			} else {
				qm.participateLatch().await();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm, boolean racing) {
		logger.info("Starting quest sequence: " + this.card.getName());
		this.bc = new BattlePointCalculator(pm);
		this.qm = bmm.getQuestModel();
		this.bm = bmm.getBoardModel();
		this.racing = racing;

		this.pm = pm;

		getSponsor();

		// determining if anyone decided to sponsor
		List<Player> sponsors = qm.getPlayerWhoSponsor();
		logger.info("sponsors: " + sponsors);
		if(sponsors.size() == 0) {
			qm.setMessage(WINTYPES.NOSPONSOR);
			qm.setWinners(new ArrayList<Player>());
			return;
		}

		// setting up quest
		Player sponsor = sponsors.get(0);

		quest = new Quest(card, qm);
		qm.setQuest(quest, sponsors);
		quest.setUpQuest();

		joinQuest(sponsor);
		
		List<Player> participants = qm.playerWhoJoined();
		// reordering participants
		if(participants.size() != 0 && participants.get(participants.size() - 1).getID() > sponsor.getID()) {
			while(participants.get(0).getID() < sponsor.getID()) {
				participants.add(participants.remove(0));
			}
		}
		
		List<Player> winners = new ArrayList<Player>(participants);
		pm.drawCards(new ArrayList<Player>(), 0);
		handleQuest(participants, winners);

		// handle resolution of the quest
		if(winners.size() > 0) {
			if(bm.isSetKingRecognition()) {
				bm.setSetKingRecognition(false);
				logger.info("King Recognition");
				pm.changeShields(winners, quest.getNumStages() + 2);	
			} else {
				pm.changeShields(winners, quest.getNumStages());
			}
		}

		pm.discardCards(participants);
		pm.drawCards(sponsors, quest.getNumStages() + quest.getNumCards());

		if(participants.size() != 0) {
			logger.info("Winners of the Quest: " + Arrays.toString(winners.stream().map(i -> i.getID()).toArray(Integer[]::new)));
			qm.setMessage(WINTYPES.WON);
			qm.setWinners(winners);
		} else {
			logger.info("No player join the tournament");
			qm.setMessage(WINTYPES.NOJOIN);
			qm.setWinners(new ArrayList<Player>());
		}

		// sleep 5 seconds so that users can see who won or lost
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		logger.info("finished quest sequence: " + this.card.getName());
	}

	private void handleQuest(List<Player> participants, List<Player> winners) {
		while(participants.size() != 0 && winners.size() > 0 && quest.getCurrentStage() < quest.getNumStages()) {
			logger.info("Stage: " + quest.getCurrentStage() + " participants: " + winners.size());
			if (quest.currentStageType() == TYPE.FOES) {
				handleFoe(participants, winners);
			} else if (quest.currentStageType() == TYPE.TESTS) {
				handleBid(participants, winners);
			}
			logger.info("# of Players still in quest: " + winners.size());
			quest.advanceStage();
			qm.setMessage(WINTYPES.PASSSTAGE);
			qm.passStage(winners);
			
			// sleep 5 seconds so that users can see who won or lost
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			pm.drawCards(winners, 1);
		}
	}

	private void handleBid(List<Player> participants, List<Player> winners) {
		qm.flipStage();

		if(winners.size() == 1) {
			qm.questionBid(winners, new BidCalculator(pm), card, (card.getName().equals("Test of the Questing Beast") &&
					card.getName().equals("Search for the Questing Beast") ? 4 : 3));	
		} else {
			qm.questionBid(winners, new BidCalculator(pm), card, (card.getName().equals("Test of the Questing Beast") && card.getName().equals("Search for the Questing Beast")
					? 4 : (card.getName().equals("Test of Morgan Le Fey") ? 3 : 1)));
		}
		
		try {
			logger.info("Waiting for users to finish bidding");
			if(racing) {
				qm.bidLatch().await(60, TimeUnit.SECONDS);	
			} else {
				qm.bidLatch().await();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		logger.info("users finished bidding");

		List<Player> bidWinner = qm.getBidWinner();
		int maxBid = qm.getMaxBid();
		if(bidWinner.size() == 0 || bidWinner.get(0) == null) {
			winners.clear();
		} else {
			qm.discardCards(bidWinner, maxBid);
			try {
				logger.info("Waiting for user to finish discarding");
				qm.discardLatch().await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			winners.clear();
			winners.add(bidWinner.get(0));
		}
	}

	private void handleFoe(List<Player> participants, List<Player> winners) {
		qm.questionCardsStage();
		try {
			logger.info("Waiting for 60 seconds for users to pick their cards");
			if(racing) {
				qm.cardsLatch().await(60, TimeUnit.SECONDS);	
			} else {
				qm.cardsLatch().await();;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		qm.finishPicking();

		qm.flipStage();

		quest.battleFoe(winners, pm);
		
		pm.flipCards(participants.iterator());
		// sleep 5 seconds so that users can see who won or lost
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		pm.discardWeapons(participants);
	}
}