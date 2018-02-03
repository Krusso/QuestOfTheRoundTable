package src.client;

import java.util.ArrayList;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureDeck;
import src.player.Player;

public class UIPlayer extends Player{

	public UIPlayer(int id) {
		super(id);
	}
	
	public ArrayList<AdventureCard> getPlayerHandAsList() {
		return hand.getDeck();
	}
	public void addCard(AdventureCard c) {
		hand.addCard(c, 1);
	}
	public void removeCard(int pos) {
		hand.getDeck().remove(pos);
	}
	
	public int getHandSize() {
		return hand.getDeck().size();
	}

}
