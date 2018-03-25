package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from server
public class QuestUpServer extends Message {
	public String[] cards;
	public int stage;
	
	public QuestUpServer() {}
	
	public QuestUpServer(String[] cards, int stage) {
		this.cards = cards;
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.UPQUEST;
	}

}
