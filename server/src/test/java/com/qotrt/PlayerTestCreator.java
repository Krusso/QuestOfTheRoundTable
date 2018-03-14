package com.qotrt;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qotrt.config.MappingJackson2MessageConverter;
import com.qotrt.messages.Message;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QotrtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// used to restart spring application after every test
// might be able to remove later
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerTestCreator {

	protected LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	@Value("${local.server.port}")
	private int port;
	static String WEBSOCKET_URI;

	@Before
	public void setup() {
		WEBSOCKET_URI = "ws://localhost:" + port + "/ws";
	}

	public StompSession connect() throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println("starting");
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		//stompClient.setMessageConverter(new StringMessageConverter());

		System.out.println("connecting to: " + WEBSOCKET_URI);
		StompSession stompSession = stompClient.connect(WEBSOCKET_URI, 
				new StompSessionHandlerAdapter() {}).get(1, SECONDS);

		this.subscribe(stompSession, "/user/queue/response");

		Thread.sleep(200);
		//JmsOperations jmsOperations;
		
		return stompSession;
	}

	public void subscribe(StompSession stompSession, String destination) {
		//stompSession.subscribe(destination, new CreateGameStompFrameHandler());
		stompSession.subscribe(destination, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return Message.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				//String json = new String((byte[]) payload);
				System.out.println("payload: " + payload);
				messages.add((Message)payload);
			}
		});
	}

	public void sendMessage(StompSession stompSession, String destination, Object e) {
		System.out.println("sending: " + e);
		System.out.println("to: " + destination);
		stompSession.send(destination, e);
	}


	@SuppressWarnings("unchecked")
	public <T> T take(Class<T> ca) throws InterruptedException {
		Message message1;
		while((message1 = messages.poll(2, SECONDS)) != null) {
			System.out.println("checking type of: " + message1);
			System.out.println("type match: " + ca.isInstance(message1));
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.convertValue(message1, ca);
			} catch(IllegalArgumentException e) {
				System.out.println("-------");
				System.out.println(e.getMessage());
				System.out.println(message1);
			}
			//T value = mapper.readValue(new String(message1), ca);
			//return (T) message1;
			//				if(ca.isInstance(value)) {
			//					return value;
			//				}
		}

		System.out.println("couldnt find: " + ca);
		return null;
	}

	private List<Transport> createTransportClient() {	
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		//		RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		//		transports.add(xhrTransport);

		return transports;
	}

	private class CreateGameStompFrameHandler extends StompSessionHandlerAdapter {

		@Override
		public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
			System.out.println("error1");
		}

		@Override
		public void handleTransportError(StompSession session, Throwable ex) {
			System.out.println("error2");
		}

		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return Object.class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			System.out.println("payload: " + o);
			//messages.add(o);
		}
	}
}
