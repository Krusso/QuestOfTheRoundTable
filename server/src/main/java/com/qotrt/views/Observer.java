package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qotrt.model.UIPlayer;
import com.qotrt.util.WebSocketUtil;

public abstract class Observer extends EventHandler implements PropertyChangeListener  {

	final static Logger logger = LogManager.getLogger(Observer.class);
	
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<UIPlayer> sendList;
	
	protected Observer() {}
	
	protected ObjectMapper mapper;

	public Observer(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		this.mapper = new ObjectMapper();
		this.messagingTemplate = messagingTemplate;
		this.sendList = new ArrayList<UIPlayer>();
		players.forEach(i -> this.addWebSocket(i));
	}
	
	public void addWebSocket(UIPlayer player) {
		sendList.add(player);
	}
	
	protected void sendMessage(Object e) {
		logger.info("sending message: " + e + " to desitination: /queue/response");

		sendList.forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(),
					"/queue/response",
					e,
					WebSocketUtil.createHeaders(i.getSessionID()));
		});
	}
	
	protected void handleEvent(PropertyChangeEvent x) {
		logger.info("event got fired: " + x.getPropertyName());
		
		handlePropertyChangeEvent(x);
	}
	
}
