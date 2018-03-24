package com.qotrt.views;

import java.beans.PropertyChangeEvent;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.messages.tournament.TournamentAcceptDeclineServer;
import com.qotrt.messages.tournament.TournamentAcceptedDeclinedServer;
import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;
import com.qotrt.messages.tournament.TournamentWinServer.WINTYPES;
import com.qotrt.model.GenericPair;

public class TournamentView extends View {

	public TournamentView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}
	
	private void questionTournament(int[] players) {
		for(int i: players) {
			sendMessage("/queue/response", new TournamentAcceptDeclineServer(i));
		}
	}
	
	private void joinTournament(Player player) {
		sendMessage("/queue/response", new TournamentAcceptedDeclinedServer(player.getID(), true));
	}
	
	private void declineTournament(Player player) {
		sendMessage("/queue/response", new TournamentAcceptedDeclinedServer(player.getID(), false));
	}
	
	private void setWinners(GenericPair e) {
		sendMessage("/queue/response", new TournamentWinServer((int[]) e.key, (WINTYPES) e.value));
	}
	
	private void questionCardTournament(int[] players) {
		for(int i: players) {
			sendMessage("/queue/response", new TournamentPickCardsServer(i, players));	
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("questiontournament")) {
			questionTournament((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("jointournament")) {
			joinTournament((Player) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("declinetournament")) {
			declineTournament((Player) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("tournamentwinners")) {
			setWinners((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questioncardtournament")) {
			questionCardTournament((int[]) evt.getNewValue());
		}
	}
}
