package src.messages.hand;

import src.messages.Message;

//from server
public class FaceUpServer extends Message {
	public String[] cards;
	
	public FaceUpServer(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = "FACEDOWN CARDS";
	}

}
