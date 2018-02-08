package src.messages.quest;

import src.messages.Message;


//from Client
public class QuestJoinClient extends Message {

	public boolean joined;
	
	public QuestJoinClient(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	@Override
	public void setMessage() {
		this.message = "JOIN QUEST";
	}

}