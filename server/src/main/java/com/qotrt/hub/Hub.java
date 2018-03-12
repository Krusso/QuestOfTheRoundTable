package com.qotrt.hub;

import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.qotrt.game.Game;
import com.qotrt.model.UIPlayer;

@Component
public class Hub {

	private ArrayList<Game> games = new ArrayList<Game>();
	private SimpMessagingTemplate messagingTemplate;
	
	public synchronized void addPlayer(UIPlayer player) {
		if(games.size() == 0) {
			games.add(new Game(messagingTemplate));
		}
		
		games.get(0).addPlayer(player);
	}

	public void setTemplate(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	public Game getGameBySessionID(String sessionId) {
		for(Game game: games) {
			if(game.getPlayerBySessionID(sessionId) != null) {
				return game;
			}
		}
		return null;
	}

}
