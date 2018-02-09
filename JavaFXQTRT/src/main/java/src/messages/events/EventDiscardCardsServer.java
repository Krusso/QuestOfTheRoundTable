package src.messages.events;

import src.game_logic.AdventureCard.TYPE;
import src.messages.Message;

//from server
public class EventDiscardCardsServer extends Message {
	
	int amount;
	TYPE type;
	
	public EventDiscardCardsServer(int player, int amount, TYPE type) {
		super(player);
		this.amount = amount;
		this.type = type;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.EVENTDISCARD;
	}

}
