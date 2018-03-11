package com.qotrt.model;

public class UIPlayer {

	private String sessionID;
	private String playerName;
	
	public UIPlayer(String sessionID,String playerName) {
		this.sessionID = sessionID;
		this.playerName = playerName;
	}
	
	public String getSessionID() {
		return this.sessionID;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	@Override
	public String toString() {
		return "UIPlayer:[ sessionID: " + this.sessionID + ", playerName: " + playerName + "]";
	}
}
