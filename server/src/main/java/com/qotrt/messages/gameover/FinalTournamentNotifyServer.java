package com.qotrt.messages.gameover;

import com.qotrt.messages.Message;

public class FinalTournamentNotifyServer extends Message {

	public int[] players;
	
	public FinalTournamentNotifyServer() {}
	
	public FinalTournamentNotifyServer(int[] players) {
		super();
		this.players = players;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.JOINEDFINALTOURNAMENT;
	}

}