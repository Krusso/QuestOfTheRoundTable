package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.gameplayer.Rank;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.FinalTournamentModel;

public class FinalTournamentSequenceManager extends SequenceManager {

	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm, boolean racing) {
		logger.info("Starting final tournament sequence manager");

		FinalTournamentModel ftm = bmm.getFinalTournamentModel();

		List<Player> participants = new ArrayList<Player>();
		pm.round().forEachRemaining(player ->{
			player.increaseLevel();
			if(player.getRank() == Rank.RANKS.KNIGHTOFTHEROUNDTABLE) {
				participants.add(player);
			}
		});

		if(participants.size() == 1) {
			ftm.setWinners(participants);
			logger.info("Winners:" + participants);
		} else {
			logger.info("Starting to question for cards");
			ftm.questionCards(participants);

			try {
				if(racing) {
					ftm.questionCards().await(60, TimeUnit.SECONDS);	
				} else {
					ftm.questionCards().await();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			ftm.finishPicking();
			logger.info("Finished questioning for cards");

			Iterator<Player> players = participants.iterator();
			pm.flipCards(players);

			BattlePointCalculator bpc = new BattlePointCalculator(pm);
			List<Player> winners = bpc.calculateHighest(participants, null);

			ftm.setWinners(winners);
			logger.info("Winners:" + winners);
		}
	}

}
