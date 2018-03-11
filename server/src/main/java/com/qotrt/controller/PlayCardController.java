package com.qotrt.controller;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.qotrt.hub.Hub;
import com.qotrt.messages.game.GameJoinClient;

@Controller
public class PlayCardController {

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

	@MessageMapping("/game.playCard")
	public void joinChat(SimpMessageHeaderAccessor headerAccessor, @Payload GameJoinClient chatMessage) {
		String s = headerAccessor.getSessionId();
		System.out.println("in here");
		System.out.println("s is: " + s);
		System.out.println("out of here");
		//return new GameJoinServer(players);
	}
}