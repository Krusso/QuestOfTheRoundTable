package com.qotrt.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.EventCard;
import com.qotrt.cards.events.ChivalrousDeed;
import com.qotrt.cards.events.CourtCalledToCamelot;
import com.qotrt.cards.events.KingRecognition;
import com.qotrt.cards.events.KingsCallToArms;
import com.qotrt.cards.events.Plague;
import com.qotrt.cards.events.Pox;
import com.qotrt.cards.events.ProsperityThroughoutTheRealm;
import com.qotrt.cards.events.QueenFavor;
import com.qotrt.deck.DeckManager;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModel;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.EventModel;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;
import com.qotrt.views.PlayerView;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestEvent {

	final static Logger logger = LogManager.getLogger(TestEvent.class);
	
	@Test
	public void testKingsCallToArms() throws InterruptedException {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms", new KingsCallToArms()));
		Runnable thread2 = new Runnable() { @Override public void run() {	esm.start(pm, bmm, false);	} };
		new Thread(thread2).start();
		Thread.sleep(500);
		em.playCard(pm.players[0], 170, pm.players[0].hand);
		em.playCard(pm.players[1], 158, pm.players[1].hand);
		em.finishDiscarding(pm.players[0]);
		em.finishDiscarding(pm.players[1]);
		pm.round().forEachRemaining(i -> assertEquals(11, i.hand.getDeck().size()));
	}
	
	@Test
	public void testKingsCallToArmsDifferent() throws InterruptedException {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms", new KingsCallToArms()));
		pm.players[0].changeShields(20);
		pm.players[0].increaseLevel();
		Runnable thread2 = new Runnable() { @Override public void run() {	esm.start(pm, bmm, false);	} };
		new Thread(thread2).start();
		Thread.sleep(500);
		logger.info(em.playCard(pm.players[0], 170, pm.players[0].hand));
		logger.info(em.finishDiscarding(pm.players[0]));
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 0) {
				assertEquals(11, i.hand.getDeck().size());
			} else {
				assertEquals(12, i.hand.getDeck().size());
			}
		});
	}
	
	@Test
	public void testKingsCallToArmsFoes() throws InterruptedException {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms", new KingsCallToArms()));
		pm.players[0].changeShields(20);
		pm.players[0].increaseLevel();
		pm.players[0].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS);
		pm.players[1].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS);
		Runnable thread2 = new Runnable() { @Override public void run() {	esm.start(pm, bmm, false);	} };
		new Thread(thread2).start();
		Thread.sleep(500);
		logger.info(em.playCard(pm.players[0], 165, pm.players[0].hand));
		logger.info(em.playCard(pm.players[0], 166, pm.players[0].hand));
		logger.info(em.finishDiscarding(pm.players[0]));
		Thread.sleep(500);
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 0) {
				assertEquals(4, i.hand.getDeck().size());
			} else {
				assertEquals(6, i.hand.getDeck().size());
			}
		});
	}
	
	@Test
	public void testKingsCallToArmsNothing() throws InterruptedException {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms", new KingsCallToArms()));
		pm.players[0].changeShields(20);
		pm.players[0].increaseLevel();
		pm.players[0].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS || i.getType() == TYPE.FOES);
		pm.players[1].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS || i.getType() == TYPE.FOES);
		Runnable thread2 = new Runnable() { @Override public void run() {	esm.start(pm, bmm, false);	} };
		new Thread(thread2).start();
		Thread.sleep(500);
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 0) {
				assertEquals(1, i.hand.getDeck().size());
			} else {
				assertEquals(1, i.hand.getDeck().size());
			}
		});
	}

	@Test
	public void testProsperityThroughoutTheRealm(){
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Prosperity Throughout the Realm", new ProsperityThroughoutTheRealm()));
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(pm, bmm, false);
		pm.round().forEachRemaining(i -> {
			assertEquals(14, i.getCardCount());
		});
	}

	@Test
	public void testChivalrousDeedHigher() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed", new ChivalrousDeed()));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		player.remove(0);
		pm.changeShields(player, 10);
		esm.start(pm, bmm, false);
		assertEquals(3, players.get(0).getShields());
		assertEquals(10, players.get(1).getShields());
	}
	
	@Test
	public void testChivalrousDeed() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed", new ChivalrousDeed()));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		player.remove(1);
		pm.changeShields(player, 10);
		esm.start(pm, bmm, false);
		assertEquals(10, players.get(0).getShields());
		assertEquals(3, players.get(1).getShields());
	}
	
	@Test
	public void testChivalrousDeedDifferentRank() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed", new ChivalrousDeed()));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		pm.changeShields(player, 10);
		pm.round().forEachRemaining(i -> i.increaseLevel());
		esm.start(pm, bmm, false);
		assertEquals(8, players.get(0).getShields());
		assertEquals(8, players.get(1).getShields());
	}
	
	@Test
	public void testChivalrousDeedLower() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed", new ChivalrousDeed()));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		pm.changeShields(player, 10);
		esm.start(pm, bmm, false);
		assertEquals(13, players.get(0).getShields());
		assertEquals(13, players.get(1).getShields());
	}

	@Test
	public void testChivalrousDeedSame() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed", new ChivalrousDeed()));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		esm.start(pm, bmm, false);
		assertEquals(3, players.get(0).getShields());
		assertEquals(3, players.get(1).getShields());
	}

	
	@Test
	public void testPlague() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Plague", new Plague()));
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			assertEquals(true, i.getShields() == 0);
		});
		pm.changeShields(player, 10);
		esm.start(pm, bmm, false);
		assertEquals(8, player.get(0).getShields());
		assertEquals(10, player.get(1).getShields());
	}

	@Test
	public void testPox() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Pox", new Pox()));
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			assertEquals(true, i.getShields() == 0);
		});
		pm.changeShields(player, 10);
		esm.start(pm, bmm, false);
		assertEquals(10, player.get(0).getShields());
		assertEquals(9, player.get(1).getShields());
	}

	@Test	
	public void testCourtCalledToCamelot() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot", new CourtCalledToCamelot()));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		pm.players[0].addCards(cards);
		cards.stream().filter(i -> i.getName().equals("King Arthur")).forEach(i -> pm.players[0].setFaceDown(pm.players[0].getCardByID(i.id)));
		pm.players[0].flipCards();
		assertEquals(1, pm.players[0].getFaceUp().size());
		assertEquals("King Arthur", pm.players[0].getFaceUp().getDeck().get(0).getName());

		esm.start(pm, bmm, false);
		assertEquals(0, pm.players[0].getFaceUp().size());
	}
	
	@Test	
	public void testCourtCalledToCamelotMultiple() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot", new CourtCalledToCamelot()));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		pm.players[0].addCards(cards);
		cards.stream().filter(i -> i.getName().equals("King Arthur")).forEach(i -> pm.players[0].setFaceDown(pm.players[0].getCardByID(i.id)));
		pm.players[0].flipCards();
		assertEquals(2, pm.players[0].getFaceUp().size());
		assertEquals("King Arthur", pm.players[0].getFaceUp().getDeck().get(0).getName());
		assertEquals("King Arthur", pm.players[0].getFaceUp().getDeck().get(1).getName());

		esm.start(pm, bmm, false);
		assertEquals(0, pm.players[0].getFaceUp().size());
	}
	
	@Test
	public void testCourCalledToCamelotNoDiscard() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot", new CourtCalledToCamelot()));
		assertEquals(0, pm.players[0].getFaceUp().size());

		esm.start(pm, bmm, false);
		assertEquals(0, pm.players[0].getFaceUp().size());
	}

	@Test
	public void testQueensFavor() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor", new QueenFavor()));
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 12);
		});
		esm.start(pm, bmm, false);
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 14);
		});
	}

	@Test
	public void testQueensFavorOnlyLower() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor", new QueenFavor()));
		pm.players[0].changeShields(10);
		pm.players[0].increaseLevel();
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(pm, bmm, false);
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 0) {
				assertEquals(12, i.getCardCount());
			} else {
				assertEquals(14, i.getCardCount());
			}
		});
	}

	@Test
	public void testQueensAllKnight() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor", new QueenFavor()));
		pm.players[0].changeShields(10);
		pm.players[0].increaseLevel();
		pm.players[1].changeShields(10);
		pm.players[1].increaseLevel();
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(pm, bmm, false);
		pm.round().forEachRemaining(i -> {
			assertEquals(14, i.getCardCount());
		});
	}

	@Test
	public void testQueensFavorLower() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor", new QueenFavor()));
		pm.players[1].changeShields(10);
		pm.players[1].increaseLevel();
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(pm, bmm, false);
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 1) {
				assertEquals(12, i.getCardCount());
			} else {
				assertEquals(14, i.getCardCount());
			}
		});
	}

	@Test
	public void testKingsRecognition() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Recognition", new KingRecognition()));
		assertEquals(false, bm.isSetKingRecognition());
		logger.info("King recongition: " + bm.isSetKingRecognition());
		esm.start(pm, bmm, false);
		logger.info("King recongition: " + bm.isSetKingRecognition());
		assertEquals(true, bm.isSetKingRecognition());
	}

	DeckManager dm;
	PlayerManager pm;
	BoardModel bm;
	EventModel em;
	PlayerView pv;
	LinkedBlockingQueue<String> output;
	BoardModelMediator bmm;
	@Before
	public void before() {
		output = new LinkedBlockingQueue<String>();
		dm = new DeckManager();
		pm = new PlayerManager(2, new UIPlayer[] {new UIPlayer("","",1), new UIPlayer("","",1)}, dm, RIGGED.ONE);
		bm = new BoardModel();
		em = new EventModel();
		bmm = new BoardModelMediator(null, null, bm, null, em, null);
		pm.start();
		pm.nextTurn();
	}
}
