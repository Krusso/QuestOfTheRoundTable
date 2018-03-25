package com.qotrt.messages.game;

import com.qotrt.messages.Message;
import com.qotrt.model.RiggedModel.RIGGED;

//Client
public class GameCreateClient extends Message {
	
	private int numPlayers;
	private String playerName;
	private String gameName;
	private RIGGED rigged;
	private AIPlayer[] ais;
	
	public GameCreateClient() {}

	public GameCreateClient(int numPlayers, String playerName, RIGGED rigged) {
		this.numPlayers = numPlayers;
		this.playerName = playerName;
		this.rigged = rigged;
	}
	
	public GameCreateClient(int numPlayers, String playerName, String gameName, RIGGED rigged) {
		this.numPlayers = numPlayers;
		this.playerName = playerName;
		this.gameName = gameName;
		this.rigged = rigged;
	}

	@Override
	public void setMessage() {
		this.messageType = Message.MESSAGETYPES.JOINGAME;	
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
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

	public AIPlayer[] getAis() {
		return ais;
	}
}
