package src.game_logic;

import java.util.Arrays;
import java.util.List;

public class AdventureCard extends Card{
	
	public static enum TYPE {
		WEAPONS, FOES, TESTS, ALLIES, AMOUR
	}
	
	protected int battlePoints;
	protected int namedBattlePoints;
	protected TYPE type;
	protected boolean named;
	
	public AdventureCard(String name, TYPE type) {
		super(name);
		this.type = type;
		this.named = false;
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
	
	public boolean checkIfNamed(String quest) {
		// "When a Foe is named on the Quest Card in play" is ambiguous so
		// rn just check if the quest and the card name have a word in common...
		// maybe should have a switch/case or lookup later on?
	    List<String> questwords = Arrays.asList(quest.split(" ")); 
	    for (String word : getName().split(" ")) {
	        if(questwords.contains(word))
	        	return true;
	    } return false;
	}
	
	public boolean isNamed() { return this.named; }
	public void name() { this.named = true; }
}