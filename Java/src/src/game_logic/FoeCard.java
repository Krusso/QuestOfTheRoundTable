package src.game_logic;

public class FoeCard extends AdventureCard{
	
	public FoeCard(String name, int battlePoints) {
		super(name, battlePoints);
	}
	
	public FoeCard(String name, int battlePoints, int namedBattlePoints) {
		super(name, battlePoints, namedBattlePoints);
	}
}