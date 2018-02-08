package src.messages.quest;

import src.messages.Message;

//from Client
public class QuestBidClient extends Message {
	public int bid;
	
	public QuestBidClient(int player, int bid) {
		super(player);
		this.bid = bid;
	}
	
	@Override
	public void setMessage() {
		this.message = "BID QUEST";
	}

}
