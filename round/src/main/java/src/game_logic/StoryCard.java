package src.game_logic;

public class StoryCard extends Card {
	
	public static enum TYPE {
		Quest, Event, Tournament;
	}
	
	protected TYPE type;
	
	public StoryCard(String name) {
		super(name);
	}
	
	public TYPE getType() { return this.type; }
}