package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//from Client
public class PlayCardClient extends Message {
	public int card;
	public ZONE zoneFrom;
	public ZONE zoneTo;
	
	public PlayCardClient() {}
	
	public PlayCardClient(int player, int card, ZONE zoneFrom, ZONE zoneTo) {
		super(player);
		this.card = card;
		this.zoneFrom = zoneFrom;
		this.zoneTo = zoneTo;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.PLAYCARD;
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
