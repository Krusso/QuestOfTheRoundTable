package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;


// from server
public class TournamentWinServer extends Message {

	public int[] players;
	public String response;
	
	public TournamentWinServer() {}
	
	public TournamentWinServer(int[] is, String response) {
		this.players = is;
		this.response = response;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.WINTOURNAMENT;
	}

}
