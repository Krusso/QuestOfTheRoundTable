// TODO
//	@Test
//	public void testMordredDiscard() throws InterruptedException {
//		TournamentSequenceManager tsm = new TournamentSequenceManager(new TournamentCard("Tournament at Camelot", 3));
//		QOTRTQueue input = new QOTRTQueue();
//		input.setPlayerManager(pm);
//		Runnable task2 = () -> { tsm.start(input, pm, bm); };
//		new Thread(task2).start();
//		
//		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
//		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
//		cards.add(new FoeCard("Mordred",30, TYPE.FOES));
//		Iterator<Player> x = pm.round();
//		x.next().addCards(cards);
//		x.next().addCards(cards);
//		
//		pm.round().next().setFaceDown(new String[] {"Sir Tristan"});
//		pm.round().next().flipCards();
//		assertEquals(1, pm.players[0].getFaceUp().size());
//		assertEquals("Sir Tristan", pm.players[0].getFaceUp().getDeck().get(0).getName());
//		
//		input.put(new MordredClient(1, 0, "Sir Tristan"));
//		
//		Thread.sleep(100);
//		assertEquals(0, pm.players[0].getFaceUp().size());
//		
//	}

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
import com.qotrt.messages.discard.HandFullFinishPickingClient;
import com.qotrt.messages.discard.HandFullFinishPickingServer;
import com.qotrt.messages.discard.HandFullServer;
import com.qotrt.messages.game.AIPlayer;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.model.RiggedModel.RIGGED;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestSpecialInteractions {

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
	public void discardAmourAlliesWhenHandFull() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(2, "hello", RIGGED.PROSPERITY, new AIPlayer[] {}, true));
				
				HandFullServer hfs = p.take(HandFullServer.class, 0);
				assertEquals(2, hfs.toDiscard);
				assertEquals(0, hfs.player);
			}
		};
		

		new Thread(player1).start();
		Thread.sleep(1000);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		HandFullServer hfs = p.take(HandFullServer.class, 1);
		assertEquals(2, hfs.toDiscard);
		assertEquals(1, hfs.player);
		
		p.sendMessage("/app/game.discardFullHand", new PlayCardClient(1, 139, ZONE.HAND, ZONE.FACEDOWN));
		PlayCardServer pcs = p.take(PlayCardServer.class, 1);
		assertEquals("", pcs.response);
		
		p.sendMessage("/app/game.discardFullHand", new PlayCardClient(1, 30, ZONE.HAND, ZONE.FACEDOWN));
		pcs = p.take(PlayCardServer.class, 1);
		assertNotEquals("", pcs.response);
		
		p.sendMessage("/app/game.finishSelectingDiscardHand", new HandFullFinishPickingClient(1));
		HandFullFinishPickingServer hffps = p.take(HandFullFinishPickingServer.class, 1);
		assertNotEquals("", hffps.response);
		
		p.sendMessage("/app/game.discardFullHand", new PlayCardClient(1, 135, ZONE.HAND, ZONE.FACEDOWN));
		pcs = p.take(PlayCardServer.class, 1);
		assertEquals("", pcs.response);
		
		p.sendMessage("/app/game.finishSelectingDiscardHand", new HandFullFinishPickingClient(1));
		hffps = p.take(HandFullFinishPickingServer.class, 1);
		assertEquals("", hffps.response);
	}
	
	@Test
	public void testDiscardWhenHandFull() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable player1 = new Runnable() {
			@Override
			public void run() {
				PlayerTestCreator p = new PlayerTestCreator();
				p.connect(WEBSOCKET_URI);
				p.sendMessage("/app/game.createGame", 
						new GameCreateClient(2, "hello", RIGGED.PROSPERITY, new AIPlayer[] {}, true));
				
				HandFullServer hfs = p.take(HandFullServer.class, 0);
				assertEquals(2, hfs.toDiscard);
				assertEquals(0, hfs.player);
				
				p.sendMessage("/app/game.discardFullHand", new PlayCardClient(0, 114, ZONE.HAND, ZONE.DISCARD));
				PlayCardServer pcs = p.take(PlayCardServer.class, 0);
				assertEquals("", pcs.response);
				
				p.sendMessage("/app/game.discardFullHand", new PlayCardClient(0, 75, ZONE.HAND, ZONE.DISCARD));
				pcs = p.take(PlayCardServer.class, 0);
				assertEquals("", pcs.response);
				
				p.sendMessage("/app/game.finishSelectingDiscardHand", new HandFullFinishPickingClient(0));
				HandFullFinishPickingServer hffps = p.take(HandFullFinishPickingServer.class, 0);
				assertEquals("", hffps.response);
			}
		};
		

		new Thread(player1).start();
		Thread.sleep(500);
		PlayerTestCreator p = new PlayerTestCreator();
		p.connect(WEBSOCKET_URI);
		p.joinGame();
		
		HandFullServer hfs = p.take(HandFullServer.class, 1);
		assertEquals(2, hfs.toDiscard);
		assertEquals(1, hfs.player);
		
		p.sendMessage("/app/game.discardFullHand", new PlayCardClient(1, 24, ZONE.HAND, ZONE.DISCARD));
		PlayCardServer pcs = p.take(PlayCardServer.class, 1);
		assertEquals("", pcs.response);
		
		p.sendMessage("/app/game.discardFullHand", new PlayCardClient(1, 30, ZONE.HAND, ZONE.DISCARD));
		pcs = p.take(PlayCardServer.class, 1);
		assertEquals("", pcs.response);
		
		p.sendMessage("/app/game.finishSelectingDiscardHand", new HandFullFinishPickingClient(1));
		HandFullFinishPickingServer hffps = p.take(HandFullFinishPickingServer.class, 1);
		assertEquals("", hffps.response);
		
		MiddleCardServer mcs = p.take(MiddleCardServer.class);
		assertNotNull(mcs);
	}

}
