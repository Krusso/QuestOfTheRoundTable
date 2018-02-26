package src.messages.tournament;

import src.messages.Message;


// from server
public class TournamentTieServer extends Message {

	public int[] players;
	
	public TournamentTieServer(int[] is) {
		this.players = is;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.TIETOURNAMENT;
	}

}
