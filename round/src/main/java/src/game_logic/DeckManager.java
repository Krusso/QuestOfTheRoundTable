package src.game_logic;

import java.util.ArrayList;

public class DeckManager {
	
	private StoryDeck storyDeck;
	private AdventureDeck adventureDeck;
	
	public DeckManager() {
		this.storyDeck = new StoryDeck();
		this.adventureDeck = new AdventureDeck();
		storyDeck.populate();
		adventureDeck.populate();
	}
	
	public ArrayList<StoryCard> getStoryCard(int n) {
		return storyDeck.drawCards(n);
	}
	
	public ArrayList<AdventureCard> getAdventureCard(int n) {
		return adventureDeck.drawCards(n);
	}
	
	public int storySize() {
		return storyDeck.size();
	}
	
	public int adventureSize() {
		return adventureDeck.size();
	}
	
}