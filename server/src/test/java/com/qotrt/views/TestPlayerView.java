package com.qotrt.views;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.qotrt.PlayerTestCreator;
import com.qotrt.QotrtApplicationTests;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.messages.hand.AddCardsServer;


public class TestPlayerView extends PlayerTestCreator {

	WebSocketStompClient stompClient;
	private static Log logger = LogFactory.getLog(QotrtApplicationTests.class);
	
    
	@Test
	public void testCreateGameEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
				try {
					StompSession ss = connect();
					GameCreateClient gcc = new GameCreateClient();
					gcc.setNumPlayers(2);
					gcc.setPlayerName("hello");
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
		
		AddCardsServer ads = take(AddCardsServer.class);
		assertEquals(12, ads.getCards().length);
		System.out.println("Cards: " + Arrays.toString(ads.getCards()));
	}
	

}
