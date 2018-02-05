package src.game_logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class Deck<E extends Card> {

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

	public void addCards(List<E> cards) {
		cards.forEach(i -> deck.add(i));
	}

	public void addCard(E card, int quantity) {
		for(int i=0; i<quantity; i++) {
			deck.add(card);
		}
	}

	public E getCard(E cardToFind) {
		for(E card: deck) {
			if(cardToFind == card) {
				deck.remove(card);
				return card;
			}
		}
		return null;
	}

	public E getCardByName(String toFind) {
		for(E card: deck) {
			if(card.getName().equals(toFind)) {
				deck.remove(card);
				return card;
			}
		}
		return null;
	}

	private E getNextCard() {
		if(deck.size() == 0) {
			return null;
		}
		return deck.remove((int)(Math.random() * deck.size()));
	}

	/**
	 *  Warning decks are not shuffled
	 * @param n
	 * @return
	 */
	public ArrayList<E> drawTopCards(int n){
		ArrayList<E> toReturn = new ArrayList<E>(n);
		IntStream.range(0, n).forEach(i -> toReturn.add(deck.remove(0)));
		return toReturn;
	}

	public ArrayList<E> drawRandomCards(int n) {
		ArrayList<E> toReturn = new ArrayList<E>(n);
		IntStream.range(0, n).forEach(i -> toReturn.add(getNextCard()));
		return toReturn;
	}

	public int size() {
		return deck.size();
	}
}