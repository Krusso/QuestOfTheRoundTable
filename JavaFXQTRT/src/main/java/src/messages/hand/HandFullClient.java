package src.messages.hand;

import src.messages.Message;

// Client
public class HandFullClient extends Message{
	public String[] cards;
	
	public HandFullClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDHANDFULL;
	}
}
