package com.qotrt.cards;

import com.qotrt.deck.AdventureDeck;

public class WeaponCard extends AdventureCard{
	
	public WeaponCard(String name, int battlePoints, TYPE type) {
		super(name, battlePoints, type);
	}

	@Override
	public String playForStage(AdventureDeck stage) {
		if(stage.findCardByName(this.getName()) != null || stage.typeCount(TYPE.TESTS) != 0) {
			logger.info("cant play duplicate weapon cards or with tests");
			return "Cant play duplicate weapons or with tests";
		}
		logger.info("can play this card");
		return "";
	}

	@Override
	public String playFaceDown(AdventureDeck faceDownDeck, AdventureDeck faceUpDeck) {
		if(faceDownDeck.findCardByName(this.getName()) != null) {
			logger.info("cant play duplicate weapon cards");
			return "Cant play duplicate weapons";
		}
		logger.info("can play this card");
		return "";
	}
	
}