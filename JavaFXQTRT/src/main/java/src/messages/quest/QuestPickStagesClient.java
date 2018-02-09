package src.messages.quest;

import src.messages.Message;

//from Client
public class QuestPickStagesClient extends Message {
	public String[] cards;
	public int stage;
	
	public QuestPickStagesClient(int player, String[] cards, int stage) {
		super(player);
		this.cards = cards;
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKSTAGES;
	}

}
