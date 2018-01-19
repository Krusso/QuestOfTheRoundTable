package src.game_logic;

public class TestCard extends AdventureCard {
	
	private int minBids;
	private int namedMinBids;
	
	public TestCard(String name) {
		super(name);
		this.minBids = 3;
		this.namedMinBids = 3;
	}
	
	public TestCard(String name, int minBids) {
		super(name);
		this.minBids = minBids;
		this.namedMinBids = minBids;
	}
	
	public TestCard(String name, int minBids, int namedMinBids) {
		super(name);
		this.minBids = minBids;
		this.namedMinBids = namedMinBids;
	}
	
	public int getMinBids() { return this.minBids; }
	public int getNamedMinBids() { return this.namedMinBids; }
}