package com.qotrt.messages.game;

import com.qotrt.messages.Message;

//from server
public class MiddleCardServer extends Message {
	public String card;
	
	public MiddleCardServer() {}
	
	public MiddleCardServer(String card) {
		super();
		this.card = card;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.SHOWMIDDLECARD;
	}

}
