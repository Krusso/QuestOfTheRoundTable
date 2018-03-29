package com.qotrt.model;

import com.qotrt.cards.StoryCard;

public class BoardModel extends Observable{

	private StoryCard card;
	private boolean setKingRecognition;
	
	public StoryCard getCard() {
		return card;
	}

	public void setCard(StoryCard card) {
		this.card = card;
		
		fireEvent("middlecard", null, card);
	}
	
	public boolean isSetKingRecognition() {
		return setKingRecognition;
	}

	public void setSetKingRecognition(boolean setKingRecognition) {
		this.setKingRecognition = setKingRecognition;
	}
}
