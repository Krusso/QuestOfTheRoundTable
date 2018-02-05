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
			System.out.println(" ----------- INDEX : " + i);
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
				input.put("game quest sponsor: player " + i);
				Thread.sleep(100);
				// assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}
		// make sure he's setting up the quest (STATE.PICKING)
		// decline to participate as all players
		
	}
	
	@Test
	public void testBidding() throws InterruptedException {}
	
	@Test
	public void testFightingFoe() throws InterruptedException {}

	@Test
	public void testOnePlayerAllStages() throws InterruptedException {
		// test one player completing all stages and tying
	}
	
	public void testThreePlayersNoWinner() throws InterruptedException {
		// test three players participating and no one wins 
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
