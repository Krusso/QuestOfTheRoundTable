package src.messages.game;

import src.messages.Message;

// Server
public class CalculateStageServer extends Message{
	public int points;
	public int stage;
	
	public CalculateStageServer(int points, int player, int stage) {
		super(player);
		this.points = points;
		this.stage = stage;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.CALCULATESTAGE;
	}
}
