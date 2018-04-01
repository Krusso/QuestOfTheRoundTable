package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from client
public class BidDiscardFinishPickingClient extends Message {
	
	public BidDiscardFinishPickingClient() {}
	
	public BidDiscardFinishPickingClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHBIDDISCARD;
	}

}
