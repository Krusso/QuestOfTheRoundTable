package src.game_logic;

import java.net.URL;

public class AmourCard extends AdventureCard {
	
	private int bids;
	
	public AmourCard(String name, int battlePoints, int bids, TYPE type) {
		super(name, battlePoints, type);
		this.bids = bids;
	}

	public int getBids() { return this.bids; }
}