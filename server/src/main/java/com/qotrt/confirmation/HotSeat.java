package com.qotrt.confirmation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.model.GenericPair;
import com.qotrt.util.PlayerUtil;

public class HotSeat extends Mode {

	final static Logger logger = LogManager.getLogger(HotSeat.class);
	private Iterator<Player> next;
	private String eventName;
	
	@Override
	public void start(List<Player> toAsk, String eventName) {
		this.toAsk = toAsk;
		next = toAsk.iterator();
		this.eventName = eventName;
		logger.info("Starting: " + this.toAsk + " size: " + this.toAsk.size() +  " next: " + next + " eventName: " + this.eventName);
		nextPlayerToAsk();
	}

	@Override
	public void start(List<Player> toAsk, Object obj, String eventName)  {
		this.toAsk = toAsk;
		next = toAsk.iterator();
		if(next.hasNext()) { next.next(); }
		if(eventName != null) {
			fireEvent(eventName, null, new GenericPair(PlayerUtil.playersToIDs(toAsk), obj));
		}
	}

	@Override
	public void nextPlayerToAsk() {
		ArrayList<Player> temp = new ArrayList<Player>();
		logger.info("Next: " + next.hasNext());
		if(next.hasNext()) {
			temp.add(next.next());
			logger.info(temp + " has next: " + next.hasNext());
			if(eventName != null) {
				fireEvent(eventName, null, PlayerUtil.playersToIDs(temp));
			}
		}
	}

}
