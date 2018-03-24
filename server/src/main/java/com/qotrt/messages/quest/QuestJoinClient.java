package com.qotrt.messages.quest;

import com.qotrt.messages.Message;


//from Client
public class QuestJoinClient extends Message {

	public boolean joined;
	
	public QuestJoinClient() {}
	
	public QuestJoinClient(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINQUEST;
	}

}