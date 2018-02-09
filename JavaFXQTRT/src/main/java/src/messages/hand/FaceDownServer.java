package src.messages.hand;

import src.messages.Message;

//from server
public class FaceDownServer extends Message {
	public String[] cards;
	
	public FaceDownServer(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.FACEDOWNCARDS;
	}

}
