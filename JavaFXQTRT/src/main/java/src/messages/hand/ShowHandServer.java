package src.messages.hand;

import src.messages.Message;

//from server
public class ShowHandServer extends Message {
	public String[] cards;
	
	public ShowHandServer(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = "SHOW CARDS";
	}

}
