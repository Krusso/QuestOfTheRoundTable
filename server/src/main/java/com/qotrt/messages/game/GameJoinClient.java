package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//Client
public class GameJoinClient extends Message {
	
	public String playerName;
	
	public GameJoinClient() {
		
	}
	
	public GameJoinClient(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINGAME;
	}
}
