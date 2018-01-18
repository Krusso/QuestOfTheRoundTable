package src.game_logic;

public class Player {

	private Rank.RANKS rank;
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
