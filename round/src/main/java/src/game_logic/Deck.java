package src.game_logic;

import java.util.ArrayList;
import java.util.stream.IntStream;

public abstract class Deck<E> {
	
	protected ArrayList<E> deck;
	
	public Deck() {
		this.deck = new ArrayList<E>();
	}
	
	abstract void populate();
	
	@Override
	public String toString() {
		String toReturn = "";
		for(E card: deck) {
			toReturn += card + " ";
		}
		return toReturn;
	}
	
	public void addCard(E card, int quantity) {
		for(int i=0; i<quantity; i++) {
			deck.add(card);
		}
	}
	
	private E getNextCard() {
		if(deck.size() == 0) {
			return null;
		}
		return deck.remove((int)(Math.random() * deck.size()));
	}
	
	public ArrayList<E> drawCards(int n) {
		ArrayList<E> toReturn = new ArrayList<E>(n);
		IntStream.range(0, n).forEach(i -> toReturn.add(getNextCard()));
		return toReturn;
	}
	
	public int size() {
		return deck.size();
	}
}