package com.qotrt.cards.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class Pox implements EventImplementation {

	@Override
	// All players except the player drawing this card lose 1 shield
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		List<Player> list = new ArrayList<Player>();
		Iterator<Player> iter = pm.round();
		iter.next();
		iter.forEachRemaining(i -> list.add(i));
		pm.changeShields(list, -1);
		logger.info("All players expect the player drawing pox loses a card");
	}

}
