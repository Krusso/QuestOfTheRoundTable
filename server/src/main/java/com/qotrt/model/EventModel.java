package com.qotrt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.gameplayer.Player;

public class EventModel extends Observable{

	private Confirmation discard = new MultiShotConfirmation(null, null, null);
	private Map<Player, GenericPair2<TYPE, Integer>> map = new HashMap<Player, GenericPair2<TYPE, Integer>>();
	private Map<Player, AdventureDeck> cards = new HashMap<Player, AdventureDeck>();

	public synchronized void start(ArrayList<Player> highest) {
		for(Player p: highest) {
			if(p.getTypeCount(TYPE.WEAPONS) >= 1) {
				//pm.setState(player, Player.STATE.EVENTDISCARDING, 1, AdventureCard.TYPE.WEAPONS);
				System.out.println("Player: " + p.getID() + " discarding: 1 weapon card");
			} else if(p.getTypeCount(TYPE.FOES) >= 1) {
				//pm.setState(player, Player.STATE.EVENTDISCARDING, Math.min(2, player.getTypeCount(TYPE.FOES)), AdventureCard.TYPE.FOES);
				System.out.println("Player: " + p.getID() + " discarding: foe cards");
			} else {
				highest.remove(p);
			}
			fireEvent("discard", null, p);
		}
		discard.start(highest);
	}

	public synchronized CountDownLatch discard() {
		return discard.getCountDownLatch();
	}
	
	public synchronized String playCard(Player p, int id, AdventureDeck adventureDeck) {
		AdventureCard c = adventureDeck.findCardByID(id);
		if(map.get(p).key != c.getType()) {
			return "Discard " + map.get(p).value + " cards of type " + map.get(p).key;
		} else if(map.get(p).value == 0) {
			return "Already have the correct amount of cards discarded";
		} 
		
		cards.get(p).addCard(adventureDeck.getCardByID(id));
		
		return "";
	}
	
	public synchronized AdventureCard getCard(Player p, int id) {
		return cards.get(p).getCardByID(id);
	}

	public synchronized boolean can() {
		return discard.can();
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

	public AdventureCard findCard(Player player, int card) {
		return cards.get(player).findCardByID(card);
	}

}
