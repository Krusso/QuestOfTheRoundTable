package com.qotrt.cards.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class ChivalrousDeed implements EventImplementation {

	@Override
	// Players with both lowest rank and least amount of shields, receives 3 shields
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		Iterator<Player> players = pm.round();
		ArrayList<Player> lowest = new ArrayList<Player>();
		lowest.add(players.next());
		while(players.hasNext()) {
			Player player = players.next();
			if(player.getRank().compareTo(lowest.get(0).getRank()) < 0 ||
					(player.getRank().compareTo(lowest.get(0).getRank()) == 0 && 
					player.getShields() < lowest.get(0).getShields())) {
				lowest.clear();
				lowest.add(player);
			} else if(player.getRank().compareTo(lowest.get(0).getRank()) == 0 &&
					player.getShields() == lowest.get(0).getShields()) {
				lowest.add(player);
			}
		}
		pm.changeShields(lowest, 3);
		logger.info("Players receiving shields: " + Arrays.toString(lowest.stream().map(i -> i.getID()).toArray(Integer[]::new)));
	
	}

}
