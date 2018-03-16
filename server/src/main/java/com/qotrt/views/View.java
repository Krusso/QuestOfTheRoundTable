package com.qotrt.views;

import java.util.ArrayList;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MimeTypeUtils;

import com.qotrt.model.UIPlayer;

public abstract class View {

	
	// TODO: used in multiple places move to util class
	private MessageHeaders createHeaders(String sessionId) {
	    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	    headerAccessor.setSessionId(sessionId);
	    headerAccessor.setLeaveMutable(true);
	    headerAccessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
	    return headerAccessor.getMessageHeaders();
	}
	
	protected View() {}
	
	public View(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.sendList = new ArrayList<UIPlayer>();
	}
	
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<UIPlayer> sendList;
	
	public void addWebSocket(UIPlayer player) {
		sendList.add(player);
	}
	
	protected void sendMessage(String destination, Object e) {
		System.out.println("sending message: " + e + " to desitination: " + destination);
		
		sendList.forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(),
					"/queue/response",
					e,
					createHeaders(i.getSessionID()));
		});
	}
	
}
