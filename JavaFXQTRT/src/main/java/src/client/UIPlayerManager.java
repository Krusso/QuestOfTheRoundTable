package src.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.AdventureCard;
import src.game_logic.Rank;
import src.player.A1;
import src.player.A2;
import src.player.AbstractAI;
import src.player.UIPlayer;
import src.socket.Game;

public class UIPlayerManager {

	final static Logger logger = LogManager.getLogger(UIPlayerManager.class);
	
	private ArrayList<AbstractAI> ais = new ArrayList<AbstractAI>();
	public final int MAX_HAND_SIZE = 12;
	public UIPlayer[] players;
	private int currentPlayer;
	public UIPlayerManager(int numPlayers) {
		players = new UIPlayer[numPlayers];
		for(int i = 0 ; i < numPlayers; i++) {
			players[i] = new UIPlayer(i);
		}
	}

	public void setShields(int p, int shields) {
		players[p].addShields(shields);
	}
	public int getShields(int p) {
		return players[p].getShields();
	}
	public Rank.RANKS getPlayerRank(int playerNum){
		return players[playerNum].getRank();
	}
	public void setPlayerRank(int p, Rank.RANKS r){
		players[p].setPlayerRank(r);

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

	public void flipFaceDownCards(int playerNum, boolean isShow) {
		ArrayList<AdventureCard> p = getFaceDownCardsAsList(playerNum);
		p.forEach(card-> {
			if(isShow) {
				card.flipUp().play();
			}else {
				card.flipDown().play();
			}
		});
		logger.info("Flipped player"+p+"'s face down cards to: " + isShow);
	}

	public void flipFaceUpCards(int playerNum) {
		logger.info("Flipping cards");
		ArrayList<AdventureCard> p = getFaceUpCardsAsList(playerNum);
		p.forEach(card-> {
			card.flipUp().play();
		});
	}

	public void setCurrentPlayer(int p) {
		currentPlayer = p;
		//		for(int i = 0; i < players.length ; i++) {
		//			if(i == p) {
		//				showPlayerHand(p);
		//			}else {
		////				hidePlayerHand(i);
		//			}
		//		}
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

	public ArrayList<AdventureCard> getFaceDownCardsAsList(int playerNum){
		return players[playerNum].getFaceDownDeck().getDeck();
	}
	
	public ArrayList<AdventureCard> getFaceUpCardsAsList(int playerNum){
		return players[playerNum].getFaceUp().getDeck();
	}
	
	public String[] getFaceDownCardNames(int playerNum){
		ArrayList<AdventureCard> faceDownCards = players[playerNum].getFaceDownDeck().getDeck();
		String[] cardNames = new String[faceDownCards.size()];
		for(int i = 0 ; i <faceDownCards.size(); i++) {
			cardNames[i] = faceDownCards.get(i).getName();
		}
		return cardNames;
	}

	public int getNumPlayers() {
		return players.length;
	}

	public void removeCardFromPlayerHandByID(int p, int id) {
		ArrayList<AdventureCard> hand = players[p].getPlayerHandAsList();
		for(int i = 0 ; i < hand.size(); i++) {
			if(hand.get(i).id == id) {
				hand.remove(i);
				return;
			}
		}
	}
	public int getCardIndexByID(int p, int id) {
		ArrayList<AdventureCard> hand = players[p].getPlayerHandAsList();
		for(int i = 0 ; i < hand.size(); i++) {
			if(hand.get(i).id == id) {
				return i;
			}
		}

		return -1;
	}
	public AdventureCard getCardByIDInHand(int p, int id) {
		//check the hand
		ArrayList<AdventureCard> hand = players[p].getPlayerHandAsList();
		for(int i = 0 ; i < hand.size(); i++) {
			if(hand.get(i).id == id) {
				return hand.get(i);
			}
		}

		return null;
	}
	public AdventureCard getCardByIDInFaceDown(int p, int id) {
		//check the facedown
		ArrayList<AdventureCard> faceDown = players[p].getFaceDownDeck().getDeck();
		for(int i = 0 ; i < faceDown.size(); i++) {
			if(faceDown.get(i).id == id) {
				return faceDown.get(i);
			}
		}
		return null;
	}
	
	public boolean isHandFull(int p) {
		if(players[p].getPlayerHandAsList().size() > MAX_HAND_SIZE) {
			return true;
		}
		return false;
	}
	
	public boolean iseultExists() {
		for(UIPlayer p: players) {
			for(AdventureCard c : p.getFaceUp().getDeck()) {
				if(c.getName().equals("Queen Iseult")){
					return true;
				}
			}
		}
		return false;
	}
	public void rememberStage(int pNum, int stage) {
		players[pNum].viewableStage = stage;
	}
	public int viewableStage(int pNum) {
		return players[pNum].viewableStage;
	}
	public void resetMerlinViewableStage() {
		for(UIPlayer p : players) {
			p.viewableStage = -1;
		}
	}

	public void setAI(List<Integer> list, List<Integer> list2) {
		logger.info("AI player #s: " + list + " " + list2);
		list.forEach(i -> ais.add(new A1(players[i], this)));
		list2.forEach(i -> ais.add(new A2(players[i], this)));
	}
	
	public AbstractAI getAI(int p) {
		logger.info("Checking if player is an AI#: " + p);
		for(AbstractAI a: ais) {
			if(a.player == players[p]) {
				logger.info("Player # is an AI: " + p);
				return a;
			}
		}
		logger.info("Player # is not an AI: " + p);
		return null;
	}
		
}
