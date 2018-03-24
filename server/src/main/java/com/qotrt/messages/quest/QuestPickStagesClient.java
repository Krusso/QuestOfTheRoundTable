package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from Client
public class QuestPickStagesClient extends Message {
	
	public QuestPickStagesClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKSTAGES;
	}

}
