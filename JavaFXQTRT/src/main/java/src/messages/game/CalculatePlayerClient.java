package src.messages.game;

import src.messages.Message;

//Client
public class CalculatePlayerClient extends Message {

	public String[] cards;
	
	public CalculatePlayerClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.CALCULATEPLAYER;
	}
	
}
