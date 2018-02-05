package src.game_logic;

public class EventCard extends StoryCard {
	
	public EventCard(String name) {
		super(name, TYPE.EVENT);
	}
	
	public EventCard(String name, String path) {
		super(name, path);
	}
	
}