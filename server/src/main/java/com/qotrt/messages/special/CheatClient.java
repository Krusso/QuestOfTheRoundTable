package com.qotrt.messages.special;

import com.qotrt.messages.Message;

//from Client
public class CheatClient extends Message {
	public CheatClient() {}
	
	public CheatClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.CHEAT;
	}
	
}
