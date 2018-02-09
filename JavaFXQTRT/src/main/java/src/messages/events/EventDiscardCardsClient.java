package src.messages.events;

import src.messages.Message;

//Client 
public class EventDiscardCardsClient extends Message{
	public String[] cards;
	
	public EventDiscardCardsClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.EVENTDISCARD;
	}

}
