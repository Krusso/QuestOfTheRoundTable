package src.game_logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TestStoryDeck {

	@Test
	public void test56Cards() {
		DeckManager dm = new DeckManager();
		ArrayList<StoryCard> cards = dm.getStoryCard(28);
		assertEquals(0,dm.storySize()); 
		for(StoryCard c: cards) {
			assertTrue(contains(c.getName()));
		}
		
		cards = dm.getStoryCard(28);
		assertEquals(0,dm.storySize()); 
		for(StoryCard c: cards) {
			assertTrue(contains(c.getName()));
		}
	}
	
	@Test
	public void testCardFrequency() {
		DeckManager dm = new DeckManager();
		ArrayList<StoryCard> cards = dm.getStoryCard(28);
		assertTrue(contains(cards, "Search for the Holy Grail", 1));
		assertTrue(contains(cards, "Test of the Green Knight", 1));
		assertTrue(contains(cards, "Search for the Questing Beast", 1));
		assertTrue(contains(cards, "Defend the Queen's Honor", 1));
		assertTrue(contains(cards, "Rescue the Fair Maiden", 1));
		assertTrue(contains(cards, "Journey Through the Enchanted Forest", 1));
		assertTrue(contains(cards, "Vanquish King Arthur's Enemies", 2));
		assertTrue(contains(cards, "Boar Hunt", 2));
		assertTrue(contains(cards, "Slay the Dragon", 1));
		assertTrue(contains(cards, "Repel the Saxon Raiders", 2));
		assertTrue(contains(cards, "Tournament at Camelot", 1));
		assertTrue(contains(cards, "Tournament at Orkney", 1));
		assertTrue(contains(cards, "Tournament at Tintagel", 1));
		assertTrue(contains(cards, "Tournament at York", 1));
		assertTrue(contains(cards, "King's Recognition", 2));
		assertTrue(contains(cards, "Queen's Favor", 2));
		assertTrue(contains(cards, "Court Called to Camelot", 2));
		assertTrue(contains(cards, "Pox", 1));
		assertTrue(contains(cards, "Plague", 1));
		assertTrue(contains(cards, "Chivalrous Deed", 1));
		assertTrue(contains(cards, "Prosperity Throughout the Realm", 1));
		assertTrue(contains(cards, "King's Call to Arms", 1));
	}
	
	private boolean contains(ArrayList<StoryCard> cards, String string, int i) {
		for(StoryCard c: cards) {
			if(c.getName().equals(string)) {
				i--;
			}
		}
		return i == 0;
	}

	public boolean contains(String s) {
		return s.equals("Search for the Holy Grail") ||
				s.equals("Test of the Green Knight") ||
				s.equals("Search for the Questing Beast") ||
				s.equals("Defend the Queen's Honor") ||
				s.equals("Rescue the Fair Maiden") ||
				s.equals("Journey Through the Enchanted Forest") ||
				s.equals("Vanquish King Arthur's Enemies") ||
				s.equals("Boar Hunt") ||
				s.equals("Slay the Dragon") ||
				s.equals("Repel the Saxon Raiders") ||
				s.equals("Tournament at Camelot") ||
				s.equals("Tournament at Orkney") ||
				s.equals("Tournament at Tintagel") ||
				s.equals("Tournament at York") ||
				s.equals("King's Recognition") ||
				s.equals("Queen's Favor") ||
				s.equals("Court Called to Camelot") ||
				s.equals("Pox") ||
				s.equals("Plague") ||
				s.equals("Chivalrous Deed") ||
				s.equals("Prosperity Throughout the Realm") ||
				s.equals("King's Call to Arms");
	}
	
}
