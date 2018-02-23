package src.messages.gameend;

import src.messages.Message;


// from server
public class GameOverServer extends Message {

	public int[] players;
	
	public GameOverServer(int[] players) {
		super();
		this.players = players;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.GAMEOVER;
	}

}
