package com.qotrt.cards;

import com.qotrt.deck.AdventureDeck;

public class TestCard extends AdventureCard {
	
	private int minBids;
	private int namedMinBids;
	
	public TestCard(String name, TYPE type) {
		super(name, type);
		this.minBids = 3;
		this.namedMinBids = 3;
	}
	
	public TestCard(String name, int minBids, TYPE type) {
		super(name, type);
		this.minBids = minBids;
		this.namedMinBids = minBids;
	}
	
	public TestCard(String name, int minBids, int namedMinBids, TYPE type) {
		super(name, type);
		this.minBids = minBids;
		this.namedMinBids = namedMinBids;
	}
	
	public int getMinBids() { return this.minBids; }
	public int getNamedMinBids() { return this.namedMinBids; }

	@Override
	public String playForStage(AdventureDeck stage) {
		if(stage.typeCount(TYPE.FOES) == 0 && stage.typeCount(TYPE.TESTS) == 0) {
			return "";
		}
		return "Cannot play more than 1 foe or test per stage";
	}

	@Override
	public String playFaceDown(AdventureDeck faceDownDeck, AdventureDeck faceUpDeck) {
		return "Cant play test card facedown";
	}
}