package src.game_logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.messages.game.GameStartClient.RIGGED;

public class DeckManager {
	
	final static Logger logger = LogManager.getLogger(DeckManager.class);
	
	private StoryDeck storyDeck;
	private AdventureDeck adventureDeck;
	private RIGGED rigged = RIGGED.NORMAL;
	
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
		if(rigged.equals(RIGGED.TWO)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = storyDeck.getCardByName("Test of the Green Knight");
			if(s == null) {
				s = storyDeck.drawRandomCards(1).get(0);
			}
			toReturn.add(s);
			logger.info("Drawing Test of the Green Knight rigged or next card: " + toReturn.get(0).getName());
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.THREE)) {
			storyDeck.getCardByName("Boar Hunt");
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Boar Hunt",
					"Prosperity Throughout the Realm",
					"Chivalrous Deed");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.FOUR)) {
			storyDeck.getCardByName("Boar Hunt");
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Boar Hunt",
					"Repel the Saxon Raiders");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.LONG)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Search for the Holy Grail",
					"Test of the Green Knight",
					"Tournament at York",
					"Pox",
					"Plague",
					"Queen's Favor",
					"Court Called to Camelot",
					"King's Call to Arms",
					"King's Recognition");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.AITOURNAMENT)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Tournament at York",
					"Tournament at Tintagel",
					"Tournament at Orkney",
					"Tournament at Camelot");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.AIQUEST) || rigged.equals(RIGGED.AIQUEST1)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Repel the Saxon Raiders",
					"Boar Hunt");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.AIQUEST2)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Repel the Saxon Raiders",
					"Pox",
					"Boar Hunt");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.GAMEEND)) {
			ArrayList<StoryCard> toReturn = new ArrayList<StoryCard>();
			StoryCard s = firstNotNull(StoryCard.class, storyDeck,
					"Pox");
			toReturn.add(s);
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		ArrayList<StoryCard> toReturn = storyDeck.drawRandomCards(n);
		storyDeck.discards.addAll(toReturn);
			
		return toReturn;
	}
	
	private <T extends Card> T firstNotNull(Class<T> a, Deck<T> d, String... strings ) {
		for(String s: strings) {
			T c = d.getCardByName(s);
			if(c != null) {
				return c;
			}
		}
		
		return d.drawRandomCards(1).get(0);
	}
	
	public void addAdventureCard(List<AdventureCard> cards) {
		adventureDeck.discards.addAll(cards);
	}
	
	public AdventureCard getAdventureCard(String string) {
		return adventureDeck.getCardByName(string);
	}
	
	public ArrayList<AdventureCard> getAdventureCard(int n) {
		if(adventureDeck.size() - n <= 0) {
			adventureDeck.reshuffle();
		}
		if(rigged.equals(RIGGED.AITOURNAMENT) || rigged.equals(RIGGED.AIQUEST) || rigged.equals(RIGGED.AIQUEST1) || rigged.equals(RIGGED.AIQUEST2)) {
			ArrayList<AdventureCard> toReturn = new ArrayList<AdventureCard>();
			AdventureCard s = firstNotNull(AdventureCard.class, adventureDeck,
					"Test of Valor",
					"Test of Temptation",
					"Test of Morgan Le Fey",
					"Test of the Questing Beast",
					"Saxon Knight");
			toReturn.add(s);
			
			return toReturn;
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