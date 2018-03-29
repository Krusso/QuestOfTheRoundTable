package com.qotrt.messages.events;

import com.qotrt.messages.Message;

//from server
public class EventDiscardOverServer extends Message {
	
	public EventDiscardOverServer() {}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.EVENTDISCARDOVER;
	}

}
