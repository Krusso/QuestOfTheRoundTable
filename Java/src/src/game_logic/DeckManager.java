package src.game_logic;

public class DeckManager {
	
	private StoryDeck storyDeck;
	private AdventureDeck adventureDeck;
	
	public DeckManager() {
		this.storyDeck = new StoryDeck();
		this.adventureDeck = new AdventureDeck();
		storyDeck.populate();
		adventureDeck.populate();
	}
	
	public Card nextStoryCard() {
		return storyDeck.getNextCard();
	}
	
	public Card nextAdventureCard() {
		return adventureDeck.getNextCard();
	}
	
}