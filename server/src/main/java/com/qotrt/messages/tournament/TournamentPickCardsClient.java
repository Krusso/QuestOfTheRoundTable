package com.qotrt.messages.tournament;

import com.qotrt.messages.Message;

//from Client
public class TournamentPickCardsClient extends Message {
	public int[] cards;
	
	public TournamentPickCardsClient(int player, int[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKTOURNAMENT;
	}

}
