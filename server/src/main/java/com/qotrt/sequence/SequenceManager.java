package com.qotrt.sequence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;


public abstract class SequenceManager {

	final static Logger logger = LogManager.getLogger(SequenceManager.class);
	
	public abstract void start(PlayerManager pm, BoardModelMediator bmm);
}