package com.qotrt.views;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.Arrays;
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
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.model.RiggedModel.RIGGED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestPlayerView {

	WebSocketStompClient stompClient;
	final static Logger logger = LogManager.getLogger(TestPlayerView.class);
	@Value("${local.server.port}")
	private int port;
	static String WEBSOCKET_URI;

	@Before
	public void setup() {
		WEBSOCKET_URI = "ws://localhost:" + port + "/ws";
	}

	@Test
	public void testCreateGameEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", new GameCreateClient(2, "hello", RIGGED.AITOURNAMENT, new AIPlayer[] {}, false));
			}
		};

		new Thread(thread2).start();
		Thread.sleep(1000);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		GameListClient glc = new GameListClient();
		p.sendMessage("/app/game.listGames", glc);
		GameListServer gls = p.take(GameListServer.class);
		GameJoinClient gjc = new GameJoinClient();
		gjc.setPlayerName("world");
		gjc.setUuid(gls.getGames()[0].getUuid());
		assertEquals(1, gls.getGames().length);

		p.sendMessage("/app/game.joinGame", gjc);

		AddCardsServer ads = p.take(AddCardsServer.class);
		assertEquals(12, ads.getCards().length);
		logger.info("Cards: " + Arrays.toString(ads.getCards()));
	}


}
