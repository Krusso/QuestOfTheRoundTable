package com.qotrt.messages.gameover;

import com.qotrt.messages.Message;

//from client
public class FinalTournamentFinishPickingClient extends Message {
	
	public FinalTournamentFinishPickingClient() {}
	
	public FinalTournamentFinishPickingClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHFINALTOURNAMENT;
	}

}
