package com.qotrt.cards.events;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.gameplayer.Rank;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.EventModel;

public class KingsCallToArms implements EventImplementation {

	@Override
	// The highest ranked players must place 1 weapon in the discard pile. 
	// If unable to do so, 2 Foe cards must be discarded
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		Iterator<Player> players = pm.round();
		Rank.RANKS rank = Rank.RANKS.SQUIRE;
		ArrayList<Player> highest = new ArrayList<Player>();
		while(players.hasNext()) {
			Player player = players.next();
			if(player.getRank().compareTo(rank) > 0) {
				rank = player.getRank();
				highest.clear();
				highest.add(player);
			} else if(player.getRank().compareTo(rank) == 0) {
				highest.add(player);
			}
		}

		
		EventModel em = bmm.getEventModel();
		
		em.start(highest);
		
		logger.info("Waiting for players to discard cards for kings call to arms");
		
		try {
			em.discard().await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		em.finish();
		logger.info("Finished discarding cards");
	}

}
