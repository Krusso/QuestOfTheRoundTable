package src.game_logic;

public class QuestCard extends StoryCard {
	private int stages;
	private String foe;
	
	public QuestCard(String name, int stages, String foe) {
		super(name, TYPE.QUEST);
		this.stages = stages;
		this.foe = foe; // assumes only one named foe per quest... need to clarify if this is the case
	}
	
	public int getNumStages() { return this.stages; }
	public String getFoe() { return this.foe; }
}