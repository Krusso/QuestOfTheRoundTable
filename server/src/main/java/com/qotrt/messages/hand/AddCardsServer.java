package com.qotrt.messages.hand;

import com.qotrt.messages.Message;

//from server
public class AddCardsServer extends Message {
	private String[] cards;
	
	public AddCardsServer() {};
	
	// TODO: also send the IDs of the cards
	public AddCardsServer(int player, String[] cards) {
		super(player);
		this.setCards(cards);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.ADDCARDS;
	}

	public String[] getCards() {
		return cards;
	}

	public void setCards(String[] cards) {
		this.cards = cards;
	}

}
