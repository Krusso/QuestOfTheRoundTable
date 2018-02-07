package src.sequence;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.BoardModel;
import src.messages.QOTRTQueue;
import src.player.Player;
import src.player.PlayerManager;

public abstract class SequenceManager {

	public abstract void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm);

	protected void questionPlayers(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions, String pattern) {
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.PICKING);
			String string;
			string = actions.take();
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(string);
			m.find();
			String cards = m.group(2);
			pm.currentFaceDown(cards);
			System.out.println("Action recieved: " + string); 
		}
	}

	protected Player questionPlayersForBid(Iterator<Player> players, PlayerManager pm, QOTRTQueue actions, String pattern) {
		Player maxBid = null;
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.BIDDING);
			String string;
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
		}
		return maxBid;
	}
}
