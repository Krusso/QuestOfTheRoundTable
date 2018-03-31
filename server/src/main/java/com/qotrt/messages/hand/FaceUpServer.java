package com.qotrt.messages.hand;

import com.qotrt.messages.Message;

//from server
public class FaceUpServer extends Message {
	
	public FaceUpServer() {}

	public FaceUpServer(int player) {
		super(player);
	}

	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FACEUPCARDS;
	}
}
