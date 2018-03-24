package com.qotrt.cards;

import com.qotrt.deck.AdventureDeck;

public class WeaponCard extends AdventureCard{
	
	public WeaponCard(String name, int battlePoints, TYPE type) {
		super(name, battlePoints, type);
	}

	@Override
	public String playForStage(AdventureDeck stage) {
		if(stage.findCardByName(this.getName()) != null) {
			System.out.println("cant play duplicate weapon cards");
			return "Cant play duplicate weapons";
		}
		System.out.println("can play this card");
		return "";
	}

	@Override
	public String playFaceDown(AdventureDeck faceDownDeck, AdventureDeck faceUpDeck) {
		if(faceDownDeck.findCardByName(this.getName()) != null) {
			System.out.println("cant play duplicate weapon cards");
			return "Cant play duplicate weapons";
		}
		System.out.println("can play this card");
		return "";
	}
	
}