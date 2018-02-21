package src.messages.hand;

import src.messages.Message;

// Client
public class HandFullClient extends Message{
	public String[] discard;
	public String[] toFaceUp;
	
	public HandFullClient(int player, String[] discard, String[] toFaceUp) {
		super(player);
		this.discard = discard;
		this.toFaceUp = toFaceUp;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDHANDFULL;
	}
}
