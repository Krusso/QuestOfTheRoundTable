package com.qotrt.messages.game;

import com.qotrt.messages.Message;
import com.qotrt.model.GenericPair;

// Server
public class GameJoinServer extends Message  {

	private GenericPair[] players;

	public GenericPair[] getPlayers() {
		return players;
	}

	public void setPlayers(GenericPair[] players) {
		this.players = players;
	}

	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.JOINGAME;
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
