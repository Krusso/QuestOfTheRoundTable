package src.game_logic;

public class StoryDeck extends Deck {
	
	public StoryDeck() {
		super();
	}
	
	void populate() {
		addCard(new QuestCard("Search for the Holy Grail",5,"All"),1);
	}
}