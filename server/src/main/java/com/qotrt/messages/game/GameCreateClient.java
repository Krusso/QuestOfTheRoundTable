package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//Client
public class GameCreateClient extends Message {
	
	private int numPlayers;
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

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
}
