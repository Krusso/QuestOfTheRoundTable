package src.messages.quest;

import src.messages.Message;

// from server
public class QuestDiscardCardsServer extends Message {
	
	public QuestDiscardCardsServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = "DISCARD QUEST";
	}

}