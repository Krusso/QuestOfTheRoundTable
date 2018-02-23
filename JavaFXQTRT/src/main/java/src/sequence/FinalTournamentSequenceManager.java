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
		if(participants.size() == 1) {
			pm.setStates(participants, Player.STATE.GAMEWON);
		} else {
			questionPlayersTournament(participants.listIterator(), pm, actions);

			Iterator<Player> players = participants.iterator();
			pm.flipCards(players);	

			BattlePointCalculator bpc = new BattlePointCalculator(pm);

			List<Player> winners = bpc.calculateHighest(participants, null);
			pm.setStates(winners, Player.STATE.GAMEWON);	
		}
	}

}
