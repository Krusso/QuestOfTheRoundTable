package com.qotrt.model;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.gameplayer.Player;

public class DiscardModel extends Observable {

	private Confirmation discard = new MultiShotConfirmation(null, null, null);
	
	public synchronized void start(ArrayList<Player> toAsk) {
		discard.start(toAsk);
		fireEvent("discard", null, toAsk.stream().toArray(Player[]::new));
	}

	public synchronized CountDownLatch discard() {
		return discard.getCountDownLatch();
	}

	public synchronized boolean finishDiscarding(Player player) {
		if(player.hand.size() > 12) {
			return false;
		} else {
			return discard.accept(player, "player: " + player + " attempting to finish discarding",
					"player: " + player.getID() + " finished discarding",
					"player: " + player + " finished discarding too late");
		}
	}
	
	
}
