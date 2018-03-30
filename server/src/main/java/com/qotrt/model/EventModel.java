package com.qotrt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.gameplayer.Player;

public class EventModel extends Observable implements Discard{

	private Confirmation discard = new MultiShotConfirmation(null, "finishDiscard", null);
	private Map<Player, GenericPair2<TYPE, Integer>> map = new HashMap<Player, GenericPair2<TYPE, Integer>>();
	private Map<Player, AdventureDeck> cards = new HashMap<Player, AdventureDeck>();

	public synchronized void start(ArrayList<Player> highest) {

		List<Player> players = highest.stream().filter(p -> {
			if(p.getTypeCount(TYPE.WEAPONS) >= 1) {
				System.out.println("Player: " + p.getID() + " discarding: 1 weapon card");
				map.put(p, new GenericPair2<TYPE, Integer>(TYPE.WEAPONS, 1));
			} else if(p.getTypeCount(TYPE.FOES) >= 1) {
				System.out.println("Player: " + p.getID() + " discarding: foe cards: " +  Math.min(2, p.getTypeCount(TYPE.FOES)));
				map.put(p, new GenericPair2<TYPE, Integer>(TYPE.FOES, Math.min(2, p.getTypeCount(TYPE.FOES))));
			} else {
				return false;
			}

			cards.put(p, new AdventureDeck());
			return true;
		}).collect(Collectors.toList());

		fireEvent("discardEvent", null, players.stream().toArray(Player[]::new));

		discard.start(players);
	}

	public synchronized CountDownLatch discard() {
		return discard.getCountDownLatch();
	}

	public synchronized String playCard(Player p, int id, AdventureDeck adventureDeck) {
		AdventureCard c = adventureDeck.findCardByID(id);
		System.out.println("Playing card: " + id + " " + c);
		if(map.get(p).key != c.getType()) {
			return "Discard " + map.get(p).value + " cards of type " + map.get(p).key;
		} else if(map.get(p).value == 0) {
			return "Already have the correct amount of cards discarded";
		} 

		cards.get(p).addCard(adventureDeck.getCardByID(id));
		GenericPair2<TYPE, Integer> pair = map.get(p);
		pair.value = pair.value - 1;
		map.put(p, pair);

		return "";
	}

	public synchronized AdventureCard findCard(Player p, int id) {
		return cards.get(p).findCardByID(id);
	}
	
	public synchronized AdventureCard getCard(Player p, int id) {
		return cards.get(p).getCardByID(id);
	}

	public synchronized boolean can() {
		return discard.can();
	}

	public synchronized String finishDiscarding(Player player) {
		if(map.get(player).value != 0) {
			return "Need to play: " + map.get(player).value + " more " + map.get(player).key + " cards";
		}
		discard.accept(player, "player: " + player + " attempting to finish discarding",
				"player: " + player.getID() + " finished discarding",
				"player: " + player + " finished discarding too late");
		return "";
	}

	public synchronized void finish() {
		fireEvent("finishEvent", null, 0);
	}

}
