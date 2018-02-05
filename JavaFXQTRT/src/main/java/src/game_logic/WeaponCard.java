package src.game_logic;

public class WeaponCard extends AdventureCard{
	
	public WeaponCard(String name, int battlePoints, TYPE type) {
		super(name, battlePoints, type);
	}

	public WeaponCard(String name, String path) {
		super(name,path);
	}
	
	
	
}