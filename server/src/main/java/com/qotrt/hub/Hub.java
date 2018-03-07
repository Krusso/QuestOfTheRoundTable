package com.qotrt.hub;

import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.qotrt.game.Game;

@Component
public class Hub {

	private ArrayList<Game> games = new ArrayList<Game>();
	private SimpMessagingTemplate messagingTemplate;
	
	public synchronized String[] addPlayer(String playerName) {
		if(games.size() == 0) {
			games.add(new Game(messagingTemplate));
		}
		
		games.get(0).addPlayer(playerName);
		return games.get(0).playerList();
	}

	public void setTemplate(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
}
