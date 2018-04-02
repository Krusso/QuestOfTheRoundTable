package com.qotrt.gameplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.AmourCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.model.UIPlayer;

public class TestPlayer {
	
	@Test
	public void testTristantIseultBoost() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		assertTrue(!p1.tristan && !p1.iseult);
		p1.addCards(cards);
		cards.forEach(i -> p1.setFaceDown(i));
		p1.flipCards();
		assertTrue(p1.tristan && p1.iseult);
	}
	
	@Test
	public void testHand() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		p1.addCards(cards);
		assertEquals("Sir Tristan Queen Iseult ", p1.hand());
		cards.forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		assertEquals("", p1.hand());
	}

	@Test
	public void testIncreaseLevel() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		assertTrue(p1.getShields() == 0);
		assertTrue(p1.getRank() == Rank.RANKS.SQUIRE);
		p1.changeShields(7);
		assertTrue(p1.getShields() == 7);
		assertTrue(p1.getRank() == Rank.RANKS.SQUIRE);
		p1.increaseLevel();
		assertTrue(p1.getShields() == 2);
		assertTrue(p1.getRank() == Rank.RANKS.KNIGHT);
		p1.increaseLevel();
		assertTrue(p1.getShields() == 2);
		assertTrue(p1.getRank() == Rank.RANKS.KNIGHT);
		p1.changeShields(10);
		assertTrue(p1.getShields() == 12);
		assertTrue(p1.getRank() == Rank.RANKS.KNIGHT);
		p1.increaseLevel();
		assertTrue(p1.getRank() == Rank.RANKS.CHAMPION);
		assertTrue(p1.getShields() == 5);
		p1.changeShields(5);
		assertTrue(p1.getRank() == Rank.RANKS.CHAMPION);
		assertTrue(p1.getShields() == 10);
		p1.increaseLevel();
		assertTrue(p1.getRank() == Rank.RANKS.KNIGHTOFTHEROUNDTABLE);
		assertTrue(p1.getShields() == 0);
	}
	
	@Test
	public void testDiscardWeapons() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		
		p1.addCards(cards);
		cards.forEach(i -> p1.setFaceDown(i));
		p1.flipCards();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse ".equals(p1.getFaceUp().toString()));
		p1.discardType(TYPE.WEAPONS);
		assertTrue("Sir Tristan Queen Iseult ".equals(p1.getFaceUp().toString()));
	}
	
	@Test
	public void testDiscardAmours() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		
		p1.addCards(cards);
		cards.forEach(i -> p1.setFaceDown(i));
		p1.flipCards();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse Amour ".equals(p1.getFaceUp().toString()));
		p1.discardType(TYPE.AMOUR);
		assertTrue("Sir Tristan Queen Iseult Dagger Horse ".equals(p1.getFaceUp().toString()));
	}
	
	@Test
	public void testDiscardAllies() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		
		p1.addCards(cards);
		cards.forEach(i -> p1.setFaceDown(i));
		p1.flipCards();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse Amour ".equals(p1.getFaceUp().toString()));
		p1.discardType(TYPE.ALLIES);
		assertTrue("Dagger Horse Amour ".equals(p1.getFaceUp().toString()));
	}
	
	@Test
	public void testCount() {
		Player p1 = new Player(0, new UIPlayer("", "",1));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		
		p1.addCards(cards);
		assertEquals(5, p1.getCardCount());
		assertEquals(2, p1.getTypeCount(TYPE.WEAPONS));
		assertEquals(0, p1.getTypeCount(TYPE.FOES));
	}
	
}
