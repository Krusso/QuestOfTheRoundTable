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
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.messages.quest.QuestSponsorClient;
import com.qotrt.messages.quest.QuestSponsorServer;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.tournament.TournamentAcceptDeclineClient;
import com.qotrt.messages.tournament.TournamentAcceptDeclineServer;
import com.qotrt.messages.tournament.TournamentFinishPickingClient;
import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;
import com.qotrt.model.RiggedModel.RIGGED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestQuest {

	WebSocketStompClient stompClient;
	final static Logger logger = LogManager.getLogger(TestQuest.class);
	@Value("${local.server.port}")
	private int port;
	static String WEBSOCKET_URI;

	@Before
	public void setup() {
		WEBSOCKET_URI = "ws://localhost:" + port + "/ws";
	}

	// testNoParticipants
	// testDiscardForQuests
	// testCantSponsorQuest
	// test2PlayerBidding
	// testBidding
	// testFightingFoe
	// testDontNeedToBidAnything
	// testKingRecognition
	// testNoBids
	
	
	@Test
	public void testNoSponsor() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
					PlayerTestCreator p = new PlayerTestCreator();
					p.connect(WEBSOCKET_URI);
					p.sendMessage("/app/game.createGame", 
							new GameCreateClient(2, "hello", RIGGED.ONESTAGETOURNAMENT));

					p.waitForThenSend(QuestSponsorServer.class, 0,
							"/app/game.sponsorQuest", new QuestSponsorClient(0, false));
			}
		};
		
		new Thread(thread2).start();
		Thread.sleep(1000);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		p.waitForThenSend(QuestSponsorServer.class, 1,
				"/app/game.sponsorQuest", new QuestSponsorClient(1, false));
		
		p.take(QuestSpons)
	}


}
