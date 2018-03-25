package com.qotrt.cards;

import com.qotrt.deck.AdventureDeck;

public class FoeCard extends AdventureCard{
	
	public FoeCard(String name, int battlePoints, TYPE type) {
		super(name, battlePoints, type);
	}
	
	public FoeCard(String name, int battlePoints, int namedBattlePoints, TYPE type) {
		super(name, battlePoints, namedBattlePoints, type);
	}

	@Override
	public String playForStage(AdventureDeck stage) {
		if(stage.typeCount(TYPE.FOES) == 0 && stage.typeCount(TYPE.TESTS) == 0) {
			return "";
		}
		return "Cannot play more than 1 foe or test per stage";
	}

	@Override
	public String playFaceDown(AdventureDeck faceDownDeck, AdventureDeck faceUpDeck) {
		return "Cant play foe face down";
	}
}