package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

// from server
public class TournamentPickCardsServer extends Message {

	public TournamentPickCardsServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKTOURNAMENT;
	}

}