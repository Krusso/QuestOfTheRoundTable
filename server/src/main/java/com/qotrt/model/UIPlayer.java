package com.qotrt.model;

public class UIPlayer {

	private String sessionID;
	private String playerName;
	private int shieldNumber;
	
	public UIPlayer(String sessionID,String playerName, int shield) {
		this.sessionID = sessionID;
		this.playerName = playerName;
		this.shieldNumber = shield;
	}
	
	public int getShieldNumber(){
		return this.shieldNumber;
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
