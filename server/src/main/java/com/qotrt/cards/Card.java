package com.qotrt.cards;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Card {
	
	final static Logger logger = LogManager.getLogger(Card.class);
	
	static final AtomicInteger NEXT_ID = new AtomicInteger(0);
	
	public final int id = NEXT_ID.getAndIncrement();
	private String name;

	public boolean inPlay = false;
	
	private int merlinUses = 1;

	public Card(String name) {
		this.name = name;

	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public boolean isMerlin() {
		if(name.equalsIgnoreCase("Merlin")) {
			return true;
		}
		return false;
	}
	
	public boolean isMordred() {
		if(name.equalsIgnoreCase("Mordred")) {
			return true;
		}
		return false;
	}
	
	public boolean tryUseMerlin() {
		logger.info("Merlin uses: " + merlinUses);
		if(merlinUses > 0) {
			merlinUses--;
			return true;
		}
		return false;
	}
	
	public void resetMerlinCharges() {
		merlinUses = 1;
	}
	
}