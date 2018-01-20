package src.game_logic;

import src.game_logic.Rank.RANKS;

public class Player {

	private RANKS rank;
	private AdventureDeck advDeck;
	
	public Player() {
		rank = Rank.RANKS.Squire;
		advDeck = new AdventureDeck();
	}
	
	public void increaseLevel() {
		if(rank == Rank.RANKS.Squire) {
			rank = Rank.RANKS.Knight;
		} else if (rank == Rank.RANKS.Knight) {
			rank = Rank.RANKS.Champion;
		}
	}
	
	public void addCards(Card[] cards) {
		for(Card card: cards) {
			advDeck.addCard(card, 1);
		}
	}

	public String hand() {
		return advDeck.toString();
	}

}
