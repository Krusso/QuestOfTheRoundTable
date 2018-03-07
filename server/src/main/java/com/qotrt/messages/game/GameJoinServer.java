package com.qotrt.messages.game;

import java.util.Arrays;

import com.qotrt.messages.Message;

// Server
public class GameJoinServer extends Message {

	public String[] players;
	
	public GameJoinServer() {
		
	}
	
	public GameJoinServer(String[] players) {
		this.players = players;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.GAMESTART;
	}
	
	@Override
	public String toString() {
		return super.toString() + " players: " + Arrays.toString(players);
	}
}
