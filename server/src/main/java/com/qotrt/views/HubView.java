package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MimeTypeUtils;

import com.qotrt.messages.game.GameJoinServer;
import com.qotrt.model.UIPlayer;

public class HubView implements PropertyChangeListener {
	
    private SimpMessagingTemplate messagingTemplate;
	
	public HubView(SimpMessagingTemplate messagingTemplate2) {
		this.messagingTemplate = messagingTemplate2;
	}

	
	private MessageHeaders createHeaders(String sessionId) {
	    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	    headerAccessor.setSessionId(sessionId);
	    headerAccessor.setLeaveMutable(true);
	    headerAccessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
	    System.out.println(headerAccessor.getMessageHeaders());
	    return headerAccessor.getMessageHeaders();
	}
	
	public void playerJoinedGame(UIPlayer[] UIPlayers) {
		System.out.println("sending message: " + Arrays.toString(UIPlayers));
		GameJoinServer gjs = new GameJoinServer();
		gjs.setPlayers(Arrays.stream(UIPlayers).map(i -> i.getPlayerName()).toArray(String[]::new));
		
		Arrays.stream(UIPlayers).forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(),
					"/queue/response",
					gjs,
					createHeaders(i.getSessionID()));
		});
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("players")) {
			playerJoinedGame((UIPlayer[]) evt.getNewValue());
		}
	}
}