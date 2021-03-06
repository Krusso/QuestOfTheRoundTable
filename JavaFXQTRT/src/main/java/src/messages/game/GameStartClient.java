package src.messages.game;

import src.messages.Message;

//Client
public class GameStartClient extends Message {
	
	public static enum RIGGED {
		ONE, TWO, THREE, FOUR, NORMAL, LONG, AITOURNAMENT, AIQUEST, AIQUEST1, AIQUEST2, GAMEEND;
	}
	
	public RIGGED rigged;
	
	public GameStartClient(int players, RIGGED rigged) {
		super(players);
		this.rigged = rigged;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.STARTGAME;
	}
	
}
