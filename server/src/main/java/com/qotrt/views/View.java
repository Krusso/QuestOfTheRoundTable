package com.qotrt.views;

import java.util.Arrays;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MimeTypeUtils;

import com.qotrt.model.UIPlayer;

public abstract class View {

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
	}
	
	private SimpMessagingTemplate messagingTemplate;
	private UIPlayer[] sendList;
	
	protected void sendMessage(String destination, Object e) {
		Arrays.stream(sendList).forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(),
					"/queue/response",
					e,
					createHeaders(i.getSessionID()));
		});
	}
	
}
