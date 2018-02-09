package src.messages.quest;

import src.messages.Message;


//from Client
public class QuestSponsorClient extends Message {

	public boolean sponser;
	
	public QuestSponsorClient(int player, boolean joined) {
		super(player);
		this.sponser = joined;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.SPONSERQUEST;
	}

}