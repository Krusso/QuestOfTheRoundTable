package src.messages.quest;

import src.messages.Message;

// from server
public class QuestPickStagesServer extends Message {

	public QuestPickStagesServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKSTAGES;
	}

}