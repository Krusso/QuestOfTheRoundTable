package src.game_logic;

import java.net.URL;

public class StoryCard extends Card {
	
	public static enum TYPE {
		QUEST, EVENT, TOURNAMENT, GAMEOVER;
	}
	
	public static final StoryCard GAMEOVER = new StoryCard("GAME OVER TOURNAMENT", TYPE.GAMEOVER);
	
	protected TYPE type;
	
	public StoryCard(String name, TYPE type) {
		super(name);
		this.type = type;
	}
	public StoryCard(String name, URL path) {
		super(name, path);
	}
	
	public TYPE getType() { return this.type; }
}