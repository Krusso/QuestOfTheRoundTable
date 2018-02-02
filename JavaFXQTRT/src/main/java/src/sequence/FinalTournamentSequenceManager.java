package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import src.game_logic.BoardModel;
import src.player.BattlePointCalculator;
import src.player.Player;
import src.player.PlayerManager;

public class FinalTournamentSequenceManager extends SequenceManager {

	@Override
	public void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm) {
		List<Player> participants = pm.getAllWithState(Player.STATE.WINNING);
		questionPlayers(participants.listIterator(), pm, actions);
		
		Iterator<Player> players = participants.iterator();
		while(players.hasNext()) {
			pm.flipCards(players.next());	
		}
		
		BattlePointCalculator bpc = new BattlePointCalculator();
		List<Player> winners = bpc.calculateHighest(participants);
		pm.setGameWinners(winners);
		
	}

}
