package src.messages.quest;

import src.messages.Message;

// from server
public class QuestBidServer extends Message {

	public QuestBidServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.BIDQUEST;
	}

}