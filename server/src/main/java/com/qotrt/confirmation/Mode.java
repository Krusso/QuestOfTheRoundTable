package com.qotrt.confirmation;

import java.util.List;
import java.util.function.Consumer;

import com.qotrt.gameplayer.Player;
import com.qotrt.model.Observable;

public abstract class Mode extends Observable {
	
	protected List<Player> toAsk;

	public abstract void start(List<Player> toAsk, String eventName);

	public abstract void start(List<Player> toAsk, Object obj, String eventName);
	
	public abstract void nextPlayerToAsk();

	public void remove(Player player) {
		toAsk.remove(player);
	}

	public void forAllPlayers(Consumer<Player> c) {
		for(Player player: toAsk) {
			c.accept(player);
		}
	}
	
}
