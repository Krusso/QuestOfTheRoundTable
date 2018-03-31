package com.qotrt.cards.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class CourtCalledToCamelot implements EventImplementation {

	@Override
	// All Allies in play must be discarded
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		List<Player> list = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> list.add(i));
		pm.discardAllies(list);
		logger.info("Discarding all allies in play");
	}

}
