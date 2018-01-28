package src.views;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;
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
		} else if(question == Player.STATE.WIN) {
			output.sendMessage("tournament won: player " + ID);
		}
	}

	public void updateFaceDown(List<AdventureCard> list, int ID) {
		output.sendMessage("face down: player " + ID + " " + list);
	}

	public void updateFaceUp(AdventureDeck faceUp, int ID) {
		output.sendMessage("face up: player " + ID + " " + faceUp);
	}

	public void updateState(STATE question, int ID, int i, TYPE type) {
		output.sendMessage("discard hand: player " + ID + " type " + type + " amount " + i);
	}

	public void updateMiddle(StoryCard card) {
		output.sendMessage("middle card: " + card.getName());
	}

}
