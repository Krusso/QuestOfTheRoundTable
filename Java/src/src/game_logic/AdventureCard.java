package src.game_logic;

public class AdventureCard extends Card{
	protected int battlePoints;
	
	public AdventureCard(String name) {
		super(name);
	}
	
	public AdventureCard(String name, int battlePoints) {
		super(name);
		this.battlePoints = battlePoints;
	}
	
	public int getBattlePoints() {
		return this.battlePoints;
	}
}