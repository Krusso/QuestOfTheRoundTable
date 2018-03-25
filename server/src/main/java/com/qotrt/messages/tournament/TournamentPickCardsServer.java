package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

// from server
public class TournamentPickCardsServer extends Message {

	// tell you who joined the tournament
	public int[] players;
	
	public TournamentPickCardsServer() {}
	
	public TournamentPickCardsServer(int player, int[] players) {
		super(player);
		this.players = players;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.PICKTOURNAMENT;
	}

}