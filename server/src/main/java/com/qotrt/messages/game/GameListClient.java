package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//Client
public class GameListClient extends Message {

	@Override
	public void setMessage() {
		this.message = Message.MESSAGETYPES.JOINGAME;	
	}
}
