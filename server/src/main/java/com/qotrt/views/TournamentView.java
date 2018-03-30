package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

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
		
		Function<PropertyChangeEvent, Boolean> funcF = 
				x -> x.getPropertyName().equals("questiontournament");
		Consumer<PropertyChangeEvent> funcC = 
				x -> questionTournament(mapper.convertValue(x.getNewValue(), int[].class));
		
		Function<PropertyChangeEvent, Boolean> funcF1 = 
				x -> x.getPropertyName().equals("jointournament");
		Consumer<PropertyChangeEvent> funcC1 = 
				x -> joinTournament(mapper.convertValue(x.getNewValue(), Player.class));
		
		Function<PropertyChangeEvent, Boolean> funcF2 = 
				x -> x.getPropertyName().equals("declinetournament");
		Consumer<PropertyChangeEvent> funcC2 = 
				x -> declineTournament(mapper.convertValue(x.getNewValue(), Player.class));
				
		Function<PropertyChangeEvent, Boolean> funcF3 = x -> x.getPropertyName().equals("tournamentwinners");
		
		Consumer<PropertyChangeEvent> funcC3 = x -> setWinners(mapper.convertValue(x.getNewValue(), GenericPair.class));

		Function<PropertyChangeEvent, Boolean> funcF4 = x -> x.getPropertyName().equals("questioncardtournament");
		
		Consumer<PropertyChangeEvent> funcC4 = x -> questionCardTournament(mapper.convertValue(x.getNewValue(), int[].class));

		
		events.add(new GenericPairTyped<>(funcF, funcC));
		events.add(new GenericPairTyped<>(funcF1, funcC1));
		events.add(new GenericPairTyped<>(funcF2, funcC2));
		events.add(new GenericPairTyped<>(funcF3, funcC3));
		events.add(new GenericPairTyped<>(funcF4, funcC4));
		
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
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
}
