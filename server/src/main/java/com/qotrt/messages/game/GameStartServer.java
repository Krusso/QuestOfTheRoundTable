package com.qotrt.messages.game;

import com.qotrt.messages.Message;

// Server
public class GameStartServer extends Message {

	public String[] players;
	
	public GameStartServer(String[] players) {
		this.players = players;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.GAMESTART;
	}
}
