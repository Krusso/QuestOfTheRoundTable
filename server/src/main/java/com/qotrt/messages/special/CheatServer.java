package com.qotrt.messages.special;

import com.qotrt.messages.Message;

// from server
public class CheatServer extends Message {

	public int[] cards;
	
	public CheatServer() {}
	
	public CheatServer(int player, int[] cards) {
		super(player);
		this.cards = cards;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.CHEAT;
	}

}