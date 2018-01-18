package src.game_logic;

public class AmourCard extends AdventureCard {
	
	private int bids;
	
	public AmourCard(String name, int battlePoints, int bids) {
		super(name, battlePoints);
		this.bids = bids;
	}
	
	public int getBids() { return this.bids; }
}