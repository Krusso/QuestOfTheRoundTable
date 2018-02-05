package src.client;

import java.util.ArrayList;

import src.game_logic.AdventureCard;
import src.game_logic.Rank;
import src.player.UIPlayer;

public class UIPlayerManager {
	
	private UIPlayer[] players;
	private int currentPlayer;
	public UIPlayerManager(int numPlayers) {
		players = new UIPlayer[numPlayers];
		for(int i = 0 ; i < numPlayers; i++) {
			players[i] = new UIPlayer(i);
		}
	}
	
//	public void setPlayerRank(int playerNum, Rank.RANKS r) {
//		players[playerNum].
//	}

	public Rank.RANKS getPlayerRank(int playerNum){
		return players[playerNum].getRank();
	}
	public void playCard(AdventureCard card, int currentPlayer2) {
		players[currentPlayer2].playCard(card);
	}
	
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean removeCardFromHand(AdventureCard c, int playerNum) {
		return players[playerNum].removeCard(c);
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
	public void faceDownPlayerHand(int playerNum) {
		ArrayList<AdventureCard> p = getPlayerHand(playerNum);
		p.forEach(card-> {
			card.faceDown();
		});
	}
	public void setCurrentPlayer(int p) {
		currentPlayer = p;
		for(int i = 0; i < players.length ; i++) {
			if(i == p) {
				showPlayerHand(p);
			}else {
//				hidePlayerHand(i);
			}
		}
	}
	
	public ArrayList<AdventureCard> getPlayerHand(int pNum){
		return players[pNum].getPlayerHandAsList();
	}
	public int getFaceDownLength(int playerNumber) {
		return players[playerNumber].faceDownDeckLength();
	}

	public String getFaceDownCards(int currentPlayer2) {
		return players[currentPlayer2].getFaceDown();
	}
	
	public int getNumPlayers() {
		return players.length;
	}
	
}
