package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import com.qotrt.game.Game;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.views.EventHandler;

public abstract class AIController extends EventHandler {

	protected Game game;
	protected Player player;
	protected AbstractAI ai;
	
	public AIController(Game game, Player player, AbstractAI ai) {
		this.game = game;
		this.player = player;
		this.ai = ai;
	}
	
	
	protected boolean contains(int[] newValue) {
		for(int i: newValue) {
			if(i == player.getID()) {
				return true;
			}
		}

		return false;
	}
	
	
	protected boolean contains(Player[] newValue) {
		return this.contains(Arrays.stream(newValue).mapToInt(i -> i.getID()).toArray());
	}
	
	public abstract void handleEvent(PropertyChangeEvent evt);
	
}
