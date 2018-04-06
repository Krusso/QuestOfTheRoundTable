package com.qotrt.messages.special;

import com.qotrt.messages.Message;
import com.qotrt.model.GenericPair;

// from server
public class MerlinServer extends Message {

	public GenericPair[] cards;
	public int stage;
	public String response;
	
	public MerlinServer() {}
	
	public MerlinServer(int player, int stage, GenericPair[] cards, String response) {
		super(player);
		this.cards = cards;
		this.response = response;
		this.stage = stage;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.MERLIN;
	}

}