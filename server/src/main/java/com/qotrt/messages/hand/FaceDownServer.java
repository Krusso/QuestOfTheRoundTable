package com.qotrt.messages.hand;

import com.qotrt.messages.Message;

//from server
public class FaceDownServer extends Message {
	public String card;
	
	public FaceDownServer(int player, String card) {
		super(player);
		this.card = card;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.FACEDOWNCARD;
	}

}
