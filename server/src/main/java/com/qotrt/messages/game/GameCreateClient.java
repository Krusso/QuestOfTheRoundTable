package com.qotrt.messages.game;

import com.qotrt.messages.Message;
import com.qotrt.model.RiggedModel.RIGGED;

//Client
public class GameCreateClient extends Message {
	
	private int numPlayers;
	private String playerName;
	private RIGGED rigged;
	
	
	public GameCreateClient() {}
	
	public GameCreateClient(int numPlayers, String playerName, RIGGED rigged) {
		this.numPlayers = numPlayers;
		this.playerName = playerName;
		this.rigged = rigged;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void setMessage() {
		this.messageType = Message.MESSAGETYPES.JOINGAME;	
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public RIGGED getRigged() {
		return rigged;
	}

	public void setRigged(RIGGED rigged) {
		this.rigged = rigged;
	}
}
