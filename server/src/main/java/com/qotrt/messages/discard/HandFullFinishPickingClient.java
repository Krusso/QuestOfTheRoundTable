package com.qotrt.messages.discard;

import com.qotrt.messages.Message;

//from client
public class HandFullFinishPickingClient extends Message {
	
	public HandFullFinishPickingClient() {}
	
	public HandFullFinishPickingClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHDISCARD;
	}

}
