package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestPickStagesServer extends Message {

	public int numStages;
	
	public QuestPickStagesServer() {}
	
	public QuestPickStagesServer(int player, int numStages) {
		super(player);
		this.numStages = numStages;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKSTAGES;
	}

}