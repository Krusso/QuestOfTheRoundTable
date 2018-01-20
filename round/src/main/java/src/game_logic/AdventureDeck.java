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
		
		addCard(new FoeCard("Dragon",50,70),1);
		addCard(new FoeCard("Giant",40),2);
		addCard(new FoeCard("Mordred",30),4);
		addCard(new FoeCard("Green Knight",25,40),2);
		addCard(new FoeCard("Black Knight",25,35),3);
		addCard(new FoeCard("Evil Knight",20,30),60);
		addCard(new FoeCard("Saxon Knight",15,25),8);
		addCard(new FoeCard("Robber Knight",15),7);
		addCard(new FoeCard("Saxons",10,20),5);
		addCard(new FoeCard("Boar",5,15),4);
		addCard(new FoeCard("Thieves",5),8);
		
		addCard(new TestCard("Test of Valor"),2);
		addCard(new TestCard("Test of Tempation"),2);
		addCard(new TestCard("Test of Morgan Le Fey",3),2);
		addCard(new TestCard("Test of the Questing Beast",3,4),2);
		
		addCard(new AllyCard("Sir Galahad",15),1);
		addCard(new AllyCard("Sir Lancelot",15,25),1);
		addCard(new AllyCard("King Arthur",10,10,2),1);
		addCard(new AllyCard("Sir Tristan",10,20),1); // namedBattlePoints here = when Queen Iseult is in play
		addCard(new AllyCard("Sir Pellinore",10,10,0,4),1);
		addCard(new AllyCard("Sir Gawain",10,20),1);
		addCard(new AllyCard("Sir Percival",5,20),1);
		addCard(new AllyCard("Queen Guinevere",0,0,3),1);
		addCard(new AllyCard("Queen Iseult",0,0,2,4),1); // namedBids here = when Tristan is in play
		addCard(new AllyCard("Merlin"),1);
		
		addCard(new AmourCard("Amour",10,1),8);
	}
}