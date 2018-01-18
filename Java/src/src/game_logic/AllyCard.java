package src.game_logic;

public class AllyCard extends AdventureCard {
	
	private int namedBattlePoints;
	
	public AllyCard(String name, int battlePoints, int namedBattlePoints) {
		super(name, battlePoints);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public int getNamedBattlePoints() { return this.namedBattlePoints; }
}