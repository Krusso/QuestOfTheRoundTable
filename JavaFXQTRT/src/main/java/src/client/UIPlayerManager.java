package src.client;

import java.util.ArrayList;

import src.game_logic.AdventureCard;

public class UIPlayerManager {
	
	private UIPlayer[] players;
	private int currentPlayer;
	public UIPlayerManager(int numPlayers) {
		players = new UIPlayer[numPlayers];
		for(int i = 0 ; i < numPlayers; i++) {
			players[i] = new UIPlayer(i);
		}
	}
	
	public void addCardToHand(AdventureCard c, int playerNum) {
		players[playerNum].addCard(c);
	}
	
	public void showPlayerHand(int playerNum) {
		ArrayList<AdventureCard> p = getPlayerHand(playerNum);
		p.forEach(card-> {
//			System.out.println("Show card: " + card.getName());
			card.show();
		});
	}
	public void hidePlayerHand(int playerNum) {
		ArrayList<AdventureCard> p = getPlayerHand(playerNum);
		p.forEach(card-> {
			card.hide();
		});
	}
	public void setCurrentPlayer(int p) {
		currentPlayer = p;
		for(int i = 0; i < players.length ; i++) {
			if(i == p) {
				showPlayerHand(p);
			}else {
				hidePlayerHand(i);
			}
		}
	}
	
	public ArrayList<AdventureCard> getPlayerHand(int pNum){
		return players[pNum].getPlayerHandAsList();
	}
	
}
