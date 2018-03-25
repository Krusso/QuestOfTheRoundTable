package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestSponsorServer extends Message {

	public int[] players;
	
	public QuestSponsorServer(int player, int[] players) {
		super(player);
		this.players = players;
	}
	
	public QuestSponsorServer() {}

	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.SPONSERQUEST;
	}

}