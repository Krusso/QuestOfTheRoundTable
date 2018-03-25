package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;


// from server
public class TournamentWinServer extends Message {

	public static enum WINTYPES {
		NOJOIN, ONEJOIN, TIE, WON	
	};
	
	public int[] players;
	public WINTYPES type;
	
	public TournamentWinServer() {}
	
	public TournamentWinServer(int[] is, WINTYPES type) {
		this.players = is;
		this.type = type;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.WINTOURNAMENT;
	}

}
