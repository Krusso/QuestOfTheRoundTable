package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//Client
public class GameListClient extends Message {

	@Override
	public void setMessage() {
		this.messageType = Message.MESSAGETYPES.JOINGAME;	
	}
}
