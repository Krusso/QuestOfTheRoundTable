package com.qotrt.controller;

import java.security.Principal;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.qotrt.hub.Hub;
import com.qotrt.messages.game.GameJoinClient;

@Controller
public class ChatController {

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
	public void joinChat(@Payload GameJoinClient chatMessage) {
		hub.setTemplate(messagingTemplate);
		String[] players = hub.addPlayer(chatMessage.playerName);
		System.out.println("players: " + Arrays.toString(players));
		//return new GameJoinServer(players);
	}
}