package com.qotrt.sequence;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.deck.AdventureDeck;

public class Stage {
	
	final static Logger logger = LogManager.getLogger(Stage.class);
	
	private AdventureDeck cards;
	
	public Stage() {
		cards = new AdventureDeck();
	}
	
	public AdventureCard getCardByID(int id) {
		return cards.getCardByID(id);
	}
	
	public void addCard(AdventureCard c) {
		cards.addCard(c);
	}
	
	public boolean isFoeStage() {
		return this.cards.typeCount(TYPE.FOES) != 0;
	}
	
	public boolean isTestStage() {
		return this.cards.typeCount(TYPE.TESTS) != 0;
	}
	
	public List<AdventureCard> getStageCards(){
		return this.cards.getDeck();
	}
	
	public String validToAdd(AdventureCard c) {
		logger.info("checking if card: " + c.getName() + " is valid to play in this stage");
		return c.playForStage(cards);
	}

	public AdventureCard findCardByID(int id) {
		return cards.findCardByID(id);
	}

}
