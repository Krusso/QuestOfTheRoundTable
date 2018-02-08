package src.messages.game;

import src.messages.Message;

//Client
public class ContinueGameClient extends Message {

	@Override
	public void setMessage() {
		this.message = "CONTINUE GAME";
	}
	
}
