package src.game_logic;

public class AllyCard extends AdventureCard {
	
	private int bids;
	private int namedBids;
	
	public AllyCard(String name) {
		super(name);
		this.battlePoints = 0;
		this.namedBattlePoints = 0;
		this.bids = 0;
		this.namedBids = 0;
	}
	
	public AllyCard(String name, int battlePoints) {
		super(name, battlePoints);
		this.namedBattlePoints = battlePoints;
	}
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints) {
		super(name, battlePoints);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints, int bids) {
		this(name, battlePoints, namedBattlePoints);
		this.bids = bids;
	}
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints, int bids, int namedBids) {
		this(name, battlePoints, namedBattlePoints, bids);
		this.namedBids = namedBids;
	}

	public int getBids() { return this.bids; }
	public int getNamedBids() { return this.namedBids; }
}