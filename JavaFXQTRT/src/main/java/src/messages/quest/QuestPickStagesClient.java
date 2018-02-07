package src.messages.quest;

import src.messages.Message;

//from Client
public class QuestPickStagesClient extends Message {
	public String[] cards;
	
	public QuestPickStagesClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = "PICK STAGES";
	}

}
