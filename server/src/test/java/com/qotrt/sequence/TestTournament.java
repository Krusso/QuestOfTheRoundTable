package com.qotrt.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.qotrt.PlayerTestCreator;
import com.qotrt.QotrtApplication;
import com.qotrt.messages.game.AIPlayer;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.messages.hand.FaceUpDiscardServer;
import com.qotrt.messages.hand.FaceUpServer;
import com.qotrt.messages.tournament.TournamentAcceptDeclineClient;
import com.qotrt.messages.tournament.TournamentAcceptDeclineServer;
import com.qotrt.messages.tournament.TournamentFinishPickingClient;
import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;
import com.qotrt.messages.tournament.TournamentWinServer.WINTYPES;
import com.qotrt.model.RiggedModel.RIGGED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestTournament {

	WebSocketStompClient stompClient;
	final static Logger logger = LogManager.getLogger(TestTournament.class);
	@Value("${local.server.port}")
	private int port;
	static String WEBSOCKET_URI;

	@Before
	public void setup() {
		WEBSOCKET_URI = "ws://localhost:" + port + "/ws";
	}

	@Test
	public void testWinSecondRound() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(4, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {}, false));

				p.waitForThenSend(TournamentAcceptDeclineServer.class, 0, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(0, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 0,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(0));
				
				p.waitForThenSend(TournamentPickCardsServer.class, 0,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(0));
			}
		};
		
		Runnable player2 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.joinGame();
				p.waitForThenSend(TournamentAcceptDeclineServer.class, 1, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(1, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 1,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(1));
				
				p.waitForThenSend(TournamentPickCardsServer.class, 1,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(1));
			}
		};
		
		Runnable player3 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.joinGame();
				p.waitForThenSend(TournamentAcceptDeclineServer.class, 2, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(2, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 2,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(2));
				
				p.waitForThenSend(TournamentPickCardsServer.class, 2,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(2));
			}
		};

		new Thread(player1).start();
		Thread.sleep(500);
		new Thread(player2).start();
		Thread.sleep(500);
		new Thread(player3).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		p.waitForThenSend(TournamentAcceptDeclineServer.class, 3, 
				"/app/game.joinTournament", new TournamentAcceptDeclineClient(3, true));
		p.waitForThenSend(TournamentPickCardsServer.class, 3,
				"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(3));
		
		FaceUpServer fus = p.take(FaceUpServer.class);
		assertNotNull(fus);
		
		TournamentWinServer tws = p.take(TournamentWinServer.class);
		assertEquals(4, tws.players.length);
		assertEquals(WINTYPES.TIE, tws.type);
		
		
		p.waitForThenSend(TournamentPickCardsServer.class, 1,
				"/app/game.playCardTournament", new PlayCardClient(3, 25, ZONE.HAND, ZONE.FACEDOWN));
		p.sendMessageWithSleep("/app/game.playCardTournament", new PlayCardClient(3, 31, ZONE.HAND, ZONE.FACEDOWN), 20);
		p.sendMessage("/app/game.finishSelectingTournament", new TournamentFinishPickingClient(3));
		
		FaceUpDiscardServer fuds = p.take(FaceUpDiscardServer.class, 3);
		assertEquals(25, fuds.cards[0].value);
		assertEquals(31, fuds.cards[1].value);
		
		tws = p.take(TournamentWinServer.class);
		assertEquals(1, tws.players.length);
		assertEquals(3, tws.players[0]);
		assertEquals(WINTYPES.WON, tws.type);
	}
	
	@Test
	public void testDrawSecondRound() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(4, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {}, false));

				p.waitForThenSend(TournamentAcceptDeclineServer.class, 0, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(0, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 0,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(0));
				
				p.waitForThenSend(TournamentPickCardsServer.class, 0,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(0));
			}
		};
		
		Runnable player2 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.joinGame();
				p.waitForThenSend(TournamentAcceptDeclineServer.class, 1, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(1, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 1,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(1));
				
				p.waitForThenSend(TournamentPickCardsServer.class, 1,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(1));
			}
		};
		
		Runnable player3 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.joinGame();
				p.waitForThenSend(TournamentAcceptDeclineServer.class, 2, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(2, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 2,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(2));
				
				p.waitForThenSend(TournamentPickCardsServer.class, 2,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(2));
			}
		};

		new Thread(player1).start();
		Thread.sleep(500);
		new Thread(player2).start();
		Thread.sleep(500);
		new Thread(player3).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		p.waitForThenSend(TournamentAcceptDeclineServer.class, 3, 
				"/app/game.joinTournament", new TournamentAcceptDeclineClient(3, true));
		p.waitForThenSend(TournamentPickCardsServer.class, 3,
				"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(3));
		TournamentWinServer tws = p.take(TournamentWinServer.class);
		assertEquals(4, tws.players.length);
		assertEquals(WINTYPES.TIE, tws.type);
		
		p.waitForThenSend(TournamentPickCardsServer.class, 3,
				"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(3));
		tws = p.take(TournamentWinServer.class);
		assertEquals(4, tws.players.length);
		assertEquals(WINTYPES.WON, tws.type);
	}

	@Test
	public void testFourPlayer() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(4, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {}, false));

				p.waitForThenSend(TournamentAcceptDeclineServer.class, 0, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(0, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 0,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(0));
			}
		};
		
		Runnable player2 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.joinGame();
				p.waitForThenSend(TournamentAcceptDeclineServer.class, 1, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(1, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 1,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(1));
			}
		};
		
		Runnable player3 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.joinGame();
				p.waitForThenSend(TournamentAcceptDeclineServer.class, 2, 
						"/app/game.joinTournament", new TournamentAcceptDeclineClient(2, true));
				p.waitForThenSend(TournamentPickCardsServer.class, 2,
						"/app/game.finishSelectingTournament", new TournamentFinishPickingClient(2));
			}
		};

		new Thread(player1).start();
		Thread.sleep(500);
		new Thread(player2).start();
		Thread.sleep(500);
		new Thread(player3).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		p.waitForThenSend(TournamentAcceptDeclineServer.class, 3, 
				"/app/game.joinTournament", new TournamentAcceptDeclineClient(3, true));
		p.waitForThenSend(TournamentPickCardsServer.class, 1,
				"/app/game.playCardTournament", new PlayCardClient(3, 25, ZONE.HAND, ZONE.FACEDOWN));
		p.sendMessageWithSleep("/app/game.playCardTournament", new PlayCardClient(3, 31, ZONE.HAND, ZONE.FACEDOWN), 20);
		p.sendMessageWithSleep("/app/game.playCardTournament", new PlayCardClient(3, 114, ZONE.HAND, ZONE.FACEDOWN), 20);
		PlayCardServer pcs = p.take(PlayCardServer.class);
		assertEquals(25, pcs.card);
		pcs = p.take(PlayCardServer.class);
		assertEquals(31, pcs.card);
		pcs = p.take(PlayCardServer.class);
		assertEquals("Cant play foe face down", pcs.response);
		p.sendMessage("/app/game.finishSelectingTournament", new TournamentFinishPickingClient(3));
		TournamentWinServer tws = p.take(TournamentWinServer.class);
		assertEquals(3, tws.players[0]);
		assertEquals(1, tws.players.length);
		assertEquals(WINTYPES.WON, tws.type);
	}

	@Test
	public void testOnePlayer() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(2, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {}, false));

				while(true) {
					TournamentAcceptDeclineServer tads = p.take(TournamentAcceptDeclineServer.class);
					if(tads.player == 0) { break; }
				}

				p.sendMessage("/app/game.joinTournament", 
						new TournamentAcceptDeclineClient(0, false));
			}
		};

		new Thread(thread2).start();
		Thread.sleep(1000);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.sendMessage("/app/game.listGames", new GameListClient());

		GameListServer gls = p.take(GameListServer.class);
		assertEquals(1, gls.getGames().length);

		p.sendMessage("/app/game.joinGame", 
				new GameJoinClient(gls.getGames()[0].getUuid(), "world"));


		MiddleCardServer mcs = p.take(MiddleCardServer.class);
		assertEquals("Tournament at York", mcs.card);

		while(true) {
			TournamentAcceptDeclineServer tads = p.take(TournamentAcceptDeclineServer.class);
			if(tads.player == 1) { break; }
		}

		TournamentAcceptDeclineClient tadc = new TournamentAcceptDeclineClient(1, true);
		p.sendMessage("/app/game.joinTournament", tadc);

		TournamentWinServer tws = p.take(TournamentWinServer.class);
		assertEquals(WINTYPES.ONEJOIN, tws.type);
		assertEquals(1, tws.players.length);
		assertEquals(1, tws.players[0]);
	}

	@Test
	public void testNoOneEnter() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
					PlayerTestCreator p = new PlayerTestCreator();
					p.connect(WEBSOCKET_URI);
					p.sendMessage("/app/game.createGame", 
							new GameCreateClient(2, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {}, false));

					while(true) {
						TournamentAcceptDeclineServer tads = p.take(TournamentAcceptDeclineServer.class);
						if(tads.player == 0) { break; }
					}

					p.sendMessage("/app/game.joinTournament", 
							new TournamentAcceptDeclineClient(0, false));
			}
		};

		new Thread(thread2).start();
		Thread.sleep(1000);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.sendMessage("/app/game.listGames", new GameListClient());

		GameListServer gls = p.take(GameListServer.class);
		assertEquals(1, gls.getGames().length);

		p.sendMessage("/app/game.joinGame", 
				new GameJoinClient(gls.getGames()[0].getUuid(), "world"));


		MiddleCardServer mcs = p.take(MiddleCardServer.class);
		assertEquals("Tournament at York", mcs.card);

		while(true) {
			TournamentAcceptDeclineServer tads = p.take(TournamentAcceptDeclineServer.class);
			if(tads.player == 1) { break; }
		}

		TournamentAcceptDeclineClient tadc = new TournamentAcceptDeclineClient(1, false);
		p.sendMessage("/app/game.joinTournament", tadc);

		TournamentWinServer tws = p.take(TournamentWinServer.class);
		assertEquals(WINTYPES.NOJOIN, tws.type);
		assertEquals(0, tws.players.length);
	}


}
