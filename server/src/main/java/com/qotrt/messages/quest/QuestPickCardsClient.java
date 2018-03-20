package com.qotrt.messages.quest;

import src.messages.Message;

//from Client
public class QuestPickCardsClient extends Message {
	public String[] cards;
	
	public QuestPickCardsClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKQUEST;
	}

}
