package src.game_logic;

public class Player {

	private Rank.RANKS rank;
	private AdventureDeck advDeck;
	
	public Player() {
		rank = Rank.RANKS.Squire;
	}
	
	public void increaseLevel() {
		if(rank == Rank.RANKS.Squire) {
			rank = Rank.RANKS.Knight;
		} else if (rank == Rank.RANKS.Knight) {
			rank = Rank.RANKS.Champion;
		}
	}
	
}
