package com.qotrt.messages.discard;

import com.qotrt.messages.Message;

//Sever
public class HandFullFinishPickingServer extends Message{

	public boolean successful;
	public String response;
	
	public HandFullFinishPickingServer() {}
	
	public HandFullFinishPickingServer(int player, boolean successful, String response) {
		super(player);
		this.successful = successful;
		this.response = response;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHDISCARD;
	}
}
