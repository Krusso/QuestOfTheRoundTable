package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//Client
public class GameJoinClient extends Message {
	
	private String playerName;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void setMessage() {
		this.message = Message.MESSAGETYPES.JOINGAME;	
	}
}
