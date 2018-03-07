package com.qotrt.game;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.views.HubView;

public class Game {
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	ArrayList<String> players = new ArrayList<String>();
	HubView hv;
	
	public Game(SimpMessagingTemplate messagingTemplate) {
		hv = new HubView(messagingTemplate);
		pcs.addPropertyChangeListener(hv);
	}

	public void addPlayer(String playerName) {
		String[] oldList = playerList();
		players.add(playerName);
		String[] newList = playerList();
		pcs.firePropertyChange("players", oldList, newList);
	}

	public String[] playerList() {
		return this.players.toArray(new String[players.size()]);
	}

}
