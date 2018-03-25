package com.qotrt.messages.hand;

import com.qotrt.messages.Message;
import com.qotrt.messages.game.PlayCardClient.ZONE;

//from server
public class FaceDownServer extends Message {
	public int ID;
	public ZONE zone;

	public FaceDownServer() {}
	
	public FaceDownServer(int player, int ID, ZONE zoneFrom, ZONE zone) {
		super(player);
		this.ID = ID;
		this.zone = zone;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FACEDOWNCARD;
	}

}
