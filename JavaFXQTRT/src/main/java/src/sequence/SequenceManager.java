package src.sequence;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.BoardModel;
import src.player.Player;
import src.player.PlayerManager;

public abstract class SequenceManager {

	public abstract void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm);

	protected void questionPlayers(Iterator<Player> players, PlayerManager pm, LinkedBlockingQueue<String> actions, String pattern) {
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionCards();
			String string;
			try {
				string = actions.take();
				Pattern p = Pattern.compile(pattern);
			    Matcher m = p.matcher(string);
			    m.find();
			    String cards = m.group(2);
				pm.currentFaceDown(cards);
				System.out.println("Action recieved: " + string);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

	protected void questionPlayersForBid(Iterator<Player> players, PlayerManager pm, LinkedBlockingQueue<String> actions, String pattern) {
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionCards();
			String string;
			try {
				string = actions.take();
				Pattern p = Pattern.compile(pattern);
			    Matcher m = p.matcher(string);
			    m.find();
			    String cards = m.group(2);
				pm.currentFaceUp(cards);
				System.out.println("Action recieved: " + string);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
}
