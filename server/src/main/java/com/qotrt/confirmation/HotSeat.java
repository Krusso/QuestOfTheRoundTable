package com.qotrt.confirmation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.qotrt.gameplayer.Player;
import com.qotrt.model.GenericPair;
import com.qotrt.util.PlayerUtil;

public class HotSeat extends Mode {

	private Iterator<Player> next;
	private String eventName;
	
	@Override
	public void start(List<Player> toAsk, String eventName) {
		next = toAsk.iterator();
		this.eventName = eventName;
		nextPlayerToAsk();
	}

	@Override
	public void start(List<Player> toAsk, Object obj, String eventName)  {
		this.toAsk = toAsk;
		if(eventName != null) {
			fireEvent(eventName, null, new GenericPair(PlayerUtil.playersToIDs(toAsk), obj));
		}
	}

	@Override
	public void nextPlayerToAsk() {
		ArrayList<Player> temp = new ArrayList<Player>();
		if(next.hasNext()) {
			temp.add(next.next());
			if(eventName != null) {
				fireEvent(eventName, null, PlayerUtil.playersToIDs(temp));
			}
		}
	}

}
