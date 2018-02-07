package src.messages.hand;

import src.messages.Message;

//from server
public class AddCardsServer extends Message {
	public String[] cards;
	
	public AddCardsServer(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = "ADD CARDS";
	}

}
