package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.BoardModel;
import src.game_logic.QuestCard;
import src.player.Player;
import src.player.PlayerManager;
import src.game_logic.Card;


public class QuestSequenceManager extends SequenceManager {
	
	QuestCard card;
	Quest quest;
	
	public QuestSequenceManager(QuestCard card) { this.card = card; }
	
	@Override
	public void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm) {
		// Finding player who wants to sponsor quest
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionQuest();
			String string;
			try {
				string = actions.take();
				System.out.println("Action recieved: " + string);
				Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			    Matcher m = p.matcher(string);
			    m.find();
				if(m.group(3).equals("sponsors")) {
					pm.currentSponsorQuest();
					break;
				} else {
					pm.currentDeclineQuest();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// determining if anyone decided to sponsor
		List<Player> sponsor = pm.getAllWithState(Player.STATE.YES);
		if(sponsor.size() == 0) {
			return;
		} else if(sponsor.size() == 1) {
			// TODO: set card in center of board
			quest = new Quest(card, sponsor.get(0));
			quest.setUpQuest(actions);
		} // never more than one sponsor
		
		pm.flushState();
		
		// Finding players who want to join
		Iterator<Player> playersForQuest = pm.round();
		while(playersForQuest.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionQuest();
			String string;
			try {
				string = actions.take();
				System.out.println("Action recieved: " + string);
				Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			    Matcher m = p.matcher(string);
			    m.find();
				if(m.group(3).equals("join quest")) {
					pm.currentJoinQuest();
				} else {
					pm.currentDeclineQuest();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		pm.drawCards(participants,1);
		
		
	}
}
