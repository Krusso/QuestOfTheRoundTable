package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

public class QuestJoinedServer extends Message {

	public boolean joined;
	
	public QuestJoinedServer() {}
	
	public QuestJoinedServer(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.JOINEDQUEST;
	}

}