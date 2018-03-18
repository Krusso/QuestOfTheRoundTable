package com.qotrt.messages.game;

import com.qotrt.messages.Message;
import com.qotrt.messages.game.PlayCardClient.ZONE;

// from server
public class PlayCardServer extends Message {

	public int card;
	public ZONE zoneTo;
	public ZONE zoneFrom;
	public String response;
	
	public PlayCardServer() {}
	
	public PlayCardServer(int player, int card, ZONE zoneFrom, ZONE zoneTo, String response) {
		super(player);
		this.card = card;
		this.zoneFrom = zoneFrom;
		this.zoneTo = zoneTo;
		this.response = response;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.PLAYCARD;
	}

}