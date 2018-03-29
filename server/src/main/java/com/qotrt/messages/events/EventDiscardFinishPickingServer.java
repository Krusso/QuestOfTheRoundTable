package com.qotrt.messages.events;

import com.qotrt.messages.Message;

//Sever
public class EventDiscardFinishPickingServer extends Message{

	public boolean successful;
	public String response;
	
	public EventDiscardFinishPickingServer() {}
	
	public EventDiscardFinishPickingServer(int player, boolean successful, String response) {
		super(player);
		this.successful = successful;
		this.response = response;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHPICKEVENT;
	}
}
