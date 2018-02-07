package src.messages.gameend;

import src.messages.Message;

public class FinalTournamentNotifyServer extends Message {

	public FinalTournamentNotifyServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = "JOINED FINAL TOURNAMENT";
	}

}