package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.messages.game.GameJoinServer;

public class HubView implements PropertyChangeListener {
	
    private SimpMessagingTemplate messagingTemplate;
	
	public HubView(SimpMessagingTemplate messagingTemplate2) {
		this.messagingTemplate = messagingTemplate2;
	}

	public void playerJoinedGame(String[] players) {
		System.out.println("sending message: " + Arrays.toString(players));
		messagingTemplate.convertAndSend("/topic/public", new GameJoinServer(players));
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("players")) {
			playerJoinedGame((String[]) evt.getNewValue());
		}
	}
}