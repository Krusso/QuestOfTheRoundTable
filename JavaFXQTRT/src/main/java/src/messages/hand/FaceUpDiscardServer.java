package src.messages.hand;

import src.messages.Message;

// from server
public class FaceUpDiscardServer extends Message {
	
	public String[] cardsDiscarded;
	
	public FaceUpDiscardServer(int player, String[] cardNames) {
		super(player);
		this.cardsDiscarded = cardNames;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDFACEUP;
	}

}