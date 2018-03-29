package com.qotrt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.qotrt.cards.AdventureCard;
import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.gameplayer.Player;

public class DiscardModel extends Observable {

	private Confirmation discard = new MultiShotConfirmation(null, "finishHandDiscard", null);
	private Map<Player, AdventureDeck> cards = new HashMap<Player, AdventureDeck>();
	
	public synchronized void start(ArrayList<Player> toAsk) {
		for(Player p: toAsk) {
			cards.put(p, new AdventureDeck());
		}
		discard.start(toAsk);
		fireEvent("discard", null, toAsk.stream().toArray(Player[]::new));
	}

	public synchronized boolean can() {
		return discard.can();
	}
	
	public synchronized CountDownLatch discard() {
		return discard.getCountDownLatch();
	}

	public synchronized AdventureCard getCard(Player p, int id) {
		return cards.get(p).getCardByID(id);
	}
	
	public synchronized void playCard(Player p, int id, AdventureDeck adventureDeck) {
		cards.get(p).addCard(adventureDeck.getCardByID(id));
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
