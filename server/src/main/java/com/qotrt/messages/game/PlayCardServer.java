package com.qotrt.messages.game;

import com.qotrt.messages.Message;
import com.qotrt.messages.game.PlayCardClient.ZONE;

// from server
public class PlayCardServer extends Message {

	public int card;
	public ZONE zone;
	public String response;
	
	public PlayCardServer() {}
	
	public PlayCardServer(int player, int card, ZONE zone, String response) {
		super(player);
		this.card = card;
		this.zone = zone;
		this.response = response;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PLAYCARD;
	}

}