package com.qotrt.deck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.qotrt.cards.AdventureCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;

public class TestAdventureDeck {

	final static Logger logger = LogManager.getLogger(TestAdventureDeck.class);

	@Test
	public void test250Cards() {
		DeckManager dm = new DeckManager();
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {new UIPlayer("","")}, dm, RIGGED.ONE);
		List<Player> players = Arrays.asList(pm.players);
		pm.drawCards(players, 125);

		logger.info("Ensuring 0 cards in adventure deck");
		assertEquals(0,dm.adventureSize());
		assertEquals(125, players.get(0).hand.size());

		for(AdventureCard c: players.get(0).hand.deck) {
			logger.info("Ensuring card: " + c.getName() + " is in the deck");
			assertTrue(contains(c.getName()));
		}

		logger.info("Discarding all cards from players hand");
		pm.discardFromHand(players.get(0), players.get(0).hand.deck.stream().map(i -> i.getName()).toArray(String[]::new));

		logger.info("Players hand is size 0");
		assertEquals(0, players.get(0).hand.size());

		pm.drawCards(players, 125);
		assertEquals(125, players.get(0).hand.size());
		assertEquals(0,dm.adventureSize()); 
		for(AdventureCard c: players.get(0).hand.deck) {
			logger.info("Ensuring card: " + c.getName() + " is in the deck");
			assertTrue(contains(c.getName()));
		}
	}

	@Test
	public void testCardFrequency() {
		DeckManager dm = new DeckManager();
		ArrayList<AdventureCard> cards = dm.getAdventureCard(125);
		assertTrue(contains(cards,"Excalibur",2));
		assertTrue(contains(cards,"Lance",6)); 
		assertTrue(contains(cards,"Battle-ax",8)); 
		assertTrue(contains(cards,"Sword",16) );
		assertTrue(contains(cards,"Horse",11) );
		assertTrue(contains(cards,"Dagger",6) );
		assertTrue(contains(cards,"Dragon",1) );
		assertTrue(contains(cards,"Giant",2) );
		assertTrue(contains(cards,"Mordred",4) );
		assertTrue(contains(cards,"Green Knight",2) );
		assertTrue(contains(cards,"Black Knight",3) );
		assertTrue(contains(cards,"Evil Knight",6));
		assertTrue(contains(cards,"Saxon Knight",8) );
		assertTrue(contains(cards,"Robber Knight",7) );
		assertTrue(contains(cards,"Saxons",5) );
		assertTrue(contains(cards,"Boar",4) );
		assertTrue(contains(cards,"Thieves",8) );
		assertTrue(contains(cards,"Test of Valor",2) );
		assertTrue(contains(cards,"Test of Temptation",2) );
		assertTrue(contains(cards,"Test of Morgan Le Fey",2) );
		assertTrue(contains(cards,"Test of the Questing Beast",2) );
		assertTrue(contains(cards,"Sir Galahad",1) );
		assertTrue(contains(cards,"Sir Lancelot",1) );
		assertTrue(contains(cards,"King Arthur",1) );
		assertTrue(contains(cards,"Sir Tristan",1) );
		assertTrue(contains(cards,"King Pellinore",1) );
		assertTrue(contains(cards,"Sir Gawain",1) );
		assertTrue(contains(cards,"Sir Percival",1) );
		assertTrue(contains(cards,"Queen Guinevere",1) );
		assertTrue(contains(cards,"Queen Iseult",1) );
		assertTrue(contains(cards,"Merlin",1) );
		assertTrue(contains(cards,"Amour",8));
	}

	private boolean contains(ArrayList<AdventureCard> cards, String string, int i) {
		logger.info("Checking cards: " + cards + " has " + i + " instances of " + string);
		for(AdventureCard c: cards) {
			if(c.getName().equals(string)) {
				i--;
			}
		}
		return i == 0;
	}

	public boolean contains(String s) {
		return s.equals("Excalibur") ||
				s.equals("Lance") ||
				s.equals("Battle-ax") ||
				s.equals("Sword") ||
				s.equals("Horse") ||
				s.equals("Dagger") ||
				s.equals("Dragon") ||
				s.equals("Giant") ||
				s.equals("Mordred") ||
				s.equals("Green Knight") ||
				s.equals("Black Knight") ||
				s.equals("Evil Knight") ||
				s.equals("Saxon Knight") ||
				s.equals("Robber Knight") ||
				s.equals("Saxons") ||
				s.equals("Boar") ||
				s.equals("Thieves") ||
				s.equals("Test of Valor") ||
				s.equals("Test of Temptation") ||
				s.equals("Test of Morgan Le Fey") ||
				s.equals("Test of the Questing Beast") ||
				s.equals("Sir Galahad") ||
				s.equals("Sir Lancelot") ||
				s.equals("King Arthur") ||
				s.equals("Sir Tristan") ||
				s.equals("King Pellinore") ||
				s.equals("Sir Gawain") ||
				s.equals("Sir Percival") ||
				s.equals("Queen Guinevere") ||
				s.equals("Queen Iseult") ||
				s.equals("Merlin") ||
				s.equals("Amour");
	}	
}
