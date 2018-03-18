package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.Card;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.cards.TestCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.QuestModel;

public class Quest {

	final static Logger logger = LogManager.getLogger(Quest.class);
	
	public static enum TYPE {
		FOE, TEST
	}

	private int stages;
	private int currentStage = -1;
	private QuestCard questCard;
	private List<List<AdventureCard>> quest;
	private QuestModel qm;

	public Quest(QuestCard questCard, QuestModel qm) {
		this.questCard = questCard;
		this.stages = questCard.getNumStages();
		this.quest = new ArrayList<List<AdventureCard>>();
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
		for(List<AdventureCard> cards: stages) {
			quest.add(cards);
		}
		
		if(quest.size() > 0) {
			currentStage = 0;
		}
	}

	public AdventureCard getFoeOrTest() {
		for(Card c: quest.get(currentStage)) {
			if(c instanceof FoeCard) {
				return (AdventureCard) c;
			} else if(c instanceof TestCard) {
				return (AdventureCard) c;
			}
		}
		return null;
	}
	
	public TYPE currentStageType() {
		for(Card card : quest.get(currentStage)) {
			if (card instanceof FoeCard) {
				return TYPE.FOE;
			} else if (card instanceof TestCard) {
				return TYPE.TEST;
			}
		}
		return null;
	}

	public List<AdventureCard> getCurrentStageCards(){
		return this.quest.get(getCurrentStage());
	}
	
	public void advanceStage() { this.currentStage++; }
	public int getCurrentStage() { return this.currentStage; }
	public int getNumStages() { return this.stages; }

	public int getNumCards() {
		int count = 0;
		for(List<AdventureCard> stage : quest) {
			count += stage.size();
		}
		return count;
	}

	public int getFoeBP() {
		int fbp = 0;
		for(AdventureCard advCard : quest.get(currentStage)) {
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