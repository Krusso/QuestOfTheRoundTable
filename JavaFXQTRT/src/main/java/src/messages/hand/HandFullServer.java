package src.messages.hand;

import src.messages.Message;
import src.messages.Message.MESSAGETYPES;

// Server
public class HandFullServer extends Message{
	
	public HandFullServer(int player) {
		super(player);
	}

	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDHANDFULL;
	}

}
