package com.qotrt.model;

import com.google.common.eventbus.EventBus;
import com.qotrt.cards.StoryCard;

public class BoardModel extends Observable{

	private StoryCard card;
	private boolean setKingRecognition;
	private EventBus eventBus;
	
	public BoardModel(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public StoryCard getCard() {
		return card;
	}

	public void setCard(StoryCard card) {
		this.card = card;
		
		fireEvent("middlecard", null, card);
		eventBus.post(card);
	}
	
	public boolean isSetKingRecognition() {
		return setKingRecognition;
	}

	public void setSetKingRecognition(boolean setKingRecognition) {
		this.setKingRecognition = setKingRecognition;
	}
}
