package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

public class TournamentAcceptDeclineServer extends Message {

	public int[] players;
	
	public TournamentAcceptDeclineServer() {}
	
	public TournamentAcceptDeclineServer(int player, int[] players) {
		super(player);
		this.players = players;
	}

	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.JOINTOURNAMENT;
	}

}