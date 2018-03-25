package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

// from server
public class QuestBidServer extends Message {

	public int maxBidValue;
	public int minBidValue;
	public int playerWithHighestBid;
	
	public QuestBidServer() {}
	
	public QuestBidServer(int player, int maxBidValue, int minBidValue, int playerWithHighestBid) {
		super(player);
		this.maxBidValue = maxBidValue;
		this.minBidValue = minBidValue;
		this.playerWithHighestBid = playerWithHighestBid;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.BIDQUEST;
	}

}