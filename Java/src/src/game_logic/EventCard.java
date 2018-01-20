package src.game_logic;

public class EventCard extends StoryCard {
	
	public EventCard(String name) {
		super(name);
		this.type = TYPE.Event;
	}
}