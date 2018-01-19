package src.game_logic;

import java.util.ArrayList;
import java.util.stream.IntStream;

public abstract class Deck {
	
	protected ArrayList<Card> deck;
	
	public Deck() {
		this.deck = new ArrayList<Card>();
	}
	
	abstract void populate();
	
	protected void addCard(Card card, int quantity) {
		for(int i=0; i<quantity; i++) {
			deck.add(card);
		}
	}
	
	private Card getNextCard() {
		if(deck.size() == 0) {
			return null;
		}
		return deck.remove((int)(Math.random() * deck.size()));
	}
	
	public Card[] drawCards(int n) {
		Card[] cards = new Card[n];
		IntStream.range(0, n).forEach(i -> cards[i] = getNextCard());
		return cards;
	}
	
	public int size() {
		return deck.size();
	}
}