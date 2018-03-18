package com.qotrt.messages.game;

import com.qotrt.messages.Message;

// Server
public class GameStartServer extends Message {

	public GameStartServer() {
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.GAMESTART;
	}
}
