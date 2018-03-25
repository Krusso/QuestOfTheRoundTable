package com.qotrt.views;

import java.util.UUID;

public class GameDisplay {
	private UUID uuid;
	private int players;
	private String gameName;
	private int capacity;
	private int AICount;
	
	public GameDisplay() {}
	public GameDisplay(UUID uuid, int players, String gameName, int capacity, int i) {
		this.setUuid(uuid);
		this.setPlayers(players);
		this.setCapacity(capacity);
		this.setGameName(gameName);
		this.setAICount(i);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public int getPlayers() {
		return players;
	}
	public void setPlayers(int players) {
		this.players = players;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getAICount() {
		return AICount;
	}
	public void setAICount(int aICount) {
		AICount = aICount;
	}
}
