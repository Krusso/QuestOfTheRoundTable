package src.game_logic;

public class DeckManager {
	
	private StoryDeck storyDeck;
	private RankDeck rankDeck;
	private AdventureDeck adventureDeck;
	
	public DeckManager() {
		this.storyDeck = new StoryDeck();
		this.rankDeck = new RankDeck();
		this.adventureDeck = new AdventureDeck();
	}
	
	public Card nextStoryCard() {
		return null;
	}
	
	public Card nextAdventureCard() {
		return null;
	}
	
}