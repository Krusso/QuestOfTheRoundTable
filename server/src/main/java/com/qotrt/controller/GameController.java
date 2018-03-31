package com.qotrt.controller;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.qotrt.game.Game;
import com.qotrt.hub.Hub;
import com.qotrt.messages.game.GameCreateClient;
import com.qotrt.messages.game.GameJoinClient;
import com.qotrt.messages.game.GameListClient;
import com.qotrt.messages.game.PlayerListClient;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.model.UIPlayer;
import com.qotrt.util.WebSocketUtil;

@Controller
public class GameController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private Hub hub;

	@MessageMapping("/game.createGame")
	public void createGame(SimpMessageHeaderAccessor headerAccessor, @Payload GameCreateClient chatMessage) {
		UUID uuid = hub.addGame(chatMessage.getGameName(), chatMessage.getNumPlayers(), chatMessage.getRigged(), chatMessage.getAis());
		String sessionID = headerAccessor.getSessionId();
		System.out.println("s is: " + sessionID);
		GameJoinClient gjc = new GameJoinClient();
		gjc.setUuid(uuid);
		gjc.setPlayerName(chatMessage.getPlayerName());
		this.joinGame(headerAccessor, gjc);
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
				WebSocketUtil.createHeaders(headerAccessor.getSessionId()));
	}

	@MessageMapping("/game.joinGame")
	public void joinGame(SimpMessageHeaderAccessor headerAccessor, @Payload GameJoinClient chatMessage) {
		System.out.println("joining game");
		String sessionID = headerAccessor.getSessionId();
		UIPlayer player = new UIPlayer(sessionID, chatMessage.getPlayerName());
		System.out.println("s is: " + sessionID);
		hub.addPlayer(player, chatMessage.getUuid());
	}

	@MessageExceptionHandler(Exception.class)
	public void handleException(Exception ex) {
		System.out.println("Got exception: " + ex.getMessage() + " " + ex);
		ex.printStackTrace();
	}
	
	@ExceptionHandler(Exception.class)
	public void handleError(Exception ex) {
		System.out.println("Got exception: " + ex.getMessage() + " " + ex);
		ex.printStackTrace();
	  }
}