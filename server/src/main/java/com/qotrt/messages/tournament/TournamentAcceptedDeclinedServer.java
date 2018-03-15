package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

public class TournamentAcceptedDeclinedServer extends Message {

	public boolean joined;
	
	public TournamentAcceptedDeclinedServer() {}
	
	public TournamentAcceptedDeclinedServer(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINEDTOURNAMENT;
	}

}