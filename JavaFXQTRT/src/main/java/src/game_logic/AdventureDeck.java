package src.game_logic;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard.TYPE;

public class AdventureDeck extends Deck<AdventureCard> {
	
	public AdventureDeck() {
		super();
	}
	
	void populate() {
		addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS),2);
		addCard(new WeaponCard("Lance",20, TYPE.WEAPONS),6);
		addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS),8);
		addCard(new WeaponCard("Sword",10, TYPE.WEAPONS),16);
		addCard(new WeaponCard("Horse",10, TYPE.WEAPONS),11);
		addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS),6);
		
		addCard(new FoeCard("Dragon",50,70, TYPE.FOES),1);
		addCard(new FoeCard("Giant",40, TYPE.FOES),2);
		addCard(new FoeCard("Mordred",30, TYPE.FOES),4);
		addCard(new FoeCard("Green Knight",25,40, TYPE.FOES),2);
		addCard(new FoeCard("Black Knight",25,35, TYPE.FOES),3);
		addCard(new FoeCard("Evil Knight",20,30, TYPE.FOES),6);
		addCard(new FoeCard("Saxon Knight",15,25, TYPE.FOES),8);
		addCard(new FoeCard("Robber Knight",15, TYPE.FOES),7);
		addCard(new FoeCard("Saxons",10,20, TYPE.FOES),5);
		addCard(new FoeCard("Boar",5,15, TYPE.FOES),4);
		addCard(new FoeCard("Thieves",5, TYPE.FOES),8);
		
		addCard(new TestCard("Test of Valor", TYPE.TESTS),2);
		addCard(new TestCard("Test of Tempation", TYPE.TESTS),2);
		addCard(new TestCard("Test of Morgan Le Fey",3, TYPE.TESTS),2);
		addCard(new TestCard("Test of the Questing Beast",3,4, TYPE.TESTS),2);
		
		addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES),1);
		addCard(new AllyCard("Sir Lancelot",15,25, TYPE.ALLIES),1);
		addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES),1);
		addCard(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES),1); // namedBattlePoints here = when Queen Iseult is in play
		addCard(new AllyCard("Sir Pellinore",10,10,0,4, TYPE.ALLIES),1);
		addCard(new AllyCard("Sir Gawain",10,20, TYPE.ALLIES),1);
		addCard(new AllyCard("Sir Percival",5,20, TYPE.ALLIES),1);
		addCard(new AllyCard("Queen Guinevere",0,0,3, TYPE.ALLIES),1);
		addCard(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES),1); // namedBids here = when Tristan is in play
		addCard(new AllyCard("Merlin", TYPE.ALLIES),1); // special power to preview any stage
		
		addCard(new AmourCard("Amour",10,1, TYPE.AMOUR),8);
	}

	public int getBP() {
		return deck.stream().mapToInt(i -> i.getBattlePoints()).sum();
	}

	public List<Card> discardType(TYPE type) {
		List<Card> removed = new ArrayList<Card>();
		deck.removeIf(i -> {
			if(i.getType() != type) {
				return false;
			}
			removed.add(i);
			return true;
		});
		
		return removed;
	}
	
	public int typeCount(TYPE type) {
		return deck.stream().mapToInt(i -> {
			if(i.getType() == type) {
				return 1;
			} else {
				return 0;
			}
		}).sum();
	}
	
	public ArrayList<AdventureCard> getDeck(){
		return deck;
	}
}