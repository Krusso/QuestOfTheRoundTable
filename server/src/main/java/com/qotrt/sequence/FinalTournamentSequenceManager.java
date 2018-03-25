package com.qotrt.sequence;

import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class FinalTournamentSequenceManager extends SequenceManager {

	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm) {
		logger.info("Starting final tournament sequence manager");
		// TODO: finish this
		//		List<Player> participants = pm.getAllWithState(Player.STATE.WINNING);
		//		if(participants.size() == 1) {
		//			pm.setStates(participants, Player.STATE.GAMEWON);
		//			logger.info("Winners:" + participants);
		//		} else {
		//			questionPlayersTournament(participants.listIterator(), pm, actions);
		//
		//			Iterator<Player> players = participants.iterator();
		//			pm.flipCards(players);	
		//
		//			BattlePointCalculator bpc = new BattlePointCalculator(pm);
		//
		//			List<Player> winners = bpc.calculateHighest(participants, null);
		//			pm.setStates(winners, Player.STATE.GAMEWON);
		//			logger.info("Winners:" + winners);
		//		}
	}

}
