package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

//from client
public class TournamentFinishPickingClient extends Message {
	
	public TournamentFinishPickingClient() {}
	
	public TournamentFinishPickingClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.FINISHPICKTOURNAMENT;
	}

}
