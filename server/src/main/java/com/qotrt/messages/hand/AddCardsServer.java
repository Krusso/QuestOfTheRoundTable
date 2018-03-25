package com.qotrt.messages.hand;

import com.qotrt.messages.Message;
import com.qotrt.model.GenericPair;

//from server
public class AddCardsServer extends Message {
	private GenericPair[] cards;
	
	public AddCardsServer() {};
	
	public AddCardsServer(int player, GenericPair[] cards) {
		super(player);
		this.setCards(cards);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.ADDCARDS;
	}

	public GenericPair[] getCards() {
		return cards;
	}

	public void setCards(GenericPair[] cards) {
		this.cards = cards;
	}

}
