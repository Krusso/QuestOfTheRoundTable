package com.qotrt.messages.discard;

import com.qotrt.messages.Message;

// Server
public class HandFullServer extends Message{
	
	public int toDiscard;
	
	public HandFullServer() {}
	
	public HandFullServer(int player, int toDiscard) {
		super(player);
		this.toDiscard = toDiscard;
	}

	@Override
	public void setMessage() {
		this.messageType = com.qotrt.messages.Message.MESSAGETYPES.HANDDISCARD;
	}

}
