package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from Client
public class QuestDiscardCardsClient extends Message {

	public QuestDiscardCardsClient() {}
	
	public QuestDiscardCardsClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDQUEST;
	}

}
