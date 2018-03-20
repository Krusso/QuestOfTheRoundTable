package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestSponsorServer extends Message {

	public QuestSponsorServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.SPONSERQUEST;
	}

}