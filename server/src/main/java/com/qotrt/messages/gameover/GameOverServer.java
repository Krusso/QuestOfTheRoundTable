package com.qotrt.messages.gameover;

import com.qotrt.messages.Message;


// from server
public class GameOverServer extends Message {

	public int[] players;
	
	public GameOverServer() {}
	
	public GameOverServer(int[] players) {
		super();
		this.players = players;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.GAMEOVER;
	}

}
