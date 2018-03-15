package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

public class TournamentAcceptDeclineServer extends Message {

	public TournamentAcceptDeclineServer() {}
	
	public TournamentAcceptDeclineServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINTOURNAMENT;
	}

}