package src.game_logic;

public class AdventureDeck extends Deck {
	
	public AdventureDeck() {
		super();
	}
	
	void populate() {
		addCard(new WeaponCard("Excalibur",30),2);
		addCard(new WeaponCard("Lance",20),6);
		addCard(new WeaponCard("Battle-ax",15),8);
		addCard(new WeaponCard("Sword",10),16);
		addCard(new WeaponCard("Horse",10),11);
		addCard(new WeaponCard("Dagger",5),6);
	}
}