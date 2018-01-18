package src.game_logic;

public class TournamentCard extends StoryCard {
	private int shields;
	
	public TournamentCard(String name, int shields) {
		super(name);
		this.shields = shields;
	}
	
	public int getShields() { return this.shields; }
}