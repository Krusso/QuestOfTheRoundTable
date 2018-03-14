package com.qotrt.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.AmourCard;
import com.qotrt.cards.Card;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.TestCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.cards.AdventureCard.TYPE;

public class AdventureDeck extends Deck<AdventureCard> {
	
	public AdventureDeck() {
		super();
	}
	
	public ArrayList<AdventureCard> discards = new ArrayList<AdventureCard>();
	
	public void populate() {
		IntStream.range(0, 2).forEach(i -> addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS)));
		IntStream.range(0, 6).forEach(i -> addCard(new WeaponCard("Lance",20, TYPE.WEAPONS)));
		IntStream.range(0, 8).forEach(i -> addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS)));
		IntStream.range(0, 16).forEach(i -> addCard(new WeaponCard("Sword",10, TYPE.WEAPONS)));
		IntStream.range(0, 11).forEach(i -> addCard(new WeaponCard("Horse",10, TYPE.WEAPONS)));
		IntStream.range(0, 6).forEach(i -> addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS)));
		
		IntStream.range(0, 1).forEach(i -> addCard(new FoeCard("Dragon",50,70, TYPE.FOES)));
		IntStream.range(0, 2).forEach(i -> addCard(new FoeCard("Giant",40, TYPE.FOES)));
		IntStream.range(0, 4).forEach(i -> addCard(new FoeCard("Mordred",30, TYPE.FOES)));
		IntStream.range(0, 2).forEach(i -> addCard(new FoeCard("Green Knight",25,40, TYPE.FOES)));
		IntStream.range(0, 3).forEach(i -> addCard(new FoeCard("Black Knight",25,35, TYPE.FOES)));
		IntStream.range(0, 6).forEach(i -> addCard(new FoeCard("Evil Knight",20,30, TYPE.FOES)));
		
		IntStream.range(0, 8).forEach(i -> addCard(new FoeCard("Saxon Knight",15,25, TYPE.FOES)));
		IntStream.range(0, 7).forEach(i -> addCard(new FoeCard("Robber Knight",15, TYPE.FOES)));
		IntStream.range(0, 5).forEach(i -> addCard(new FoeCard("Saxons",10,20, TYPE.FOES)));
		IntStream.range(0, 4).forEach(i -> addCard(new FoeCard("Boar",5,15, TYPE.FOES)));
		IntStream.range(0, 8).forEach(i -> addCard(new FoeCard("Thieves",5, TYPE.FOES)));
		
		IntStream.range(0, 2).forEach(i -> addCard(new TestCard("Test of Valor", TYPE.TESTS)));
		IntStream.range(0, 2).forEach(i -> addCard(new TestCard("Test of Temptation", TYPE.TESTS)));
		IntStream.range(0, 2).forEach(i -> addCard(new TestCard("Test of Morgan Le Fey",3, TYPE.TESTS)));
		IntStream.range(0, 2).forEach(i -> addCard(new TestCard("Test of the Questing Beast",3,4, TYPE.TESTS)));
		
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Sir Lancelot",15,25, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES))); // namedBattlePoints here = when Queen Iseult is in play
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("King Pellinore",10,10,0,4, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Sir Gawain",10,20, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Sir Percival",5,20, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Queen Guinevere",0,0,3, TYPE.ALLIES)));
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES)));// namedBids here = when Tristan is in play
		IntStream.range(0, 1).forEach(i -> addCard(new AllyCard("Merlin", TYPE.ALLIES),1));  // special power to preview any stage
		
		IntStream.range(0, 8).forEach(i -> addCard(new AmourCard("Amour",10,1, TYPE.AMOUR)));
	}
	
	public void reshuffle() {
		this.deck.addAll(discards);
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