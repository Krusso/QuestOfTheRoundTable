package com.qotrt.messages.game;

import java.util.UUID;

import com.qotrt.messages.Message;

//Client
public class GameJoinClient extends Message {
	
	private UUID uuid;
	private String playerName;
	private String gameName;

	
	public GameJoinClient() {}
	
	public GameJoinClient(UUID uuid, String playerName) {
		this.uuid = uuid;
		this.playerName = playerName;
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

	@Override
	public void setMessage() {
		this.messageType = Message.MESSAGETYPES.JOINGAME;	
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}