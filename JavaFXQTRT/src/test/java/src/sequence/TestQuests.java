package src.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.QuestCard;
import src.player.Player;
import src.player.PlayerManager;
import src.socket.OutputController;
import src.views.PlayerView;
import src.views.PlayersView;

public class TestQuests {
	
	@Test
	public void testNoSponsor() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",3,"Dragon"));
		LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<String> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));
		
		// decline to sponsor as each player
		for(int i = 0; i < 4; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			input.put("game quest decline: player " + i);
			Thread.sleep(100);
			assertEquals(Player.STATE.NO, players.get(i).getQuestion());
		}

		// no one accepted
		assertEquals(0,players.get(0).getShields());
		assertEquals(0,players.get(1).getShields());
		assertEquals(0,players.get(2).getShields());
		assertEquals(0,players.get(3).getShields());
	}
	
	@Test
	public void testNoParticipants() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",3,"Dragon"));
		LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<String> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));
		
		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put("game quest decline: player " + i);
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put("game quest sponsors: player " + i);
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

		while(true) {
			String string = actualOutput.take();
			System.out.println(string);
			if(string.equals("quest sponsoring: player 3")) break;
		}

		// sending cards for each stage
		input.put("game quest stage picked: 0 cards: Boar");
		input.put("game quest stage picked: 1 cards: Saxons");
		input.put("game quest stage picked: 2 cards: Saxons,Thieves");

		// decline to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			input.put("game quest decline quest: player " + i);
			Thread.sleep(100);
			assertEquals(Player.STATE.NO, players.get(i).getQuestion());
		}
	}
	
	@Test
	public void testBidding() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",1,"Dragon"));
		LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<String> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));
		
		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put("game quest decline: player " + i);
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put("game quest sponsors: player " + i);
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}
		
		while(true) {
			String string = actualOutput.take();
			System.out.println(string);
			if(string.equals("quest sponsoring: player 3")) break;
		}

		// sending cards for each stage
		input.put("game quest stage picked: 0 cards: Test of Valor");

		// agree to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			input.put("game quest join: player " + i);
			Thread.sleep(100);
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}
		
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(0).getQuestion());
		input.put("game quest bid: player 0 1");
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(1).getQuestion());
		input.put("game quest bid: player 1 skip");
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(2).getQuestion());
		input.put("game quest bid: player 2 2");

		Thread.sleep(100);
		assertEquals(Player.STATE.TESTDISCARD, players.get(2).getQuestion());
		
		while(true) {
			String string = actualOutput.take();
			System.out.println(string);
			if(string.equals("quest bidding discard: player 2")) break;
		}
		
		input.put("game test discard: player 2 Thieves,Thieves");
		Thread.sleep(100);
		assertEquals(12, players.get(2).getCardCount());
		assertEquals(0, players.get(0).getShields());
		assertEquals(0, players.get(1).getShields());
		assertEquals(1, players.get(2).getShields());
		assertEquals(0, players.get(3).getShields());
		
	}
	
	@Test
	public void testFightingFoe() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",2,"Dragon"));
		LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<String> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));
		
		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put("game quest decline: player " + i);
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put("game quest sponsors: player " + i);
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}
		
		while(true) {
			String string = actualOutput.take();
			System.out.println(string);
			if(string.equals("quest sponsoring: player 3")) break;
		}

		// sending cards for each stage
		input.put("game quest stage picked: 0 cards: Boar");
		input.put("game quest stage picked: 1 cards: Saxons");

		// agree to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				String string = actualOutput.take();
				System.out.println(string);
				if(string.equals("quest deciding: player " + i)) break;
			}
			input.put("game quest join: player " + i);
			Thread.sleep(100);
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}
		
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(0).getQuestion());
		input.put("game cards picked for quest: player 0 Lance");
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(1).getQuestion());
		input.put("game cards picked for quest: player 1 Lance,Excalibur");
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(2).getQuestion());
		input.put("game cards picked for quest: player 2 Battle-ax");

		while(true) {
			String string = actualOutput.take();
			System.out.println(string);
			if(string.equals("quest picking: player 0")) break;
		}
		
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(0).getQuestion());
		input.put("game cards picked for quest: player 0 Lance");
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(1).getQuestion());
		input.put("game cards picked for quest: player 1 Lance,Excalibur");
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(2).getQuestion());
		input.put("game cards picked for quest: player 2 Dagger");
		
		Thread.sleep(100);
		assertEquals(12, players.get(2).getCardCount());
		assertEquals(2, players.get(0).getShields());
		assertEquals(2, players.get(1).getShields());
		assertEquals(0, players.get(2).getShields());
		assertEquals(0, players.get(3).getShields());
	}

	DeckManager dm;
	PlayerManager pm;
	BoardModel bm;
	PlayerView pv;
	OutputController oc;
	LinkedBlockingQueue<String> output;
	@Before
	public void before() {
		output = new LinkedBlockingQueue<String>();
		oc = new OutputController(output);
		dm = new DeckManager();
		pm = new PlayerManager(4, dm);
		bm = new BoardModel();
		pv = new PlayerView(oc);
		pm.subscribe(pv);
		PlayersView pvs = new PlayersView(oc);
		pm.subscribe(pvs);
		pm.start();
		pm.nextTurn();
	}

}
