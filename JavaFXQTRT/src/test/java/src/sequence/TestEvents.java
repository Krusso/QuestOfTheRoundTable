package src.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AllyCard;
import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.EventCard;
import src.messages.QOTRTQueue;
import src.messages.events.EventDiscardCardsClient;
import src.messages.events.EventDiscardCardsServer;
import src.messages.game.GameStartClient.RIGGED;
import src.player.Player;
import src.player.PlayerManager;
import src.socket.OutputController;
import src.views.PlayerView;

public class TestEvents {

	@Test
	public void testKingsCallToArms() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms"));
		QOTRTQueue q = new QOTRTQueue();
		q.put(new EventDiscardCardsClient(0, new String[] {"Dagger"}));
		q.put(new EventDiscardCardsClient(1, new String[] {"Dagger"}));
		esm.start(q, pm, bm);
		pm.round().forEachRemaining(i -> assertEquals(11, i.hand.getDeck().size()));
	}
	
	@Test
	public void testKingsCallToArmsDifferent() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms"));
		QOTRTQueue q = new QOTRTQueue();
		pm.players[0].changeShields(20);
		pm.players[0].increaseLevel();
		q.put(new EventDiscardCardsClient(0, new String[] {"Dagger"}));
		esm.start(q, pm, bm);
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 0) {
				assertEquals(11, i.hand.getDeck().size());
			} else {
				assertEquals(12, i.hand.getDeck().size());
			}
		});
	}
	
	@Test
	public void testKingsCallToArmsFoes() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms"));
		QOTRTQueue q = new QOTRTQueue();
		pm.players[0].changeShields(20);
		pm.players[0].increaseLevel();
		pm.players[0].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS);
		pm.players[1].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS);
		q.put(new EventDiscardCardsClient(0, new String[] {"Saxons", "Saxons"}));
		esm.start(q, pm, bm);
		pm.round().forEachRemaining(i -> {
			if(i.getID() == 0) {
				assertEquals(4, i.hand.getDeck().size());
			} else {
				assertEquals(6, i.hand.getDeck().size());
			}
		});
	}
	
	@Test
	public void testKingsCallToArmsNothing() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms"));
		QOTRTQueue q = new QOTRTQueue();
		pm.players[0].changeShields(20);
		pm.players[0].increaseLevel();
		pm.players[0].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS || i.getType() == TYPE.FOES);
		pm.players[1].hand.getDeck().removeIf(i -> i.getType() == TYPE.WEAPONS || i.getType() == TYPE.FOES);
		esm.start(q, pm, bm);
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
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Prosperity Throughout the Realm"));
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(new QOTRTQueue(), pm, bm);
		pm.round().forEachRemaining(i -> {
			assertEquals(14, i.getCardCount());
		});
	}

	@Test
	public void testChivalrousDeedHigher() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed"));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		player.remove(0);
		pm.changeShields(player, 10);
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(3, players.get(0).getShields());
		assertEquals(10, players.get(1).getShields());
	}
	
	@Test
	public void testChivalrousDeed() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed"));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		player.remove(1);
		pm.changeShields(player, 10);
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(10, players.get(0).getShields());
		assertEquals(3, players.get(1).getShields());
	}
	
	@Test
	public void testChivalrousDeedDifferentRank() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed"));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		pm.changeShields(player, 10);
		pm.round().forEachRemaining(i -> i.increaseLevel());
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(8, players.get(0).getShields());
		assertEquals(8, players.get(1).getShields());
	}
	
	@Test
	public void testChivalrousDeedLower() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed"));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		pm.changeShields(player, 10);
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(13, players.get(0).getShields());
		assertEquals(13, players.get(1).getShields());
	}

	@Test
	public void testChivalrousDeedSame() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed"));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(0, i.getShields());
		});
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(3, players.get(0).getShields());
		assertEquals(3, players.get(1).getShields());
	}

	
	@Test
	public void testPlague() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Plague"));
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			assertEquals(true, i.getShields() == 0);
		});
		pm.changeShields(player, 10);
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(8, player.get(0).getShields());
		assertEquals(10, player.get(1).getShields());
	}

	@Test
	public void testPox() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Pox"));
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			assertEquals(true, i.getShields() == 0);
		});
		pm.changeShields(player, 10);
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(10, player.get(0).getShields());
		assertEquals(9, player.get(1).getShields());
	}

	@Test	
	public void testCourtCalledToCamelot() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot"));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		pm.players[0].addCards(cards);
		pm.players[0].setFaceDown(new String[] {"King Arthur"});
		pm.players[0].flipCards();
		assertEquals(1, pm.players[0].getFaceUp().size());
		assertEquals("King Arthur", pm.players[0].getFaceUp().getDeck().get(0).getName());

		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(0, pm.players[0].getFaceUp().size());
	}
	
	@Test	
	public void testCourtCalledToCamelotMultiple() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot"));
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		pm.players[0].addCards(cards);
		pm.players[0].setFaceDown(new String[] {"King Arthur", "King Arthur"});
		pm.players[0].flipCards();
		assertEquals(2, pm.players[0].getFaceUp().size());
		assertEquals("King Arthur", pm.players[0].getFaceUp().getDeck().get(0).getName());
		assertEquals("King Arthur", pm.players[0].getFaceUp().getDeck().get(1).getName());

		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(0, pm.players[0].getFaceUp().size());
	}
	
	@Test
	public void testCourCalledToCamelotNoDiscard() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot"));
		assertEquals(0, pm.players[0].getFaceUp().size());

		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(0, pm.players[0].getFaceUp().size());
	}

	@Test
	public void testQueensFavor() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 12);
		});
		esm.start(new QOTRTQueue(), pm, bm);
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 14);
		});
	}

	@Test
	public void testQueensFavorOnlyLower() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		pm.players[0].changeShields(10);
		pm.players[0].increaseLevel();
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(new QOTRTQueue(), pm, bm);
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
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		pm.players[0].changeShields(10);
		pm.players[0].increaseLevel();
		pm.players[1].changeShields(10);
		pm.players[1].increaseLevel();
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(new QOTRTQueue(), pm, bm);
		pm.round().forEachRemaining(i -> {
			assertEquals(14, i.getCardCount());
		});
	}

	@Test
	public void testQueensFavorLower() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		pm.players[1].changeShields(10);
		pm.players[1].increaseLevel();
		pm.round().forEachRemaining(i -> {
			assertEquals(12, i.getCardCount());
		});
		esm.start(new QOTRTQueue(), pm, bm);
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
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Recognition"));
		PlayerManager pm = new PlayerManager(0, null, RIGGED.ONE);
		BoardModel bm = new BoardModel();
		assertEquals(false, bm.isSetKingRecognition());
		esm.start(new QOTRTQueue(), pm, bm);
		assertEquals(true, bm.isSetKingRecognition());
	}

	DeckManager dm;
	PlayerManager pm;
	BoardModel bm;
	PlayerView pv;
	LinkedBlockingQueue<String> output;
	@Before
	public void before() {
		output = new LinkedBlockingQueue<String>();
		dm = new DeckManager();
		pm = new PlayerManager(2, dm, RIGGED.ONE);
		bm = new BoardModel();
		pv = new PlayerView(new OutputController(output));
		pm.subscribe(pv);
		pm.start();
		pm.nextTurn();
	}
}
