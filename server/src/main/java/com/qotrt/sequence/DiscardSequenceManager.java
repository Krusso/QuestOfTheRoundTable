package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.DiscardModel;


public class DiscardSequenceManager extends SequenceManager {
	
	final static Logger logger = LogManager.getLogger(DiscardSequenceManager.class);
	private BoardModelMediator bmm;

	public DiscardSequenceManager(BoardModelMediator bmm) {
		this.bmm = bmm;
	}
	
	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm1, boolean racing) {
		logger.info("Starting discard sequence manager");
		Iterator<Player> players = pm.round();
		ArrayList<Player> toAsk = new ArrayList<Player>();
		while(players.hasNext()) {
			Player player = players.next();
			if(player.hand.size() > 12 ) {
				logger.info("Player: " + player.getID() + " has too many cards: " + player.hand.size());
				toAsk.add(player);
			}
		}
			
		if(toAsk.size() != 0) {
			DiscardModel dm  = bmm.getDiscardModel();
			dm.start(toAsk);
			
			logger.info("Waiting for players to discard cards");
			
			try {
				dm.discard().await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
		}

		pm.flipCards(toAsk.iterator());
		
		logger.info("Ending discard sequence manager");
	}

}
