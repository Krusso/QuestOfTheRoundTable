package com.qotrt.messages.special;

import com.qotrt.messages.Message;

//from Client
public class MerlinClient extends Message {
	public int merlin;
	public int stage;
	
	public MerlinClient() {}
	
	public MerlinClient(int player, int merlin, int stage) {
		super(player);
		this.merlin = merlin;
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.MERLIN;
	}
	
}
