package src.messages.game;

import src.messages.Message;

//Client
public class CalculatePlayerClient extends Message {

	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.CONTINUEGAME;
	}
	
}
