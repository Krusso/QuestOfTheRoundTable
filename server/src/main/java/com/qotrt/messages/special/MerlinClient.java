package com.qotrt.messages.special;

import com.qotrt.messages.Message;

//from Client
public class MerlinClient extends Message {
	public int stage;
	
	public MerlinClient() {}
	
	public MerlinClient(int player, int stage) {
		super(player);
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.MERLIN;
	}
	
}
