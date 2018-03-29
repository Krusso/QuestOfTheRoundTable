package com.qotrt.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import com.qotrt.messages.events.EventDiscardCardsServer;
import com.qotrt.messages.events.EventDiscardFinishPickingClient;
import com.qotrt.messages.events.EventDiscardFinishPickingServer;
import com.qotrt.messages.events.EventDiscardOverServer;
import com.qotrt.messages.game.AIPlayer;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.model.RiggedModel.RIGGED;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestEventKingsCallToArms {

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
	public void testKingsCallToArms() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(2, "hello", RIGGED.KINGSCALLTOARMS, new AIPlayer[] {}));

				p.waitForThenSend(EventDiscardCardsServer.class, 0, "/app/game.discardEvent",
						new PlayCardClient(0, 170, ZONE.HAND, ZONE.DISCARD));
				p.take(PlayCardServer.class, 0);
				
				p.sendMessage("/app/game.discardEvent", 
						new PlayCardClient(0, 170, ZONE.DISCARD, ZONE.HAND));
				p.take(PlayCardServer.class, 0);
				
				p.sendMessage("/app/game.discardEvent", 
						new PlayCardClient(0, 170, ZONE.HAND, ZONE.DISCARD));
				p.take(PlayCardServer.class, 0);
				
				p.sendMessage("/app/game.discardEvent", 
						new PlayCardClient(0, 164, ZONE.HAND, ZONE.DISCARD));
				PlayCardServer pcs = p.take(PlayCardServer.class, 0);
				assertNotEquals("",pcs.response);
				
				p.sendMessage("/app/game.discardEvent", 
						new PlayCardClient(0, 165, ZONE.HAND, ZONE.DISCARD));
				pcs = p.take(PlayCardServer.class, 0);
				assertNotEquals("",pcs.response);
				
				p.sendMessage("/app/game.discardEvent", 
						new PlayCardClient(0, 163, ZONE.HAND, ZONE.DISCARD));
				pcs = p.take(PlayCardServer.class, 0);
				assertNotEquals("",pcs.response);
				
				p.sendMessage("/app/game.finishSelectingEvent", new EventDiscardFinishPickingClient(0));
			}
		};
		

		new Thread(player1).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		
		p.waitForThenSend(EventDiscardCardsServer.class, 1, 
				"/app/game.finishSelectingEvent", new EventDiscardFinishPickingClient(1));
		EventDiscardFinishPickingServer edfps = p.take(EventDiscardFinishPickingServer.class, 1);
		assertNotEquals("", edfps.response);
		assertEquals(false, edfps.successful);
		
		p.sendMessage("/app/game.discardEvent", 
				new PlayCardClient(1, 158, ZONE.HAND, ZONE.DISCARD));
		p.take(PlayCardServer.class, 1);
		
		p.sendMessage("/app/game.discardEvent", 
				new PlayCardClient(1, 158, ZONE.DISCARD, ZONE.HAND));
		p.take(PlayCardServer.class, 1);
		
		p.sendMessage("/app/game.discardEvent", 
				new PlayCardClient(1, 158, ZONE.HAND, ZONE.DISCARD));
		p.take(PlayCardServer.class, 1);
		
		p.sendMessage("/app/game.discardEvent", 
				new PlayCardClient(1, 152, ZONE.HAND, ZONE.DISCARD));
		PlayCardServer pcs = p.take(PlayCardServer.class, 1);
		assertNotEquals("",pcs.response);
		
		p.sendMessage("/app/game.discardEvent", 
				new PlayCardClient(1, 153, ZONE.HAND, ZONE.DISCARD));
		pcs = p.take(PlayCardServer.class, 1);
		assertNotEquals("",pcs.response);
		
		p.sendMessage("/app/game.discardEvent", 
				new PlayCardClient(1, 151, ZONE.HAND, ZONE.DISCARD));
		pcs = p.take(PlayCardServer.class, 1);
		assertNotEquals("",pcs.response);
		
		p.sendMessage("/app/game.finishSelectingEvent", new EventDiscardFinishPickingClient(1));
		
		assertNotNull(p.take(EventDiscardOverServer.class));
	}
}
