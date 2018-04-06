package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.game.Game;
import com.qotrt.game.PlayCard;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.views.EventHandler;

public abstract class AIController extends EventHandler {

	final static Logger logger = LogManager.getLogger(TestTournament.class);
	
	protected PlayCard pc;
	protected Game game;
	protected Player player;
	protected AbstractAI ai;

	public AIController(Game game, Player player, AbstractAI ai) {
		this.game = game;
		this.player = player;
		this.ai = ai;
		this.pc = new PlayCard();
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
		return this.contains(Arrays.stream(newValue).
				mapToInt(i -> i.getID()).toArray());
	}

	public abstract void handleEvent(PropertyChangeEvent evt);

	public Consumer<PropertyChangeEvent> wrapEvent(PropertyChangeEvent evt, Consumer<PropertyChangeEvent> consumer) {
		logger.info("Wrapping: " + evt.getPropertyName() + " for player: " + player.getID());
		Consumer<PropertyChangeEvent> wrapped = x -> { 
			Executors.newScheduledThreadPool(1).execute(
					new Runnable() {
						@Override
						public void run() {
							try {
								logger.info("AI player: " + player.getID() + " sleeping");
								Thread.sleep(10000);
								logger.info("Consuming: " + evt.getPropertyName() + " value: " + evt.getNewValue() + " player: " + player.getID());
								consumer.accept(evt);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}});
		};
		logger.info("Returning wrapped: " + wrapped);
		return wrapped;
	}

}
