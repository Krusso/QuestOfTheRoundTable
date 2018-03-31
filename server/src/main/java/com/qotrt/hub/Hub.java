package com.qotrt.hub;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.qotrt.game.Game;
import com.qotrt.messages.game.AIPlayer;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;

@Component
public class Hub {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<Game> games = new ArrayList<Game>();
	
	final static Logger logger = LogManager.getLogger(Hub.class);
	
	public synchronized boolean addPlayer(UIPlayer player, UUID uuid) {
		for(Game game: games) {
			if(game.getUUID().equals(uuid)) {
				return game.addPlayer(player);
			}
		}
		return false;
	}

	public synchronized Game getGameBySessionID(String sessionId) {
		for(Game game: games) {
			if(game.getPlayerBySessionID(sessionId) != null) {
				return game;
			}
		}
		return null;
	}

	public synchronized UUID addGame(String gameName, int numPlayers, RIGGED rigged, AIPlayer[] aiPlayers, Boolean discard, Boolean racing) {
		Game game = new Game(messagingTemplate, gameName, numPlayers, rigged, aiPlayers, discard, racing);
		games.add(game);
		logger.info("added game");
		logger.info("games size: " + games.size());
		logger.info("game name: " + game.getGameName());

		return game.getUUID();
	}

	public synchronized ArrayList<Game> listGames() {
		return games;
	}

}
