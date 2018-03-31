package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;
import com.qotrt.util.WebSocketUtil;

public abstract class Observer implements PropertyChangeListener {

	final static Logger logger = LogManager.getLogger(Observer.class);
	
	protected Observer() {}
	
	protected ObjectMapper mapper;
	protected ArrayList<GenericPairTyped<Function<PropertyChangeEvent, Boolean>, Consumer<PropertyChangeEvent>>> events = 
			new ArrayList<GenericPairTyped<Function<PropertyChangeEvent, Boolean>, Consumer<PropertyChangeEvent>>>();
	
	public Observer(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		this.mapper = new ObjectMapper();
		this.messagingTemplate = messagingTemplate;
		this.sendList = new ArrayList<UIPlayer>();
		players.forEach(i -> this.addWebSocket(i));
	}
	
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<UIPlayer> sendList;
	
	public void addWebSocket(UIPlayer player) {
		sendList.add(player);
	}
	
	protected void sendMessage(String destination, Object e) {
		logger.info("sending message: " + e + " to desitination: " + destination);

		sendList.forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(),
					"/queue/response",
					e,
					WebSocketUtil.createHeaders(i.getSessionID()));
		});
	}
	
	protected void handleEvent(PropertyChangeEvent x) {
		logger.info("event got fired: " + x.getPropertyName());
		
		events.stream().filter(i -> {
			return i.key.apply(x);
		}).forEach(i -> {
			i.value.accept(x);
		});

	}
	
}
