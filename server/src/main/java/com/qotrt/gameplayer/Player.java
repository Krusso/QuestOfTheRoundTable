package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.Card;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.gameplayer.Rank.RANKS;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.model.GenericPair;
import com.qotrt.model.Observable;
import com.qotrt.model.UIPlayer;


public class Player extends Observable {

	final static Logger logger = LogManager.getLogger(Player.class);

	private UIPlayer uiPlayer;
	protected RANKS rank;
	public AdventureDeck hand;
	protected AdventureDeck faceDown;
	private AdventureDeck faceUp;
	//private PlayerView pv;
	private final int ID;
	protected int shields;
	public boolean tristan = false;
	public boolean iseult = false;

	public Player(int id, UIPlayer uiPlayer) {
		this.rank = Rank.RANKS.SQUIRE;
		this.hand = new AdventureDeck();
		this.faceDown = new AdventureDeck();
		this.faceUp = new AdventureDeck();
		this.ID = id;
		this.shields = 0;
		this.uiPlayer = uiPlayer;
	}

	public AdventureCard findCardByID(int id) {
		return hand.findCardByID(id);
	}

	public AdventureCard getCardByID(int id) {
		return hand.getCardByID(id);
	}
	
	public boolean compareSessionID(String otherSessionID) {
		return uiPlayer.getSessionID().equals(otherSessionID);
	}

	public int getID() {
		return ID;
	}

	public void increaseLevel() {
		if(rank == Rank.RANKS.SQUIRE && shields >= 5) {
			rank = Rank.RANKS.KNIGHT;
			changeShields(-5);
		} else if (rank == Rank.RANKS.KNIGHT && shields >= 7) {
			rank = Rank.RANKS.CHAMPION;
			changeShields(-7);
		} else if (rank == Rank.RANKS.CHAMPION && shields >= 10) {
			rank = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
			changeShields(-10);
		} else {
			return;
		}

		logger.info("Player id: " + ID + " current rank: " + rank + " current shields: " + shields);
		fireEvent("increaseLevel", null, new GenericPair(rank, ID));
		increaseLevel();
	}

	public void addCards(ArrayList<AdventureCard> cards) {
		for(AdventureCard card: cards) {
			logger.info("Player id: " + ID + " adding card " + card.getName());
			hand.addCard(card, 1);
		}

		fireEvent("addCards", null, new GenericPair(cards.stream().
				map(i -> new GenericPair(i.getName(), i.id)).toArray(GenericPair[]::new), 
				ID));
	}

	public String hand() {
		return hand.toString();
	}


	public void changeShields(int shields) {
		this.shields += shields;
		if(this.shields < 0) {
			this.shields = 0;
		}

		logger.info("Changing shields for player: " + ID + " to: " + this.shields);
		fireEvent("changeShields", null, new GenericPair(this.shields, ID));
	}

	public int faceDownDeckLength() {
		return faceDown.size();
	}

	public AdventureDeck getFaceDownDeck() {
		return this.faceDown;
	}


	public void setFaceDown(AdventureCard card, ZONE zoneFrom) {
		logger.info("Player id: " + ID + " setting face down: " + card + " from zone: " + zoneFrom);
		faceDown.addCard(card);
		fireEvent("moveCard", null, new GenericPair(new GenericPair(card.id, zoneFrom),ID));
	}

	public RANKS getRank() {
		return this.rank;
	}

	public void flipCards() {
		logger.info("Face down cards: " + faceDown.getDeck());
		faceUp.addCards(faceDown.drawTopCards(faceDown.size()));
		faceUp.getDeck().forEach(i -> {
			if(i.getName().equals("Sir Tristan")) {
				tristan = true;
			} else if(i.getName().equals("Queen Iseult")) {
				iseult = true;
			}
		});
		if(tristan && iseult) { logger.info("Have both iseult and tristant"); }
		logger.info("Face up deck: " + faceUp.getDeck());
	}

	public final AdventureDeck getFaceUp() {
		return this.faceUp;
	}

	public void discardType(TYPE type) {
		List<Card> removedCards = faceUp.discardType(type);
		removedCards.forEach(i -> {
			if(i.getName().equals("Sir Tristan")) {
				tristan = false;
			} else if(i.getName().equals("Queen Iseult")) {
				iseult = false;
			}
		});
		logger.info("Player id: " + ID + " discarded type: " + type);
		logger.info("Player id: " + ID + " discarded cards: " + Arrays.toString(removedCards.stream().map(i -> i.getName()).toArray(String[]::new)));
		fireEvent("discardType", null, new GenericPair(removedCards, ID));
	}

	public int getShields() {
		return this.shields;
	}

	public int getTypeCount(TYPE type) {
		return hand.typeCount(type);
	}

	protected ArrayList<AdventureCard> removeCards(String[] split) {
		ArrayList<AdventureCard> removed = new ArrayList<AdventureCard>();
		for(String cardName: split) {
			removed.add(hand.getCardByName(cardName));
		}
		logger.info("Player id: " + ID + " removing cards " + Arrays.toString(split));
		return removed;
	}

	public Card getCard(String cardName) {
		return hand.getCardByName(cardName);
	}

	public int getCardCount() {
		return hand.size();
	}
}