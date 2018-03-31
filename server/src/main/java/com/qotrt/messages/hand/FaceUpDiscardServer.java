package com.qotrt.messages.hand;

import com.qotrt.messages.Message;
import com.qotrt.model.GenericPair;

// from server
public class FaceUpDiscardServer extends Message {
	
	public GenericPair[] cards;
	
	public FaceUpDiscardServer() {};
	
	public FaceUpDiscardServer(int player, GenericPair[] cards) {
		super(player);
		this.cards = cards;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.DISCARDFACEUP;
	}

}