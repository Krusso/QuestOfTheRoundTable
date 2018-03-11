package com.qotrt.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.qotrt.hub.Hub;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.model.UIPlayer;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private Hub hub;

	@PostConstruct
	private void init() {
		//activeSessionManager.registerListener(this);
		System.out.println("created");
	}

	@PreDestroy
	private void destroy() {
		//activeSessionManager.removeListener(this);
		System.out.println("deleted");
	}

	@MessageMapping("/game.joinGame")
	public void joinChat(SimpMessageHeaderAccessor headerAccessor, @Payload GameJoinClient chatMessage) {
		String sessionID = headerAccessor.getSessionId();
		UIPlayer player = new UIPlayer(sessionID, chatMessage.getPlayerName());
		System.out.println("s is: " + sessionID);
		hub.setTemplate(messagingTemplate);
		hub.addPlayer(player);
	}
}