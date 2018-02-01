package src.sequence;

import src.player.Player;
import java.util.List;
import src.game_logic.Card;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestCreator {

	int stages;
	Player sponsor;
	List<List<Card>> quest;

	public QuestCreator(int stages, Player sponsor) {
		this.stages = stages;
		quest = new ArrayList<List<Card>>();
	}
	
	public List<List<Card>> setUpQuest(LinkedBlockingQueue<String> actions) {
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
					Card card = (Card) Class.forName(cards[i]).newInstance(); // :^)
					cardlist.add(card);
				}
				quest.add(stage, cardlist);
				System.out.println("Quest Stage recieved: " + string);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return quest;
	}
}
