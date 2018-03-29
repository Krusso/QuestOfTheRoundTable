package com.qotrt.sequence;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.Rank.RANKS;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.game.GameStartClient.RIGGED;
import src.messages.QOTRTQueue;
import src.messages.gameend.GameOverServer;
import src.messages.tournament.TournamentPickCardsClient;
import src.player.Player;
import src.player.PlayerManager;
import src.socket.OutputController;
import src.views.PlayerView;
import src.views.PlayersView;

public class TestFinalTournament {
	
	final static Logger logger = LogManager.getLogger(TestFinalTournament.class);
	
	
	@Test
	public void testOneWinnerFinalTournament() throws InterruptedException {
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(pm.players[0]);
		players.add(pm.players[1]);
		pm.changeShields(players, 100);
		pm.rankUp();
		
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { 		
			FinalTournamentSequenceManager ftsm = new FinalTournamentSequenceManager();
			ftsm.start(input, pm, bm); 
		};
		new Thread(task2).start();

		
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		while(true) {
			Message string = actualOutput.take();
			if(string.message == MESSAGETYPES.RANKUPDATE) break;
		}
		
		assertTrue(pm.players[0].getRank() == RANKS.KNIGHTOFTHEROUNDTABLE);
		assertTrue(pm.players[1].getRank() == RANKS.KNIGHTOFTHEROUNDTABLE);
		
		input.put(new TournamentPickCardsClient(0, new String[] {"Dagger"}));
		input.put(new TournamentPickCardsClient(1, new String[] {}));
		
		while(true) {
			Message string = actualOutput.take();
			if(string.message == MESSAGETYPES.GAMEOVER) {
				GameOverServer x = (GameOverServer) string;
				assertTrue(x.players[0] == 0);
				break;
			}
		}
	}
	
	@Test
	public void testDrawFinalTournament() throws InterruptedException {
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(pm.players[0]);
		players.add(pm.players[1]);
		pm.changeShields(players, 100);
		pm.rankUp();
		
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { 		
			FinalTournamentSequenceManager ftsm = new FinalTournamentSequenceManager();
			ftsm.start(input, pm, bm); 
		};
		new Thread(task2).start();

		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		while(true) {
			Message string = actualOutput.take();
			if(string.message == MESSAGETYPES.RANKUPDATE) break;
		}
		assertTrue(pm.players[0].getRank() == RANKS.KNIGHTOFTHEROUNDTABLE);
		assertTrue(pm.players[1].getRank() == RANKS.KNIGHTOFTHEROUNDTABLE);
		
		input.put(new TournamentPickCardsClient(0, new String[] {}));
		input.put(new TournamentPickCardsClient(1, new String[] {}));
		while(true) {
			Message string = actualOutput.take();
			if(string.message == MESSAGETYPES.GAMEOVER) {
				GameOverServer x = (GameOverServer) string;
				assertTrue(x.players[0] == 0 || x.players[0] == 1);
				assertTrue(x.players[1] == 0 || x.players[1] == 1);
				break;
			}
		}
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
		pm = new PlayerManager(2, dm, RIGGED.ONE);
		bm = new BoardModel();
		pv = new PlayerView(oc);
		pm.subscribe(pv);
		PlayersView pvs = new PlayersView(oc);
		pm.subscribe(pvs);
		pm.start();
		pm.nextTurn();
	}
}
