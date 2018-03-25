package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestJoinServer extends Message {

	public QuestJoinServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINQUEST;
	}

}