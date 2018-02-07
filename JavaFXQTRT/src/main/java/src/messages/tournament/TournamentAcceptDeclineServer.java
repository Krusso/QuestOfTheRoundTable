package src.messages.tournament;

import src.messages.Message;

public class TournamentAcceptDeclineServer extends Message {

	public TournamentAcceptDeclineServer(int players) {
		super(players);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = "JOIN TOURNAMENT";
	}

}