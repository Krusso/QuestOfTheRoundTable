package com.qotrt.messages.hand;

import com.qotrt.messages.Message;

//from server
public class FaceDownServer extends Message {
	public int ID;

	public FaceDownServer() {}
	
	public FaceDownServer(int player, int ID) {
		super(player);
		this.ID = ID;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.FACEDOWNCARD;
	}

}
