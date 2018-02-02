package src.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.EventCard;
import src.player.Player;
import src.player.PlayerManager;
import src.socket.OutputController;
import src.views.PlayerView;

public class TestEvents {

	@Test
	public void testKingsCallToArms() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Call to Arms"));
		// WIP
		assertEquals(true, true);
	}
	
	@Test
	public void testProsperityThroughoutTheRealm(){
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 12);
		});
		esm.start(new LinkedBlockingQueue<String>(), pm, bm);
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 14);
		});
	}
	
	@Test
	public void testChivalrousDeed() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Chivalrous Deed"));
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> player = new ArrayList<Player>();
		pm.round().forEachRemaining(i -> {
			player.add(i);
			players.add(i);
			assertEquals(true, i.getShields() == 0);
		});
		player.remove(0);
		pm.changeShields(player, 10);
		esm.start(new LinkedBlockingQueue<String>(), pm, bm);
		assertEquals(3, players.get(0).getShields());
		assertEquals(10, players.get(1).getShields());
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
		esm.start(new LinkedBlockingQueue<String>(), pm, bm);
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
		esm.start(new LinkedBlockingQueue<String>(), pm, bm);
		assertEquals(10, player.get(0).getShields());
		assertEquals(9, player.get(1).getShields());
	}
	
	@Test
	public void testCourtCalledToCamelot() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Court Called to Camelot"));
		// WIP
		assertEquals(true, true);
	}
	
	@Test
	public void testQueensFavor() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 12);
		});
		esm.start(new LinkedBlockingQueue<String>(), pm, bm);
		pm.round().forEachRemaining(i -> {
			assertEquals(true, i.getCardCount() == 14);
		});
	}

	@Test
	public void testKingsRecognition() {
		EventSequenceManager esm = new EventSequenceManager(new EventCard("King's Recognition"));
		PlayerManager pm = new PlayerManager(0, null);
		BoardModel bm = new BoardModel();
		assertEquals(false, bm.isSetKingRecognition());
		esm.start(new LinkedBlockingQueue<String>(), pm, bm);
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
		pm = new PlayerManager(2, dm);
		bm = new BoardModel();
		pv = new PlayerView(new OutputController(output));
		pm.subscribe(pv);
		pm.start();
		pm.nextTurn();
	}
}
