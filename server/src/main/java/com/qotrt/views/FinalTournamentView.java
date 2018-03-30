package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;
import com.qotrt.messages.tournament.TournamentWinServer.WINTYPES;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class FinalTournamentView extends Observer {

	public FinalTournamentView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		Function<PropertyChangeEvent, Boolean> funcF = x -> x.getPropertyName().equals("questionFinalTournament");
		Consumer<PropertyChangeEvent> funcC = x -> questionCardTournament(mapper.convertValue(x.getNewValue(), int[].class));


		Function<PropertyChangeEvent, Boolean> funcF3 = x -> x.getPropertyName().equals("gamewinners");

		Consumer<PropertyChangeEvent> funcC3 = x -> setWinners(mapper.convertValue(x.getNewValue(), GenericPair.class));



		events.add(new GenericPairTyped<>(funcF, funcC));
		events.add(new GenericPairTyped<>(funcF3, funcC3));

	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
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
