package com.qotrt.controller;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.qotrt.messages.game.GameListServer;
import com.qotrt.model.UIPlayer;
import com.qotrt.util.WebSocketUtil;

@Controller
public class GameController {

	final static Logger logger = LogManager.getLogger(GameController.class);
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private Hub hub;

	@MessageMapping("/game.createGame")
	public void createGame(SimpMessageHeaderAccessor headerAccessor, @Payload GameCreateClient chatMessage) {
		logger.info("Discard: " + chatMessage.getDiscard());
		if(null == chatMessage.getDiscard()) {
			chatMessage.setDiscard(true);
		}
		if(null == chatMessage.getRacing()) {
			chatMessage.setRacing(true);
		}
		
		UUID uuid = hub.addGame(chatMessage.getGameName(), 
				chatMessage.getNumPlayers(),
				chatMessage.getRigged(), 
				chatMessage.getAis(),
				chatMessage.getDiscard(),
				chatMessage.getRacing());
		
		String sessionID = headerAccessor.getSessionId();
		logger.info("s is: " + sessionID);
		logger.info("Rigged: " + chatMessage.getRigged());
		GameJoinClient gjc = new GameJoinClient();
		gjc.setUuid(uuid);
		gjc.setPlayerName(chatMessage.getPlayerName());
		this.joinGame(headerAccessor, gjc);
		logger.info("created game");

	}

	@MessageMapping("/game.listGames")
	public void listGames(SimpMessageHeaderAccessor headerAccessor, @Payload GameListClient chatMessage) {
		logger.info("listing games");
		ArrayList<Game> games = hub.listGames();
		logger.info("games");
		GameListServer gls = new GameListServer();
		gls.setGamesWithGameList(games);
		messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), 
				"/queue/response",
				gls,
				WebSocketUtil.createHeaders(headerAccessor.getSessionId()));
	}

	@MessageMapping("/game.joinGame")
	public void joinGame(SimpMessageHeaderAccessor headerAccessor, @Payload GameJoinClient chatMessage) {
		logger.info("joining game");
		String sessionID = headerAccessor.getSessionId();
		UIPlayer player = new UIPlayer(sessionID, chatMessage.getPlayerName());
		logger.info("s is: " + sessionID);
		hub.addPlayer(player, chatMessage.getUuid());
	}

	@MessageExceptionHandler(Exception.class)
	public void handleException(Exception ex) {
		logger.info("Got exception: " + ex.getMessage() + " " + ex);
		ex.printStackTrace();
	}
	
	@ExceptionHandler(Exception.class)
	public void handleError(Exception ex) {
		logger.info("Got exception: " + ex.getMessage() + " " + ex);
		ex.printStackTrace();
	  }
}