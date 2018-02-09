package src.sequence;

import java.util.Iterator;
import java.util.List;

import src.game_logic.BoardModel;
import src.messages.QOTRTQueue;
import src.player.BattlePointCalculator;
import src.player.Player;
import src.player.PlayerManager;

public class FinalTournamentSequenceManager extends SequenceManager {

	@Override
	public void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
		List<Player> participants = pm.getAllWithState(Player.STATE.WINNING);
		questionPlayersTournament(participants.listIterator(), pm, actions);
		
		Iterator<Player> players = participants.iterator();
		while(players.hasNext()) {
			pm.flipCards(players.next());	
		}
		
		BattlePointCalculator bpc = new BattlePointCalculator();
		List<Player> winners = bpc.calculateHighest(participants);
		pm.setState(winners, Player.STATE.GAMEWON);
		
	}

}
