package src.messages.game;

import src.messages.Message;

//Client
public class MordredClient extends Message {

	public String otherAllyCard;
	public int otherPlayer;
	
	public MordredClient(int currentPlayer, int otherPlayer, String otherAllyCard) {
		super(currentPlayer);
		this.otherAllyCard = otherAllyCard;
		this.otherPlayer = otherPlayer;
	}


	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.MORDRED;
	}
	
}
