package com.qotrt.sequence;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.qotrt.PlayerTestCreator;
import com.qotrt.QotrtApplicationTests;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.model.RiggedModel.RIGGED;


public class TestTournament extends PlayerTestCreator {

	WebSocketStompClient stompClient;
	final static Logger logger = LogManager.getLogger(TestTournament.class);
	
    
	@Test
	public void testTournament() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
				try {
					StompSession ss = connect();
					GameCreateClient gcc = new GameCreateClient();
					gcc.setNumPlayers(2);
					gcc.setPlayerName("hello");
					gcc.setRigged(RIGGED.AITOURNAMENT);
					sendMessage(ss, "/app/game.createGame", gcc);
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					e.printStackTrace();
				}
			}
		};
		
		new Thread(thread2).start();
		Thread.sleep(1000);
		StompSession ss = connect();
		GameListClient glc = new GameListClient();
		sendMessage(ss, "/app/game.listGames", glc);
		GameListServer gls = take(GameListServer.class);
		GameJoinClient gjc = new GameJoinClient();
		gjc.setPlayerName("world");
		gjc.setUuid(gls.getGames()[0].getUuid());
		assertEquals(1, gls.getGames().length);
		
		sendMessage(ss, "/app/game.joinGame", gjc);


		MiddleCardServer mcs = take(MiddleCardServer.class);
		assertEquals("Tournament at York", mcs.card);
	}
	

}
