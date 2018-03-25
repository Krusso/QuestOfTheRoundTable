package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;


// from server
public class TournamentTieServer extends Message {

	public int[] players;
	
	public TournamentTieServer(int[] is) {
		this.players = is;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.TIETOURNAMENT;
	}

}
