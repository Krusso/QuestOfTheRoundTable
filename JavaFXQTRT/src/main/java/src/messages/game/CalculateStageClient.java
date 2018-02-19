package src.messages.game;

import src.messages.Message;

//Client
public class CalculateStageClient extends Message {

	public String[] cards;
	public int stage;
	
	public CalculateStageClient(int player, String[] cards, int stage) {
		super(player);
		this.cards = cards;
		this.stage = stage;
	}
	
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.CALCULATESTAGE;
	}
	
}
