package com.qotrt;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
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

import com.qotrt.messages.Message;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameJoinServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class QotrtApplicationTests {
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

//		final AtomicReference<Throwable> failure = new AtomicReference<>();
//		
//		StompSessionHandler handler = new AbstractTestSessionHandler(failure) {
//
//			@Override
//			public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
//				session.subscribe("/user/queue/response", new StompFrameHandler() {
//					@Override
//					public Type getPayloadType(StompHeaders headers) {
//						logger.info("Wow?");
//						System.out.println(headers.getDestination());
//						System.out.println(headers.getId());
//						System.out.println(headers.getSession());
//						System.out.println(headers.get("simpSessionId"));
//						System.out.println(headers.get("hello"));
//						return GameJoinServer.class;
//					}
//
//					@Override
//					public void handleFrame(StompHeaders headers, Object payload) {
//						//String json = ((String) payload);
//						logger.debug("Got " + payload);
//						System.out.println("payload: " + payload);
//						//assertEquals(0, -1);
////						try {
////							
////						}
////						catch (Throwable t) {
////							//System.out.println("failure");
////						}
//					}
//				});
//				
//				
//				GameJoinClient gjc = new GameJoinClient();
//				gjc.setPlayerName(username);
//				session.send("/app/game.joinGame", gjc);
//			}
//		};
		
		
		
		
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.add("login", "roar");
		
		//stompClient.connect(WEBSOCKET_URI, handler, headers);
		
		StompSession stompSession = stompClient.connect(WEBSOCKET_URI, 
				new StompSessionHandlerAdapter() {}).get(1, SECONDS);

		//stompSession.subscribe("/topic/public", new CreateGameStompFrameHandler());
		stompSession.subscribe("/user/queue/response", new CreateGameStompFrameHandler());
		GameJoinClient gjc = new GameJoinClient();
		gjc.setPlayerName(username);
		stompSession.send("/app/game.joinGame", gjc);
		
		Thread.sleep(1000);
		
	}

	private List<Transport> createTransportClient() {
//		List<Transport> transports = new ArrayList<>(1);
//		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		transports.add(xhrTransport);
		
		return transports;
	}

	private class CreateGameStompFrameHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			System.out.println("recieved anything?");
			System.out.println(stompHeaders.getDestination());
			System.out.println(stompHeaders.getId());
			System.out.println(stompHeaders.getSession());
			System.out.println(stompHeaders.get("simpSessionId"));
			System.out.println(stompHeaders.get("hello"));
			return GameJoinServer.class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			System.out.println("here");
			System.out.println("payload: " + o);
			//System.out.println("handled?: " + Arrays.toString(((GameJoinServer) o).players));
			//messages.add((GameJoinServer) o);
		}
	}

	// might be useful later
//	class DefaultStompFrameHandler implements StompFrameHandler {
//        @Override
//        public Type getPayloadType(StompHeaders stompHeaders) {
//            return byte[].class;
//        }
//
//        @Override
//        public void handleFrame(StompHeaders stompHeaders, Object o) {
//            blockingQueue.offer(new String((byte[]) o));
//        }
//    }

	private static abstract class AbstractTestSessionHandler extends StompSessionHandlerAdapter {

		private final AtomicReference<Throwable> failure;


		public AbstractTestSessionHandler(AtomicReference<Throwable> failure) {
			this.failure = failure;
		}

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			logger.error("STOMP ERROR frame: " + headers.toString());
			this.failure.set(new Exception(headers.toString()));
		}

		@Override
		public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
			logger.error("Handler exception", ex);
			this.failure.set(ex);
		}

		@Override
		public void handleTransportError(StompSession session, Throwable ex) {
			logger.error("Transport failure", ex);
			this.failure.set(null);
			//this.failure.set(ex);
		}
	}
	
}
