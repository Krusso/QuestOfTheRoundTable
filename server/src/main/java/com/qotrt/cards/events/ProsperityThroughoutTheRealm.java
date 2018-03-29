package com.qotrt.cards.events;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class ProsperityThroughoutTheRealm implements EventImplementation {

	@Override
	// All players may immediately draw 2 adventure cards
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		ArrayList<Player> list = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> list.add(i));
		pm.drawCards(list, 2);
		logger.info("All players draw 2 cards");
	}

}
