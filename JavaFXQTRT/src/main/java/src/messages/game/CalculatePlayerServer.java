package src.messages.game;

import src.messages.Message;

// Server
public class CalculatePlayerServer extends Message{
	public int points;
	
	public CalculatePlayerServer(int points, int player) {
		super(player);
		this.points = points;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.CALCULATEPLAYER;
	}
}
