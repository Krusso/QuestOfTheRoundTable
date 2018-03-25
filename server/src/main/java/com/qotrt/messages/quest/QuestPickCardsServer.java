package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestPickCardsServer extends Message {

	public int[] players;
	
	public QuestPickCardsServer() {}
	
	public QuestPickCardsServer(int player, int[] players) {
		super(player);
		this.players = players;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.PICKQUEST;
	}

}