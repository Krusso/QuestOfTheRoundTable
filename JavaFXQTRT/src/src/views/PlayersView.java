package src.views;

import src.socket.OutputController;

public class PlayersView {
	
	private OutputController output;
	
	public PlayersView(OutputController output) {
		this.output = output;
	}
	
	public void update(int playerTurn, String hand) {
		output.sendMessage("turn next:" + playerTurn);
		output.sendMessage("hand show:" + hand);
	}

}
