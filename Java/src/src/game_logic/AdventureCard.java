package src.game_logic;

public class AdventureCard extends Card{
	
	protected int battlePoints;
	protected int namedBattlePoints;
	
	public AdventureCard(String name) {
		super(name);
	}
	
	public AdventureCard(String name, int battlePoints) {
		this(name);
		this.battlePoints = battlePoints;
	}
	
	public AdventureCard(String name, int battlePoints, int namedBattlePoints) {
		this(name, battlePoints);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public int getBattlePoints() { return this.battlePoints; }
	public int getNamedBattlePoints() { return this.namedBattlePoints; }
}