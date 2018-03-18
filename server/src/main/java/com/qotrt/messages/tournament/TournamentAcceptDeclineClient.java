package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

//from Client
public class TournamentAcceptDeclineClient extends Message {

	public boolean joined;
	
	public TournamentAcceptDeclineClient() {}
	
	public TournamentAcceptDeclineClient(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.JOINTOURNAMENT;
	}

}