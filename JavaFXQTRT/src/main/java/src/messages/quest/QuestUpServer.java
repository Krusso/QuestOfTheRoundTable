package src.messages.quest;

import src.messages.Message;

//from server
public class QuestUpServer extends Message {
	public String[][] cards;
	
	public QuestUpServer(int player, String[][] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = "UP QUEST";
	}

}
