package com.qotrt.sequence;

import static org.junit.Assert.assertEquals;

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
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.messages.gameover.FinalTournamentFinishPickingClient;
import com.qotrt.messages.gameover.FinalTournamentNotifyServer;
import com.qotrt.messages.gameover.GameOverServer;
import com.qotrt.model.RiggedModel.RIGGED;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestFinalTournament {

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
	public void testOneWinnerFinalTournament() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(2, "hello", RIGGED.GAMEEND, new AIPlayer[] {}, false, true));

				p.take(FinalTournamentNotifyServer.class);
				p.sendMessage("/app/game.finishSelectingFinalTournament", new FinalTournamentFinishPickingClient(0));
			}
		};
		

		new Thread(player1).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		FinalTournamentNotifyServer ftns = p.take(FinalTournamentNotifyServer.class);
		assertEquals(0, ftns.players[0]);
		assertEquals(1, ftns.players[1]);
		assertEquals(2, ftns.players.length);
		
		p.sendMessage("/app/game.playForFinalTournament", new PlayCardClient(1, 139, ZONE.HAND, ZONE.FACEDOWN));
		PlayCardServer pcs = p.take(PlayCardServer.class, 1);
		assertEquals("",pcs.response);
		
		p.sendMessage("/app/game.finishSelectingFinalTournament", new FinalTournamentFinishPickingClient(1));
		
		GameOverServer gos = p.take(GameOverServer.class);
		assertEquals(1, gos.players[0]);
		assertEquals(1, gos.players.length);
	}
	
	@Test
	public void testDrawFinalTournament() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(2, "hello", RIGGED.GAMEEND, new AIPlayer[] {}, false, true));

				p.take(FinalTournamentNotifyServer.class);
				p.sendMessage("/app/game.finishSelectingFinalTournament", new FinalTournamentFinishPickingClient(0));
			}
		};
		

		new Thread(player1).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		FinalTournamentNotifyServer ftns = p.take(FinalTournamentNotifyServer.class);
		assertEquals(0, ftns.players[0]);
		assertEquals(1, ftns.players[1]);
		assertEquals(2, ftns.players.length);
		
		p.sendMessage("/app/game.finishSelectingFinalTournament", new FinalTournamentFinishPickingClient(1));
		
		GameOverServer gos = p.take(GameOverServer.class);
		assertEquals(0, gos.players[0]);
		assertEquals(1, gos.players[1]);
		assertEquals(2, gos.players.length);
	}
}
