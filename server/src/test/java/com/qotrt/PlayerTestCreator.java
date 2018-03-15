package com.qotrt;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

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


public class PlayerTestCreator {

	protected LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	private StompSession stompSession;
	
	public StompSession connect(String WEBSOCKET_URI) throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println("starting");
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		//stompClient.setMessageConverter(new StringMessageConverter());

		System.out.println("connecting to: " + WEBSOCKET_URI);
		stompSession = stompClient.connect(WEBSOCKET_URI, 
				new StompSessionHandlerAdapter() {}).get(1, SECONDS);

		this.subscribe("/user/queue/response");

		Thread.sleep(200);
		//JmsOperations jmsOperations;
		
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
				System.out.println("payload: " + payload);
				messages.add((Message)payload);
			}
		});
	}

	public void sendMessage(String destination, Object e) {
		System.out.println("sending: " + e);
		System.out.println("to: " + destination);
		stompSession.send(destination, e);
	}


	public <T> T take(Class<T> ca) throws InterruptedException {
		Message message1;
		while((message1 = messages.poll(10, SECONDS)) != null) {
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
		}

		System.out.println("couldnt find: " + ca);
		return null;
	}

	private List<Transport> createTransportClient() {	
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		return transports;
	}
}
