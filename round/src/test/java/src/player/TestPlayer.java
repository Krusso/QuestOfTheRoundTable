package src.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import src.game_logic.AdventureCard;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.Rank;
import src.game_logic.WeaponCard;
import src.game_logic.AdventureCard.TYPE;

public class TestPlayer {
	
	@Test
	public void testTristantIseultBoost() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		assertTrue(!p1.hasTristanIseultBoost());
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		assertTrue(p1.hasTristanIseultBoost());
	}
	
	@Test
	public void testHand() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		p1.addCards(cards);
		assertEquals("Sir Tristan Queen Iseult ", p1.hand());
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		assertEquals("", p1.hand());
	}

	@Test
	public void testIncreaseLevel() {
		Player p1 = new Player(0);
		assertTrue(p1.getShields() == 0);
		assertTrue(p1.getRank() == Rank.RANKS.SQUIRE);
		p1.changeShields(22);
		assertTrue(p1.getShields() == 22);
		assertTrue(p1.getRank() == Rank.RANKS.SQUIRE);
		p1.increaseLevel();
		assertTrue(p1.getShields() == 17);
		assertTrue(p1.getRank() == Rank.RANKS.KNIGHT);
		p1.increaseLevel();
		assertTrue(p1.getShields() == 10);
		assertTrue(p1.getRank() == Rank.RANKS.CHAMPION);
		p1.increaseLevel();
		assertTrue(p1.getRank() == Rank.RANKS.KNIGHTOFTHEROUNDTABLE);
		assertTrue(p1.getShields() == 0);
	}
	
	@Test
	public void testDiscardWeapons() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse ".equals(p1.getFaceUp().toString()));
		p1.discardWeapons();
		assertTrue("Sir Tristan Queen Iseult ".equals(p1.getFaceUp().toString()));
	}
	
	@Test
	public void testDiscardAmours() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse Amour ".equals(p1.getFaceUp().toString()));
		p1.discardAmours();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse ".equals(p1.getFaceUp().toString()));
	}
	
	@Test
	public void testDiscardAllies() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		assertTrue("Sir Tristan Queen Iseult Dagger Horse Amour ".equals(p1.getFaceUp().toString()));
		p1.discardAllies();
		assertTrue("Dagger Horse Amour ".equals(p1.getFaceUp().toString()));
	}
	
	@Test
	public void testCount() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		
		p1.addCards(cards);
		assertEquals(5, p1.getCardCount());
		assertEquals(2, p1.getWeaponCount());
		assertEquals(0, p1.getFoeCount());
	}
	
}
