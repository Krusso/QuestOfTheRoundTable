package src.messages.tournament;

import src.messages.Message;

//from Client
public class TournamentAcceptDeclineClient extends Message {

	public boolean joined;
	
	public TournamentAcceptDeclineClient(int player, boolean joined) {
		super(player);
		this.joined = joined;
	}
	
	@Override
	public void setMessage() {
		this.message = "JOIN TOURNAMENT";
	}

}