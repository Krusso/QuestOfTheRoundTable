package src.messages.tournament;

import src.messages.Message;

//from Client
public class TournamentPickCardsClient extends Message {
	public String[] cards;
	
	public TournamentPickCardsClient(int player, String[] cards) {
		super(player);
		this.cards = cards;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PICKTOURNAMENT;
	}

}
