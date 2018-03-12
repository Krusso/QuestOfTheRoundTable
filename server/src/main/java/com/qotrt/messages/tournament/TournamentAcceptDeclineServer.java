package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

public class TournamentAcceptDeclineServer extends Message {

	public TournamentAcceptDeclineServer(int players) {
		super(players);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINTOURNAMENT;
	}

}