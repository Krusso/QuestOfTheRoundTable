package com.qotrt.cards;

public abstract class AdventureCard extends Card{
	
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
		this.namedBattlePoints = this.battlePoints;
	}
	
	public AdventureCard(String name, int battlePoints, int namedBattlePoints, TYPE type) {
		this(name, battlePoints, type);
		this.namedBattlePoints = namedBattlePoints;
	}
	
	public TYPE getType() { return this.type; }
	public int getBattlePoints() { return this.battlePoints; }
	public int getNamedBattlePoints() { return this.namedBattlePoints; }
	
	public boolean isNamed() { return this.named; }
	public void name() { this.named = true; }

	public boolean checkIfNamed(String[] foe) {
		for(String c: foe) {
			if(c.equals("All") && this.type != TYPE.ALLIES) {
				return true;
			} else if(c.equals(this.getName())) {
				return true;
			}
		}
	    return false;
	}
}