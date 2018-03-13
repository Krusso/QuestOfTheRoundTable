package com.qotrt.views;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.qotrt.QotrtApplication;
import com.qotrt.QotrtApplicationTests;
import com.qotrt.messages.Message;
import com.qotrt.messages.game.GameJoinClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestPlayerView {
	@Value("${local.server.port}")
    private int port;
	WebSocketStompClient stompClient;
	static String WEBSOCKET_URI;
	private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	private static Log logger = LogFactory.getLog(QotrtApplicationTests.class);
	
    @Before
    public void setup() {
        WEBSOCKET_URI = "ws://localhost:" + port + "/ws";
    }
    
	@Test
	public void testCreateGameEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
		Runnable thread2 = new Runnable() {
			@Override
			public void run() {
				try {
					connect("hello");
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					e.printStackTrace();
				}
			}
		};
		
		new Thread(thread2).start();
		connect("world");
		Thread.sleep(1000);
		Message message1;
		while((message1 = messages.poll(1, SECONDS)) != null) {
			System.out.println(message1);
		}
	}
	
	public void connect(String username) throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println("starting");
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.add("login", "roar");
		
		StompSession stompSession = stompClient.connect(WEBSOCKET_URI, 
				new StompSessionHandlerAdapter() {}).get(1, SECONDS);

		stompSession.subscribe("/user/queue/response", new CreateGameStompFrameHandler());
		GameJoinClient gjc = new GameJoinClient();
		gjc.setPlayerName(username);
		stompSession.send("/app/game.joinGame", gjc);
		
		Thread.sleep(1000);
		
	}

	private List<Transport> createTransportClient() {	
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		transports.add(xhrTransport);
		
		return transports;
	}

	private class CreateGameStompFrameHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return Object.class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			System.out.println("here");
			System.out.println("payload: " + o);
		}
	}
	
}
