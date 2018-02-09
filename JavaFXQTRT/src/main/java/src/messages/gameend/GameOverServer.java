package src.messages.gameend;

import src.game_logic.Rank;
import src.messages.Message;


// from server
public class GameOverServer extends Message {

	public GameOverServer(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.GAMEOVER;
	}

}
