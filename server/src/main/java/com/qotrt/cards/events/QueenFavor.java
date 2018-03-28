package com.qotrt.cards.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.gameplayer.Rank;
import com.qotrt.model.BoardModelMediator;

public class QueenFavor implements EventImplementation {

	@Override
	// The lowest ranked player(s) immediately receives 2 Adventure Cards
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		Iterator<Player> players = pm.round();
		Rank.RANKS rank = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
		ArrayList<Player> lowest = new ArrayList<Player>();
		while(players.hasNext()) {
			Player player = players.next();
			if(player.getRank().compareTo(rank) < 0) {
				rank = player.getRank();
				lowest.clear();
				lowest.add(player);
			} else if(player.getRank().compareTo(rank) == 0) {
				lowest.add(player);
			}
		}
		logger.info("Giving cards to: " + Arrays.toString(lowest.stream().map(i -> i.getID()).toArray(Integer[]::new)));
		pm.drawCards(lowest, 2);
	}

}
