package com.qotrt.deck;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.Card;
import com.qotrt.cards.StoryCard;
import com.qotrt.model.RiggedModel.RIGGED;

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
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class, n, storyDeck,
					"Boar Hunt",
					"Prosperity Throughout the Realm",
					"Chivalrous Deed");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.FOUR)) {
			storyDeck.getCardByName("Boar Hunt");
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class,n, storyDeck,
					"Boar Hunt",
					"Repel the Saxon Raiders");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.LONG)) {
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class,  n, storyDeck,
					"Search for the Holy Grail",
					"Test of the Green Knight",
					"Tournament at York",
					"Pox",
					"Plague",
					"Queen's Favor",
					"Court Called to Camelot",
					"King's Call to Arms",
					"King's Recognition");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.AITOURNAMENT)) {
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class, n, storyDeck,
					"Tournament at York",
					"Tournament at Tintagel",
					"Tournament at Orkney",
					"Tournament at Camelot");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.AIQUEST) || rigged.equals(RIGGED.AIQUEST1)) {
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class, n, storyDeck,
					"Repel the Saxon Raiders",
					"Boar Hunt");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.AIQUEST2)) {
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class, n, storyDeck,
					"Repel the Saxon Raiders",
					"Pox",
					"Boar Hunt");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		if(rigged.equals(RIGGED.GAMEEND)) {
			ArrayList<StoryCard> toReturn = firstNotNull(StoryCard.class, n, storyDeck,
					"Pox");
			storyDeck.discards.add(toReturn.get(0));
			
			return toReturn;
		}
		
		ArrayList<StoryCard> toReturn = storyDeck.drawRandomCards(n);
		storyDeck.discards.addAll(toReturn);
			
		return toReturn;
	}
	
	private <T extends Card> ArrayList<T> firstNotNull(Class<T> a, int n, Deck<T> d, String... strings ) {
		ArrayList<T> list = new ArrayList<T>();
		for(String s: strings) {
			T c = d.getCardByName(s);
			if(c != null) {
				list.add(c);
			}
			if(list.size() == n) {
				return list;
			}
		}
		
		return d.drawRandomCards(n);
	}
	
	public void addAdventureCard(List<AdventureCard> cards) {
		adventureDeck.discards.addAll(cards);
	}
	
	public AdventureCard getAdventureCard(String string) {
		AdventureCard card =  adventureDeck.getCardByName(string);
		if(card == null) {
			card = adventureDeck.drawRandomCards(1).get(0);
		}
		return card;
	}
	
	public ArrayList<AdventureCard> getAdventureCard(int n) {
		if(adventureDeck.size() - n <= 0) {
			adventureDeck.reshuffle();
		}
		if(rigged.equals(RIGGED.AITOURNAMENT) || rigged.equals(RIGGED.AIQUEST) || rigged.equals(RIGGED.AIQUEST1) || rigged.equals(RIGGED.AIQUEST2)) {
			ArrayList<AdventureCard> toReturn = firstNotNull(AdventureCard.class, n, adventureDeck,
					"Test of Valor",
					"Test of Temptation",
					"Test of Morgan Le Fey",
					"Test of the Questing Beast",
					"Saxon Knight");
			return toReturn;
		}
		
		if(!rigged.equals(RIGGED.NORMAL) && !rigged.equals(RIGGED.ONE)) {
			ArrayList<AdventureCard> toReturn = firstNotNull(AdventureCard.class, n, adventureDeck,
					"Test of Valor",
					"Test of Temptation",
					"Test of Morgan Le Fey",
					"Test of the Questing Beast",
					"Sword",
					"Horse",
					"Battle-ax",
					"Lance",
					"Dagger",
					"Excalibur",
					"Amour",
					"Thieves",
					"Boar",
					"Saxons",
					"Saxon Knight",
					"Evil Knight",
					"Mordred");
			
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