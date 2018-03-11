package com.qotrt.cards;

public class QuestCard extends StoryCard {
	private int stages;
	private String[] foe;
	
	public QuestCard(String name, int stages, String[] strings) {
		super(name, TYPE.QUEST);
		this.stages = stages;
		this.foe = strings; 
	}
	
	public int getNumStages() { return this.stages; }
	public String[] getFoe() { return this.foe; }
}