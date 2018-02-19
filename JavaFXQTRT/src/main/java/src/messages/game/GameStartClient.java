package src.messages.game;

import src.messages.Message;

//Client
public class GameStartClient extends Message {
	
	public boolean rigged;
	
	public GameStartClient(int players, boolean rigged) {
		super(players);
		this.rigged = rigged;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.STARTGAME;
	}
	
}
