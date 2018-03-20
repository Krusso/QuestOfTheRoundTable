package com.qotrt.messages.quest;

import src.messages.Message;

//from server
public class QuestDownServer extends Message {
	public String[][] cards;
	
	public QuestDownServer(int player, String[][] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DOWNQUEST;
	}

}
