package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//from Client
public class PlayCardClient extends Message {
	public int card;
	public ZONE zone;
	
	public PlayCardClient() {}
	
	public PlayCardClient(int player, int card, ZONE zone) {
		super(player);
		this.card = card;
		this.zone = zone;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PLAYCARD;
	}
	
	public static enum ZONE {
		FACEDOWN, 
		FACEUP,
		DISCARD, 
		STAGE1, 
		STAGE2, 
		STAGE3, 
		STAGE4, 
		STAGE5, 
		HAND
	}

}
