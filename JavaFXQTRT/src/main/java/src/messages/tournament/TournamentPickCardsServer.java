package src.messages.tournament;

import src.messages.Message;

// from server
public class TournamentPickCardsServer extends Message {

	public TournamentPickCardsServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = "PICK TOURNAMENT";
	}

}