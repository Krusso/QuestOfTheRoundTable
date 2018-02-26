package src.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.AdventureCard;
import src.game_logic.Rank;

public class UIPlayer extends Player{

	public int viewableStage = -1;
	final static Logger logger = LogManager.getLogger(UIPlayer.class);
	
	public UIPlayer(int id) {
		super(id);
	}
	
	public void addShields(int s) {
		shields = s;
	}
	
	public ArrayList<AdventureCard> getPlayerHandAsList() {
		return hand.getDeck();
	}
	
	/*
	 * Adds the AdventureCard c to the player's hand
	 */
	public void addCard(AdventureCard c) {
		logger.info("Adding card: " + c.getName());
		hand.addCard(c, 1);
	}

	public boolean removeCard(AdventureCard c) {
		logger.info("Removing card: " + c.getName());
		return hand.getDeck().remove(c);
	}

	public void playCard(AdventureCard card) {
		setFaceDown(card);
	}
	
	private void setFaceDown(AdventureCard card) {
		List<AdventureCard> list = new ArrayList<AdventureCard>();
		list.add(hand.getCard(card));
		faceDown.addCards(list);
		logger.info("Setting: " + card.getName() + " face down");
	}

	public String getFaceDown() {
		return this.getFaceDownDeck().toString().replaceAll(" ", ",");
	}

	public void setPlayerRank(Rank.RANKS r) {
		rank = r;
	}
}
