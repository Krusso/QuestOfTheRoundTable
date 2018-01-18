package src.game_logic;

public class FoeCard extends AdventureCard{
	
	private int namedBattlePoints;
	
	public FoeCard(String name, int battlePoints, int namedBattlePoints) {
		super(name, battlePoints);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public int getNamedBattlePoints() { return this.namedBattlePoints; }
}