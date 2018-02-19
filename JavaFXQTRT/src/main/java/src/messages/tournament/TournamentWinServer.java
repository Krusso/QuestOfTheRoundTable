package src.messages.tournament;

import src.messages.Message;


// from server
public class TournamentWinServer extends Message {

	public int[] players;
	
	public TournamentWinServer(int[] is) {
		this.players = is;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.WINTOURNAMENT;
	}

}
