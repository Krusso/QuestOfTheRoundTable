package src.messages.quest;

import src.messages.Message;


//from Client
public class QuestSponserClient extends Message {

	public boolean sponser;
	
	public QuestSponserClient(int player, boolean joined) {
		super(player);
		this.sponser = joined;
	}
	
	@Override
	public void setMessage() {
		this.message = "SPONSER QUEST";
	}

}