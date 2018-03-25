package com.qotrt.messages.quest;

import src.messages.Message;

// from server
public class QuestBidServer extends Message {

	public int maxBidValue;
	public int minBidValue;
	
	public QuestBidServer(int player, int maxBidValue, int i) {
		super(player);
		this.maxBidValue = maxBidValue;
		this.minBidValue = i;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.BIDQUEST;
	}

}