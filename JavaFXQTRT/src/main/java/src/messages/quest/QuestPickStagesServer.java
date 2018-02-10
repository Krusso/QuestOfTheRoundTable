package src.messages.quest;

import src.messages.Message;

// from server
public class QuestPickStagesServer extends Message {

	public int numStages;
	
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