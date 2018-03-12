package com.qotrt.messages.game;

import com.qotrt.messages.Message;


//from server
public class ShieldCountServer extends Message {
	public int shields;
	
	public ShieldCountServer(int player, int shields) {
		super(player);
		this.shields = shields;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.SHIELDCOUNT;
	}

}
