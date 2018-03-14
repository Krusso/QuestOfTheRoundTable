package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;

public class TournamentView extends View implements PropertyChangeListener {

	public TournamentView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}
	
	private void questionTournament(int[] players) {
		sendMessage("/queue/response", players);
	}
	
	private void joinTournament(Player player) {
		sendMessage("/queue/response", player.getID());
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("questiontournament")) {
			questionTournament((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("jointournament")) {
			joinTournament((Player) evt.getNewValue());
		}
	}
	
}
