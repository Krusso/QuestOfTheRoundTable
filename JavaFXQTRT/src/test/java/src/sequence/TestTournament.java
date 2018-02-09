package src.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.TournamentCard;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.messages.tournament.TournamentPickCardsClient;
import src.player.Player;
import src.player.PlayerManager;
import src.socket.OutputController;
import src.views.PlayerView;
import src.views.PlayersView;

public class TestTournament {

	@Test
	public void testWinSecondRound() throws InterruptedException {
		TournamentSequenceManager tsm = new TournamentSequenceManager(new TournamentCard("Tournament at Camelot", 3));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { tsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// accept tournament as all 4 players
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.JOINTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTIONED, players.get(i).getQuestion());
			input.put(new TournamentAcceptDeclineClient(i, true));
			actualOutput.take();
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}

		// select cards as all 4 players
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.PICKTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.PICKING, players.get(i).getQuestion());
			if(i == 0 || i == 1) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Lance", "Lance"}));
			} else if(i == 2 || i == 3) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Lance"}));
			}   
		}

		// select cards for top 2 players
		for(int i = 0; i < 2; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.PICKTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.PICKING, players.get(i).getQuestion());
			if(i == 0) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Excalibur"}));
			} else if(i == 1) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Dagger"}));
			}
		}


		// player 0 won and player 1 won
		while(true) {
			Message string = actualOutput.take();
			System.out.println(string);
			if(string.message == MESSAGETYPES.WINTOURNAMENT && string.player == 0) break;
		}
		assertEquals(7,players.get(0).getShields());
		assertEquals(0,players.get(1).getShields());
		assertEquals(0,players.get(2).getShields());
		assertEquals(0,players.get(3).getShields());
	}

	@Test
	public void testDrawSecondRound() throws InterruptedException {
		TournamentSequenceManager tsm = new TournamentSequenceManager(new TournamentCard("Tournament at Camelot", 3));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { tsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// accept tournament as all 4 players
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.JOINTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTIONED, players.get(i).getQuestion());
			input.put(new TournamentAcceptDeclineClient(i, true));
			actualOutput.take();
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}

		// select cards as all 4 players
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.PICKTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.PICKING, players.get(i).getQuestion());
			if(i == 0 || i == 1) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Lance", "Lance"}));	
			} else if(i == 2 || i == 3) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Lance"}));
			}
		}

		// select cards for top 2 players
		for(int i = 0; i < 2; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.PICKTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.PICKING, players.get(i).getQuestion());
			input.put(new TournamentPickCardsClient(i, new String[] {"Dagger"}));	
		}


		// player 0 won and player 1 won
		while(true) {
			Message string = actualOutput.take();
			System.out.println(string);
			if(string.message == MESSAGETYPES.WINTOURNAMENT && string.player == 1) break;
		}
		assertEquals(7,players.get(0).getShields());
		assertEquals(7,players.get(1).getShields());
		assertEquals(0,players.get(2).getShields());
		assertEquals(0,players.get(3).getShields());
	}

	@Test
	public void testNoOneEnter() throws InterruptedException {
		TournamentSequenceManager tsm = new TournamentSequenceManager(new TournamentCard("Tournament at Camelot", 3));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { tsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();

		// accept tournament only as player 0
		iter.forEachRemaining(i -> players.add(i));
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.JOINTOURNAMENT && string.player == i) break;
			}
			input.put(new TournamentAcceptDeclineClient(i, false));
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
	public void testOnePlayer() throws InterruptedException {
		TournamentSequenceManager tsm = new TournamentSequenceManager(new TournamentCard("Tournament at Camelot", 3));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { tsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();

		// accept tournament only as player 0
		iter.forEachRemaining(i -> players.add(i));
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.JOINTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTIONED, players.get(i).getQuestion());
			if(i == 0) { 
				input.put(new TournamentAcceptDeclineClient(i, true));
				actualOutput.take();
				assertEquals(Player.STATE.YES, players.get(i).getQuestion());
			} else { 
				input.put(new TournamentAcceptDeclineClient(i, false));
				actualOutput.take();
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			}
		}

		// player 0 won
		assertEquals(4,players.get(0).getShields());
		assertEquals(0,players.get(1).getShields());
		assertEquals(0,players.get(2).getShields());
		assertEquals(0,players.get(3).getShields());
	}

	@Test
	public void testFourPlayer() throws InterruptedException {
		TournamentSequenceManager tsm = new TournamentSequenceManager(new TournamentCard("Tournament at Camelot", 3));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { tsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// accept tournament as all 4 players
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.JOINTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTIONED, players.get(i).getQuestion());
			input.put(new TournamentAcceptDeclineClient(i, true));
			actualOutput.take();
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}

		// select cards as all 4 players
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				System.out.println(string);
				if(string.message == MESSAGETYPES.PICKTOURNAMENT && string.player == i) break;
			}
			assertEquals(Player.STATE.PICKING, players.get(i).getQuestion());
			if(i == 0) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Excalibur", "Lance", "Lance", "Battle-ax"}));	
			} else if(i == 1) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Excalibur", "Lance", "Lance"}));		
			} else if(i == 2) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Excalibur", "Lance"}));	
			} else if(i == 3) {
				input.put(new TournamentPickCardsClient(i, new String[] {"Excalibur"}));	
			}   
		}


		// player 0 won
		while(true) {
			Message string = actualOutput.take();
			System.out.println(string);
			if(string.message == MESSAGETYPES.WINTOURNAMENT && string.player == 0) break;
		}
		assertEquals(7,players.get(0).getShields());
		assertEquals(0,players.get(1).getShields());
		assertEquals(0,players.get(2).getShields());
		assertEquals(0,players.get(3).getShields());
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
