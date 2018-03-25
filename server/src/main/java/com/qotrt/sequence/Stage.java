package com.qotrt.sequence;

import java.util.List;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.deck.AdventureDeck;

public class Stage {
	
	private AdventureDeck cards;
	
	public Stage(List<AdventureCard> list) {
		cards = new AdventureDeck();
		cards.addCards(list);
	}
	
	public AdventureCard getCardByID(int id) {
		return cards.getCardByID(id);
	}
	
	public void addCard(AdventureCard c) {
		cards.addCard(c);
	}
	
	public boolean isFoeStage() {
		return this.cards.typeCount(TYPE.FOES) == 0;
	}
	
	public boolean isTestStage() {
		return this.cards.typeCount(TYPE.TESTS) == 0;
	}
	
	public List<AdventureCard> getStageCards(){
		return this.cards.getDeck();
	}
	
	public String validToAdd(AdventureCard c) {
		return c.playForStage(cards);
	}

	public AdventureCard findCardByID(int id) {
		return cards.findCardByID(id);
	}

}
