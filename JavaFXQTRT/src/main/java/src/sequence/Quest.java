package src.sequence;

import src.player.Player;
import src.player.PlayerManager;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.Card;
import src.game_logic.AdventureCard;
import src.game_logic.QuestCard;
import src.game_logic.FoeCard;
import src.game_logic.TestCard;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.quest.QuestPickStagesClient;

import java.util.ArrayList;
import java.util.Iterator;
import src.player.BattlePointCalculator;

public class Quest {

	final static Logger logger = LogManager.getLogger(Quest.class);
	
	public static enum TYPE {
		FOE, TEST
	}

	private int stages;
	private int currentStage = -1;
	private QuestCard questCard;
	private Player sponsor;
	private List<List<Card>> quest;

	public Quest(QuestCard questCard, Player sponsor) {
		this.questCard = questCard;
		this.stages = questCard.getNumStages();
		this.sponsor = sponsor;
		this.quest = new ArrayList<List<Card>>();
	}

	// verification for quest stages will be done on client side
	public void setUpQuest(QOTRTQueue actions, PlayerManager pm) {
		while(quest.size()<stages) {
			try {
				QuestPickStagesClient qpsc = actions.take(QuestPickStagesClient.class, MESSAGETYPES.PICKSTAGES);
				int stage = qpsc.stage;
				String[] cards = qpsc.cards;
				List<Card> cardlist = new ArrayList<Card>();
				for(int i=0; i<cards.length; i++) {
					AdventureCard card = (AdventureCard) sponsor.getCard(cards[i]);
					logger.info(card + " " + questCard);
					if (card.checkIfNamed(questCard.getFoe())) card.name();
					cardlist.add(card);
				}
				quest.add(stage, cardlist);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		if(quest.size()>0) {
			currentStage = 0;
			pm.questDown(sponsor, quest);
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

	public void advanceStage() { this.currentStage++; }
	public int getCurrentStage() { return this.currentStage; }
	public int getNumStages() { return this.stages; }

	public int getNumCards() {
		int count = 0;
		for(List<Card> stage : quest) {
			count += stage.size();
		}
		return count;
	}

	public int getFoeBP() {
		int fbp = 0;
		for(Card card : quest.get(currentStage)) {
			AdventureCard advCard = (AdventureCard) card;
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