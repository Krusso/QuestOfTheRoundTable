package com.qotrt.messages.events;

import com.qotrt.messages.Message;

//from client
public class EventDiscardFinishPickingClient extends Message {
	
	public EventDiscardFinishPickingClient() {}
	
	public EventDiscardFinishPickingClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHPICKEVENT;
	}

}
