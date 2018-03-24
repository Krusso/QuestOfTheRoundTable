package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from Client
public class QuestBidClient extends Message {
	public int bid;
	
	public QuestBidClient() {}
	
	public QuestBidClient(int player, int bid) {
		super(player);
		this.bid = bid;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.BIDQUEST;
	}

}
