package src.messages.tournament;

import src.messages.Message;


// from server
public class TournamentWinServer extends Message {

	public TournamentWinServer(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.WINTOURNAMENT;
	}

}
