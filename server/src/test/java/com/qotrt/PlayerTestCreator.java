package com.qotrt;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qotrt.config.MappingJackson2MessageConverter;
import com.qotrt.messages.Message;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.GameListServer;


public class PlayerTestCreator {

	final static Logger logger = LogManager.getLogger(PlayerTestCreator.class);
	
	protected LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	private StompSession stompSession;

	public StompSession connect(String WEBSOCKET_URI) {
		logger.info("starting");
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		logger.info("connecting to: " + WEBSOCKET_URI);
		try {
			stompSession = stompClient.connect(WEBSOCKET_URI, 
					new StompSessionHandlerAdapter() {}).get(1, SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.subscribe("/user/queue/response");

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stompSession;
	}

	public void subscribe(String destination) {
		//stompSession.subscribe(destination, new CreateGameStompFrameHandler());
		stompSession.subscribe(destination, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return Message.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				//String json = new String((byte[]) payload);
				logger.info("payload: " + payload);
				messages.add((Message)payload);
			}
		});
	}

	public void sendMessage(String destination, Object e) {
		logger.info("sending: " + e);
		logger.info("to: " + destination);
		stompSession.send(destination, e);
	}


	public <T> T take(Class<T> ca) {
		Message message1;
		try {
			while((message1 = messages.poll(20, SECONDS)) != null) {
				logger.info("checking type of: " + message1);
				logger.info("type match: " + ca.isInstance(message1));
				ObjectMapper mapper = new ObjectMapper();
				try {
					logger.info("returning: " + mapper.convertValue(message1, ca));
					return mapper.convertValue(message1, ca);
				} catch(IllegalArgumentException e) {
					logger.info("-------");
					logger.info(e.getMessage());
					logger.info(message1);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("couldnt find: " + ca);
		return null;
	}
	
	public <T extends Message> T take(Class<T> ca, int player) {
		while(true) {
			T tads = take(ca);
			logger.info("tads: " + tads);
			if(tads.player == player) { return tads; }
		}
	}
	

	private List<Transport> createTransportClient() {	
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		return transports;
	}

	public <T extends Message> void waitForThenSend(Class<T> class1, int player, String destination,
			Object objectToSend) {
		while(true) {
			T tads = take(class1);
			logger.info("tads: " + tads);
			if(tads.player == player) { break; }
		}

		sendMessage(destination, objectToSend);
	}
	
	public void joinGame() {
		sendMessage("/app/game.listGames", new GameListClient());
		GameListServer gls = take(GameListServer.class);
		assertEquals(1, gls.getGames().length);

		sendMessage("/app/game.joinGame", 
				new GameJoinClient(gls.getGames()[0].getUuid(), "world"));
	}

	public void sendMessageWithSleep(String string, Object e, int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		sendMessage(string, e);
		try {
			Thread.sleep(i);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
