package src.game_logic;

public class TournamentCard extends StoryCard {
	private int shields;
	
	public TournamentCard(String name, int shields) {
		super(name, TYPE.TOURNAMENT);
		this.shields = shields;
	}
	public TournamentCard(String name, String path) {
		super(name, path);
	}
	
	public int getShields() { return this.shields; }
}