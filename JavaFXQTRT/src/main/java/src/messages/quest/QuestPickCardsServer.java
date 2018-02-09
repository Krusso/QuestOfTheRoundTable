package src.messages.quest;

import src.messages.Message;

// from server
public class QuestPickCardsServer extends Message {

	public QuestPickCardsServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKQUEST;
	}

}