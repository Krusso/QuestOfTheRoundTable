package com.qotrt.sequence;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.QuestModel;

public class Quest {

	final static Logger logger = LogManager.getLogger(Quest.class);

	private int stages;
	private int currentStage = -1;
	private QuestCard questCard;
	private Stage[] quest;
	private QuestModel qm;

	public Quest(QuestCard questCard, QuestModel qm) {
		this.questCard = questCard;
		this.stages = questCard.getNumStages();
		this.quest = new Stage[questCard.getNumStages()];
		this.qm = qm;
	}

	// verification for quest stages will be done on client side
	public void setUpQuest() {
		try {
			qm.stageSetupLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<List<AdventureCard>> stages = qm.getStageCards();
		for(int i = 0; i < stages.size(); i++) {
			quest[i] = new Stage(stages.get(i));
		}
		
		currentStage = 0;
	}

	public TYPE currentStageType() {
		if(quest[currentStage].isFoeStage()) {
			return TYPE.FOES;
		} else if(quest[currentStage].isTestStage()) {
			return TYPE.TESTS;
		}
		return null;
	}

	public List<AdventureCard> getCurrentStageCards(){
		return this.quest[currentStage].getStageCards();
	}
	
	public Stage getStage(int i) {
		return quest[i];
	}
	public void advanceStage() { this.currentStage++; }
	public int getCurrentStage() { return this.currentStage; }
	public int getNumStages() { return this.stages; }

	public int getNumCards() {
		return Arrays.stream(quest).mapToInt(i -> i.getStageCards().size()).sum();
	}

	public int getFoeBP() {
		int fbp = 0;
		for(AdventureCard advCard : quest[currentStage].getStageCards()) {
			if (advCard.getType() == AdventureCard.TYPE.FOES) {
				if (advCard.isNamed()) {
					fbp += advCard.getNamedBattlePoints();
				} else {
					fbp += advCard.getBattlePoints();
				}
			} else {
				fbp += advCard.getBattlePoints();
			}
		}
		logger.info("Foe BP: " + fbp);
		return fbp;
	}

	public void battleFoe(List<Player> participants, PlayerManager pm) {
		Iterator<Player> players = participants.iterator();
		pm.flipCards(players);	

		BattlePointCalculator bpc = new BattlePointCalculator(pm);
		bpc.getFoeWinners(participants, this.questCard, getFoeBP());
	}
}