package src.game_logic;

import src.game_logic.AdventureCard.TYPE;

public class AllyCard extends AdventureCard {
	
	private int bids;
	private int namedBids;
	
	public AllyCard(String name, TYPE type) {
		super(name, type);
		this.battlePoints = 0;
		this.namedBattlePoints = 0;
		this.bids = 0;
		this.namedBids = 0;
	}
	
	public AllyCard(String name, String path) {
		super(name, path);
		type = TYPE.ALLIES;
	}
	public AllyCard(String name, int battlePoints, TYPE type) {
		super(name, battlePoints, type);
		this.namedBattlePoints = battlePoints;
	}
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints, TYPE type) {
		super(name, battlePoints, type);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints, int bids, TYPE type) {
		this(name, battlePoints, namedBattlePoints, type);
		this.bids = bids;
	}
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints, int bids, int namedBids, TYPE type) {
		this(name, battlePoints, namedBattlePoints, bids, type);
		this.namedBids = namedBids;
	}

	public int getBids() { return this.bids; }
	public int getNamedBids() { return this.namedBids; }
}