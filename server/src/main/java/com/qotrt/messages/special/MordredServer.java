package com.qotrt.messages.special;

import com.qotrt.messages.Message;

// from server
public class MordredServer extends Message {

	public int mordred;
	public int opponent;
	public int otherPlayer;
	public String response;
	
	public MordredServer() {}
	
	public MordredServer(int player, int otherPlayer, int mordred, int opponent, String response) {
		super(player);
		this.otherPlayer = otherPlayer;
		this.mordred = mordred;
		this.opponent = opponent;
		this.response = response;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.MORDRED;
	}

}