package src.game_logic;

import java.util.ArrayList;

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
	
	// TO-DO
	public void shuffle() {}
}