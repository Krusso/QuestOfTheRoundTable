package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from Client
public class QuestPickCardsClient extends Message {
	
	public QuestPickCardsClient() {}
	
	public QuestPickCardsClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.PICKQUEST;
	}

}
