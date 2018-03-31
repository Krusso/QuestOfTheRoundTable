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
	private Boolean discard;
	private Boolean racing;
	
	public GameCreateClient() {}
	
	public GameCreateClient(int numPlayers, String playerName, String gameName, RIGGED rigged, AIPlayer[] ais) {
		this.numPlayers = numPlayers;
		this.playerName = playerName;
		this.gameName = gameName;
		this.rigged = rigged;
		this.ais = ais;
	}

	public GameCreateClient(int numPlayers, String playerName, RIGGED rigged, AIPlayer[] ais, boolean discard) {
		this.numPlayers = numPlayers;
		this.playerName = playerName;
		this.rigged = rigged;
		this.ais = ais;
		this.discard = discard;
	}
	
	public GameCreateClient(int numPlayers, String playerName, RIGGED rigged, AIPlayer[] ais, boolean discard, boolean racing) {
		this.numPlayers = numPlayers;
		this.playerName = playerName;
		this.rigged = rigged;
		this.ais = ais;
		this.discard = discard;
		this.racing = racing;
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

	public Boolean getDiscard() {
		return this.discard;
	}

	public void setDiscard(Boolean discard) {
		this.discard = discard;
	}

	public Boolean getRacing() {
		return racing;
	}

	public void setRacing(Boolean racing) {
		this.racing = racing;
	}
}
