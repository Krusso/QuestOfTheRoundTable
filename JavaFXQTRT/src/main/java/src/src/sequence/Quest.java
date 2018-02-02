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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ListIterator;

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
	
	// reminder that verification for quest stages will be done on client side
	public void setUpQuest(LinkedBlockingQueue<String> actions) {
		String string;
		while(quest.size()<stages) {
			try {
				string = actions.take();
				Pattern p = Pattern.compile("quest stage picked: (^\\d$) cards: (.*)");
				Matcher m = p.matcher(string);
				m.find();
				int stage = Integer.parseInt(m.group(1));
				String[] cards = m.group(2).split(",");
				List<Card> cardlist = new ArrayList<Card>();
				for(int i=0; i<cards.length; i++) {
					AdventureCard card = (AdventureCard) sponsor.getCard(cards[i]);
					if (card.checkIfNamed(questCard.getName())) card.name();
					cardlist.add(card);
				}
				quest.add(stage, cardlist);
				this.currentStage = 0;
				System.out.println("Quest Stage recieved: " + string);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	
	public void bid(List<Player> participants, PlayerManager pm, LinkedBlockingQueue<String> actions) {
		
		String[] highestBid = {};
		int winningPlayerNum = -1;

		ListIterator<Player> players = participants.listIterator();
		
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentBid();
			String string;
			try {
				string = actions.take();
				Pattern p = Pattern.compile("quest bid: player (\\d+) (.*)");
			    Matcher m = p.matcher(string);
			    m.find();
			    String[] cards = m.group(2).split(",");
				System.out.println("Action recieved: " + string);
				
				if(cards.length > highestBid.length) {
					highestBid = cards;
					winningPlayerNum = Integer.parseInt(m.group(1));
				} else {
					players.remove();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}