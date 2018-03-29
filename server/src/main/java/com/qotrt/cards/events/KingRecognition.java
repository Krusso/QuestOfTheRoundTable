package com.qotrt.cards.events;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public class KingRecognition implements EventImplementation {

	@Override
	// The next player to complete a Quest will receive 2 extra shields
	public void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm) {
		bmm.getBoardModel().setSetKingRecognition(true);
		logger.info("Set Kings Recognition");
	}

}
