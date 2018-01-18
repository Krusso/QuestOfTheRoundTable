package src.socket;

import src.game_logic.Card;
import src.game_logic.DeckManager;
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
		DeckManager dm = new DeckManager();
		for(int i = gm.getNumPlayers(); i > 0; i--) {
			players[i - 1] = new Player();
			players[i - 1].addCards(dm.getAdventureCard(12));
		}
		
		output.sendMessage("hand add:all players 12");
		output.sendMessage("rank set:all knight");
		
		int playerTurn = -1;
		while(true) {
			playerTurn++;
			if(playerTurn == gm.getNumPlayers()) playerTurn = 0;
			output.sendMessage("turn next:" + playerTurn);
			output.sendMessage("hand show:" + players[playerTurn].hand());
			
			Card card = dm.getStoryCard(1)[0];
			
			
		}
		
	}

}
