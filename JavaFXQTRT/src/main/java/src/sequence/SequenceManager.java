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

	protected Player questionPlayersForBid(Iterator<Player> players, PlayerManager pm, LinkedBlockingQueue<String> actions, String pattern) {
		Player maxBid = null;
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.currentQuestionBids();
			String string;
			try {
				string = actions.take();
				Pattern p = Pattern.compile(pattern);
			    Matcher m = p.matcher(string);
			    m.find();
				System.out.println("Action recieved: " + string);
				String stringBid = m.group(2);
			    if("skip".equals(stringBid)) {
			    	continue;
			    }
			    maxBid = next;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		return maxBid;
	}
}
