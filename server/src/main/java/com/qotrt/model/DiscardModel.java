package com.qotrt.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.qotrt.cards.AdventureCard;
import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.gameplayer.Player;

public class DiscardModel extends Observable implements PropertyChangeListener, Discard {

	private Confirmation discard;
	private Map<Player, AdventureDeck> cards = new HashMap<Player, AdventureDeck>();

	public DiscardModel() {
		discard = new MultiShotConfirmation(null, "finishHandDiscard", null, true);
		discard.subscribe(this);
	} 
	
	@Override
	//propagating events up
	public void propertyChange(PropertyChangeEvent arg0) {
		fireEvent(arg0.getPropertyName(), arg0.getOldValue(), arg0.getNewValue());
	}
	
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

	public synchronized AdventureCard findCard(Player p, int id) {
		return cards.get(p).findCardByID(id);
	}

	public synchronized String playCard(Player p, int id, AdventureDeck adventureDeck) {
		cards.get(p).addCard(adventureDeck.getCardByID(id));
		return "";
	}

	public synchronized String finishDiscarding(Player player) {
		if(player.hand.size() != 12) {
			return "Please discard until you have 12 cards in your hand";
		} else {
			discard.accept(player, "player: " + player + " attempting to finish discarding",
					"player: " + player.getID() + " finished discarding",
					"player: " + player + " finished discarding too late");
			return "";
		}
	}


}
