package com.qotrt.controller;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;

import com.qotrt.game.Game;
import com.qotrt.hub.Hub;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.GameListServer;
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

	// TODO: find some util class to put this in
	private MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		headerAccessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
		return headerAccessor.getMessageHeaders();
	}

	@MessageMapping("/game.createGame")
	public void createGame(SimpMessageHeaderAccessor headerAccessor, @Payload GameCreateClient chatMessage) {
		UUID uuid = hub.addGame(chatMessage.getNumPlayers());
		GameJoinClient gjc = new GameJoinClient();
		gjc.setUuid(uuid);
		gjc.setPlayerName(chatMessage.getPlayerName());
		this.joinChat(headerAccessor, gjc);
		System.out.println("created game");
	}

	@MessageMapping("/game.listGames")
	public void listGames(SimpMessageHeaderAccessor headerAccessor, @Payload GameListClient chatMessage) {
		System.out.println("listing games");
		ArrayList<Game> games = hub.listGames();
		System.out.println("games");
		GameListServer gls = new GameListServer();
		gls.setGamesWithGameList(games);
		messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), 
				"/queue/response",
				gls,
				createHeaders(headerAccessor.getSessionId()));
	}

	@MessageMapping("/game.joinGame")
	public void joinChat(SimpMessageHeaderAccessor headerAccessor, @Payload GameJoinClient chatMessage) {
		System.out.println("joining game");
		String sessionID = headerAccessor.getSessionId();
		UIPlayer player = new UIPlayer(sessionID, chatMessage.getPlayerName());
		System.out.println("s is: " + sessionID);
		hub.addPlayer(player, chatMessage.getUuid());
	}

	@MessageExceptionHandler
	public void handleException(IllegalArgumentException ex) {
		System.out.println("Got exception: " + ex.getMessage());
	}

}