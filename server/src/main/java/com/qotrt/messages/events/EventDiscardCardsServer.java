package com.qotrt.messages.events;

import com.qotrt.messages.Message;

//from server
public class EventDiscardCardsServer extends Message {
	
	public int[] players;
	public String instructions;
	
	public EventDiscardCardsServer() {}
	
	public EventDiscardCardsServer(int player, String instructions, int[] players) {
		super(player);
		this.instructions = instructions;
		this.players = players;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.EVENTDISCARD;
	}

}
