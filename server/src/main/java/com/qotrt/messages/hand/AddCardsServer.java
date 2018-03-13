package com.qotrt.messages.hand;

import com.qotrt.messages.Message;

//from server
public class AddCardsServer extends Message {
	public String[] cards;
	
	// TODO: also send the IDs of the cards
	public AddCardsServer(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.ADDCARDS;
	}

}
