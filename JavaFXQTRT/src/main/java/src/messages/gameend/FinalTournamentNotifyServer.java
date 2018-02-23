package src.messages.gameend;

import src.messages.Message;

public class FinalTournamentNotifyServer extends Message {

	public int[] players;
	
	public FinalTournamentNotifyServer(int[] players) {
		super();
		this.players = players;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.JOINEDFINALTOURNAMENT;
	}

}