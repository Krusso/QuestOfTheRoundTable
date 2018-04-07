package com.qotrt.messages.special;

import com.qotrt.messages.Message;

//from Client
public class MerlinClient extends Message {
	public int id;
	public int stage;
	
	public MerlinClient() {}
	
	public MerlinClient(int player, int id, int stage) {
		super(player);
		this.id = id;
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.MERLIN;
	}
	
}
