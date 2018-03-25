package com.qotrt.messages.quest;

import src.messages.Message;

// from server
public class QuestDiscardCardsServer extends Message {
	public int cardsToDiscard;
	
	public QuestDiscardCardsServer(int player, int cardsToBid) {
		super(player);
		this.cardsToDiscard = cardsToBid;
	}
	
	//from server
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.DISCARDQUEST;
	}

}