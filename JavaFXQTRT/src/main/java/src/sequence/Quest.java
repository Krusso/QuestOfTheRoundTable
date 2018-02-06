package src.sequence;

import src.player.Player;
import src.player.PlayerManager;
import java.util.List;
import src.game_logic.Card;
import src.game_logic.AdventureCard;
import src.game_logic.QuestCard;
import src.game_logic.FoeCard;
import src.game_logic.TestCard;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ListIterator;

import src.player.BattlePointCalculator;

public class Quest {
	
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
	public void setUpQuest(LinkedBlockingQueue<String> actions, PlayerManager pm) {
		String string;
		while(quest.size()<stages) {
			try {
				string = actions.take();
				Pattern p = Pattern.compile("game quest stage picked: (\\d) cards: (.*)");
				Matcher m = p.matcher(string);
				m.find();
				int stage = Integer.parseInt(m.group(1));
				String[] cards = m.group(2).split(",");
				List<Card> cardlist = new ArrayList<Card>();
				for(int i=0; i<cards.length; i++) {
					AdventureCard card = (AdventureCard) sponsor.getCard(cards[i]);
					if (card.checkIfNamed(questCard.getName(), questCard.getFoe())) card.name();
					cardlist.add(card);
				}
				quest.add(stage, cardlist);
				System.out.println("Quest Stage recieved: " + string);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(quest.size()>0) {
			currentStage = 0;
			pm.questDown(sponsor, quest);
		}
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
		for(List stage : quest) {
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
				}
			} else {
				fbp += advCard.getBattlePoints();
			}
		}
		return fbp;
	}
	
	public void battleFoe(List<Player> participants, PlayerManager pm) {
		Iterator<Player> players = participants.iterator();
		while(players.hasNext()) {
			pm.flipCards(players.next());	
		}
		
		BattlePointCalculator bpc = new BattlePointCalculator();
		bpc.getFoeWinners(participants, getFoeBP());
	}
}