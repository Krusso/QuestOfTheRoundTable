package com.qotrt.cards;

import com.qotrt.deck.AdventureDeck;

public class AmourCard extends AdventureCard {
	
	private int bids;
	
	public AmourCard(String name, int battlePoints, int bids, TYPE type) {
		super(name, battlePoints, type);
		this.bids = bids;
	}

	public int getBids() { return this.bids; }

	@Override
	public String playForStage(AdventureDeck deck) {
		return "Cannot play Ally card for quest stage";
	}

	@Override
	public String playFaceDown(AdventureDeck faceDownDeck, AdventureDeck faceUpDeck) {
		if(faceDownDeck.typeCount(TYPE.AMOUR) == 0 && faceUpDeck.typeCount(TYPE.AMOUR) == 0) {
			return "";
		}
		return "Can only play one amour";
	}
}