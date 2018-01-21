package src.views;

import java.util.ArrayList;

import src.game_logic.AdventureCard;
import src.game_logic.Rank.RANKS;
import src.player.Player;
import src.player.Player.STATE;
import src.socket.OutputController;

public class PlayerView {
	
	private OutputController output;
	
	public PlayerView(OutputController output) {
		this.output = output;
	}
	
	public void update(RANKS rank, int ID) {
		output.sendMessage("rank set: player " + ID + " " + rank);
	}

	public void updateCards(ArrayList<AdventureCard> cards, int ID) {
		output.sendMessage("add cards: player " + ID + " " + cards);
	}

	public void updateState(STATE question, int ID) {
		if(question == Player.STATE.QUESTIONED) {
			output.sendMessage("tournament accept: player " + ID);
		} else if(question == Player.STATE.PICKING) {
			output.sendMessage("tournament picking: player " + ID);
		}
	}

}
