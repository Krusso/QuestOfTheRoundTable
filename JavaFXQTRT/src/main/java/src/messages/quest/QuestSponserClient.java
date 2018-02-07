package src.messages.quest;

import src.messages.Message;


//from Client
public class QuestSponserClient extends Message {

	public boolean joined;
	
	public QuestSponserClient(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	@Override
	public void setMessage() {
		this.message = "SPONSER QUEST";
	}

}