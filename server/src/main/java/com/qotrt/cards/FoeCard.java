package com.qotrt.cards;

public class FoeCard extends AdventureCard{
	
	public FoeCard(String name, int battlePoints, TYPE type) {
		super(name, battlePoints, type);
	}
	
	public FoeCard(String name, int battlePoints, int namedBattlePoints, TYPE type) {
		super(name, battlePoints, namedBattlePoints, type);
	}
}