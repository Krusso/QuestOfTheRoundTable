package src.messages.game;

import src.messages.Message;

// Server
public class ContinueGameServer extends Message {
	
	public String messageText;
	
	public ContinueGameServer(String message) {
		super();
		this.messageText = message;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.CONTINUEGAME;
	}
}
