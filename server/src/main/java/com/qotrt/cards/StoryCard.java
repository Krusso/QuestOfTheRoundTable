package com.qotrt.cards;

import com.qotrt.deck.AdventureDeck;

public abstract class StoryCard extends Card {
	
	public static enum TYPE {
		QUEST, EVENT, TOURNAMENT, GAMEOVER;
	}
	
	protected TYPE type;
	
	public StoryCard(String name, TYPE type) {
		super(name);
		this.type = type;
	}
	
	public TYPE getType() { return this.type; }

	@Override
	public String playForStage(AdventureDeck deck) {
		return "Can never play this card for a stage";
	}

	@Override
	public String playFaceDown(AdventureDeck deck, AdventureDeck faceUpDeck) {
		return "Can never play this card face down";
	}
}