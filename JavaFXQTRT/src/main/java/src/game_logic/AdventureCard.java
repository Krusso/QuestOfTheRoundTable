package src.game_logic;

public class AdventureCard extends Card{
	
	public static enum TYPE {
		WEAPONS, FOES, TESTS, ALLIES, AMOUR
	}
	
	protected int battlePoints;
	protected int namedBattlePoints;
	protected TYPE type;
	
	public AdventureCard(String name, TYPE type) {
		super(name);
		this.type = type;
	}
	
	public AdventureCard(String name, int battlePoints, TYPE type) {
		this(name, type);
		this.battlePoints = battlePoints;
	}
	
	public AdventureCard(String name, int battlePoints, int namedBattlePoints, TYPE type) {
		this(name, battlePoints, type);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public TYPE getType() { return this.type; }
	public int getBattlePoints() { return this.battlePoints; }
	public int getNamedBattlePoints() { return this.namedBattlePoints; }
}