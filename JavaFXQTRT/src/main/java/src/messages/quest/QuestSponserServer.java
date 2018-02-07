package src.messages.quest;

import src.messages.Message;

// from server
public class QuestSponserServer extends Message {

	public QuestSponserServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = "SPONSER QUEST";
	}

}