package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestSponsoredServer extends Message {

	public boolean sponsored;
	
	public QuestSponsoredServer(int player, boolean sponsored) {
		super(player);
		this.sponsored = sponsored;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.SPONSERQUEST;
	}

}