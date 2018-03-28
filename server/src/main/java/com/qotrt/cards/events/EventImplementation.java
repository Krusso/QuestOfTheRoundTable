package com.qotrt.cards.events;

import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;

public interface EventImplementation {
	
	public abstract void perform(Logger logger, PlayerManager pm, BoardModelMediator bmm);
	
}
