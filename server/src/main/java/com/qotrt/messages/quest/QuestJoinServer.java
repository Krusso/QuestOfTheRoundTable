package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestJoinServer extends Message {

	public int[] players;
	
	public QuestJoinServer() {}
	
	public QuestJoinServer(int player, int[] players) {
		super(player);
		this.players = players;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINQUEST;
	}

}