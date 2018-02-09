package src.messages.quest;

import src.messages.Message;

//from Client
public class QuestDiscardCardsClient extends Message {
	public String[] cards;
	
	public QuestDiscardCardsClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDQUEST;
	}

}
