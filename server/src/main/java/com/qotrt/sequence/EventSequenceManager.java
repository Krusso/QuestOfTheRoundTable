package com.qotrt.sequence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.EventCard;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;


public class EventSequenceManager extends SequenceManager{

	private EventCard card;

	public EventSequenceManager(EventCard card2) {
		this.card = card2;
	}

	final static Logger logger = LogManager.getLogger(EventSequenceManager.class);

	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm, boolean racing) {
		logger.info("Event Sequence Manager starting: " + card.getName());
		
		card.implementation.perform(logger, pm, bmm);
		
		logger.info("Event Sequence Manager over");
	}

}
