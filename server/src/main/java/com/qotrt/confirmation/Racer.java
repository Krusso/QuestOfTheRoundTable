package com.qotrt.confirmation;

import java.util.List;

import com.qotrt.gameplayer.Player;
import com.qotrt.model.GenericPair;
import com.qotrt.util.PlayerUtil;

public class Racer extends Mode {

	@Override
	public void start(List<Player> toAsk, String eventName) {
		this.toAsk = toAsk;
		if(eventName != null) {
			fireEvent(eventName, null, PlayerUtil.playersToIDs(toAsk));
		}
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
		// since racing all players already asked do nothing
	}

}
