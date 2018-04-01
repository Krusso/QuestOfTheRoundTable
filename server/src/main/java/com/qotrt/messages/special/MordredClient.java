package com.qotrt.messages.special;

import com.qotrt.messages.Message;

//from Client
public class MordredClient extends Message {
	
	public int mordred;
	public int opponent;
	
	public MordredClient() {}
	
	public MordredClient(int player, int mordred, int opponent) {
		super(player);
		this.mordred = mordred;
		this.opponent = opponent;
	}

	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.MORDRED;
	}
	
}
