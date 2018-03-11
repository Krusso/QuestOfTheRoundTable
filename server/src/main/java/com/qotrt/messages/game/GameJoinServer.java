package com.qotrt.messages.game;

import java.util.Arrays;

import com.qotrt.messages.Message;
import com.qotrt.model.UIPlayer;

// Server
public class GameJoinServer  {

	private String[] players;

	public String[] getPlayers() {
		return players;
	}

	public void setPlayers(String[] players) {
		this.players = players;
	}
	
//	public GameJoinServer(UIPlayer[] UIPlayers) {
//		this.players = Arrays.stream(UIPlayers).map(i -> i.getPlayerName()).toArray(String[]::new);
//	}

//	@Override
//	public void setMessage() {
//		this.message = MESSAGETYPES.GAMESTART;
//	}
	
//	@Override
//	public String toString() {
//		return super.toString() + " players: " + Arrays.toString(players);
//	}
}
