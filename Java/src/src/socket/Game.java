package src.socket;

import src.game_logic.Player;

public class Game extends Thread{

	private OutputController output;
	private GameModel gm;
	private Player[] players;
	
	public Game(OutputController output, GameModel gm) {
		this.output = output;
		this.gm = gm;
	}

	public void run() {
		players = new Player[gm.getNumPlayers()];
		for(int i = gm.getNumPlayers(); i > 0; i--) {
			players[i - 1] = new Player(); 
		}
		
		
	}

}
