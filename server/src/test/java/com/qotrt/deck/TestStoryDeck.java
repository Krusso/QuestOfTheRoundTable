package com.qotrt.deck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qotrt.cards.StoryCard;
import com.qotrt.model.RiggedModel.RIGGED;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestStoryDeck {

	final static Logger logger = LogManager.getLogger(TestStoryDeck.class);
	
	@Test
	public void test56Cards() {
		DeckManager dm = new DeckManager();
		dm.setRigged(RIGGED.NORMAL);
		ArrayList<StoryCard> cards = dm.getStoryCard(28);
		assertEquals(0,dm.storySize()); 
		for(StoryCard c: cards) {
			logger.info("Ensuring card: " + c.getName() + " is in the deck");
			assertTrue(contains(c.getName()));
		}
		
		logger.info("all cards: " + cards.stream().map(i -> i.getName()).collect(Collectors.toList()));
		
		cards = dm.getStoryCard(28);
		assertEquals(0,dm.storySize()); 
		for(StoryCard c: cards) {
			logger.info("Ensuring card: " + c.getName() + " is in the deck");
			assertTrue(contains(c.getName()));
		}
	}
	
	@Test
	public void testCardFrequency() {
		DeckManager dm = new DeckManager();
		dm.setRigged(RIGGED.NORMAL);
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
		logger.info("Checking cards: " + cards + " has " + i + " instances of " + string);
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
