package com.qotrt.hub;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.qotrt.game.Game;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;

@Component
public class Hub {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<Game> games = new ArrayList<Game>();
	
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

	public synchronized UUID addGame(String gameName, int numPlayers, RIGGED rigged, Object ais) {
		Game game = new Game(messagingTemplate, gameName, numPlayers, rigged, ais);
		games.add(game);
		System.out.println("added game");
		System.out.println("games size: " + games.size());
		System.out.println("game name: " + game.getGameName());

		return game.getUUID();
	}

	public synchronized ArrayList<Game> listGames() {
		return games;
	}

}
