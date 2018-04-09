package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.messages.tournament.TournamentAcceptDeclineServer;
import com.qotrt.messages.tournament.TournamentAcceptedDeclinedServer;
import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;
import com.qotrt.messages.tournament.TournamentWinServer.WINTYPES;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class TournamentView extends Observer {

	public TournamentView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questiontournament"), 
				x -> questionTournament(mapper.convertValue(x.getNewValue(), int[].class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("jointournament"), 
				x -> joinTournament(mapper.convertValue(x.getNewValue(), Player.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("declinetournament"), 
				x -> declineTournament(mapper.convertValue(x.getNewValue(), Player.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("tournamentwinners"), 
				x -> setWinners(mapper.convertValue(x.getNewValue(), GenericPair.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questioncardtournament"), 
				x -> questionCardTournament(mapper.convertValue(x.getNewValue(), int[].class))));
		
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}
	
	private void questionTournament(int[] players) {
		for(int i: players) {
			sendMessage(new TournamentAcceptDeclineServer(i, players));
		}
	}
	
	private void joinTournament(Player player) {
		sendMessage(new TournamentAcceptedDeclinedServer(player.getID(), true));
	}
	
	private void declineTournament(Player player) {
		sendMessage(new TournamentAcceptedDeclinedServer(player.getID(), false));
	}
	
	private void setWinners(GenericPair e) {
		sendMessage(new TournamentWinServer((int[]) e.key, (WINTYPES) e.value));
	}
	
	private void questionCardTournament(int[] players) {
		for(int i: players) {
			sendMessage(new TournamentPickCardsServer(i, players));	
		}
	}
}
