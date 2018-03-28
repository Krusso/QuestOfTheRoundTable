package com.qotrt.cards.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class Plague implements EventImplementation {

	@Override
	// Drawer loses 2 shields if possible
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		List<Player> list = new ArrayList<Player>();
		list.add(pm.round().next());
		pm.changeShields(list, -2);
		logger.info("Draw loses card");
	}

}
