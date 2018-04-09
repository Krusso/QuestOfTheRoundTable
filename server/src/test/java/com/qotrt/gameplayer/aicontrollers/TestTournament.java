package com.qotrt.gameplayer.aicontrollers;

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
import com.qotrt.messages.tournament.TournamentAcceptDeclineClient;
import com.qotrt.messages.tournament.TournamentAcceptedDeclinedServer;
import com.qotrt.messages.tournament.TournamentFinishPickingClient;
import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;
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
	public void testAITournaments() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		

		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.sendMessage("/app/game.createGame", 
				new GameCreateClient(3, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {new AIPlayer(1, 1), new AIPlayer(2, 2)}, false));
		
		TournamentAcceptedDeclinedServer tadc = p.take(TournamentAcceptedDeclinedServer.class);
		if(tadc.player == 1) { assertEquals(false, tadc.joined); } else { assertEquals(true, tadc.joined); }
		
		tadc = p.take(TournamentAcceptedDeclinedServer.class);
		if(tadc.player == 1) { assertEquals(false, tadc.joined); } else { assertEquals(true, tadc.joined); }
		
		p.sendMessage("/app/game.joinTournament", new TournamentAcceptDeclineClient(0,true));
		
		p.waitForThenSend(TournamentPickCardsServer.class, 0, "/app/game.finishSelectingTournament", new TournamentFinishPickingClient(0));
		
		assertEquals(2,p.take(TournamentWinServer.class).players[0]);

	}
}
