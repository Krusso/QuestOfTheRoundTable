package com.qotrt.messages.game;

import com.qotrt.messages.Message;

// from server
public class PlayCardServer extends Message {

	public PlayCardServer(int player) {
		super(player);
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKTOURNAMENT;
	}

}