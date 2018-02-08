package src.messages.game;

import src.messages.Message;

//Client
public class GameStartClient extends Message {
	
	public GameStartClient(int players) {
		super(players);
	}
	
	@Override
	public void setMessage() {
		this.message = "START GAME";
	}
	
}
