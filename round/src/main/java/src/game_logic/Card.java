package src.game_logic;

public abstract class Card {
	private String name;
	
	public Card(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}