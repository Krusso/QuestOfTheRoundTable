package src.messages.game;

import src.messages.Message;

//from server
public class TurnNextServer extends Message {
	
	public TurnNextServer(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.TURNNEXT;
	}

}
