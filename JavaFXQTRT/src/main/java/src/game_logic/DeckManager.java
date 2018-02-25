package src.game_logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.client.TestGameBoardScenarios;
import src.messages.game.GameStartClient.RIGGED;

public class DeckManager {
	
	final static Logger logger = LogManager.getLogger(DeckManager.class);
	
	private StoryDeck storyDeck;
	private AdventureDeck adventureDeck;
	private RIGGED rigged;
	
	public DeckManager() {
		this.storyDeck = new StoryDeck();
		this.adventureDeck = new AdventureDeck();
		storyDeck.populate();
		adventureDeck.populate();
	}
	
	public ArrayList<StoryCard> getStoryCard(int n) {
		if(storyDeck.size() - n <= 0) {
			storyDeck.reshuffle();
		}
		if(rigged.equals(RIGGED.THREE)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = storyDeck.getCardByName("Boar Hunt");
			if(s == null) {
				s = storyDeck.drawRandomCards(1).get(0);
			}
			toReturn.add(s);
			logger.info("Drawing Boar Hunt rigged or next card: " + toReturn.get(0).getName());
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		} else if(rigged.equals(RIGGED.TWO)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = storyDeck.getCardByName("Test of the Green Knight");
			if(s == null) {
				s = storyDeck.drawRandomCards(1).get(0);
			}
			toReturn.add(s);
			logger.info("Drawing Test of the Green Knight rigged or next card: " + toReturn.get(0).getName());
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		} else {
			ArrayList<StoryCard> toReturn = storyDeck.drawRandomCards(n);
			storyDeck.discards.addAll(toReturn);
			
			return toReturn;	
		}
	}
	public void addAdventureCard(List<AdventureCard> cards) {
		adventureDeck.discards.addAll(cards);
	}
	
	public ArrayList<AdventureCard> getAdventureCard(int n) {
		if(adventureDeck.size() - n <= 0) {
			adventureDeck.reshuffle();
		}
		return adventureDeck.drawRandomCards(n);
	}
	
	public int storySize() {
		return storyDeck.size();
	}
	
	public int adventureSize() {
		return adventureDeck.size();
	}

	public void setRigged(RIGGED rigged) {
		this.rigged = rigged;
	}
	
}