package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.messages.game.GameJoinServer;
import com.qotrt.model.UIPlayer;

public class HubView extends View implements PropertyChangeListener {


	public HubView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}

	public void playerJoinedGame(UIPlayer[] UIPlayers) {
		System.out.println("sending message: " + Arrays.toString(UIPlayers));
		GameJoinServer gjs = new GameJoinServer();
		gjs.setPlayers(Arrays.stream(UIPlayers).map(i -> i.getPlayerName()).toArray(String[]::new));
		
		sendMessage("/queue/response", gjs);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("players")) {
			playerJoinedGame((UIPlayer[]) evt.getNewValue());
		}
	}
}