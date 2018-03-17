package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

// from server
public class TournamentPickCardsServer extends Message {

	public TournamentPickCardsServer() {}
	
	public TournamentPickCardsServer(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKTOURNAMENT;
	}

}