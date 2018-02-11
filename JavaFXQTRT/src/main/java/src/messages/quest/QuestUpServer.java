package src.messages.quest;

import src.messages.Message;

//from server
public class QuestUpServer extends Message {
	public String[] cards;
	public int stage;
	
	public QuestUpServer(int player, String[] cards, int stage) {
		super(player);
		this.cards = cards;
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.UPQUEST;
	}

}
