package src.game_logic;

public class AmourCard extends AdventureCard {
	
	private int bids;
	
	public AmourCard(String name, int battlePoints, int bids, TYPE type) {
		super(name, battlePoints, type);
		this.bids = bids;
	}
	public AmourCard(String name, String path) {
		super(name, path);
		type = TYPE.AMOUR;
	}
	public int getBids() { return this.bids; }
}