package src.messages.quest;

import src.messages.Message;

// from server
public class QuestSponsorServerCant extends Message {

	public QuestSponsorServerCant(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.SPONSERQUESTCANT;
	}

}