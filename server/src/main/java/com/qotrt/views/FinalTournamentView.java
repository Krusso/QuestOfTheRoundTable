package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.messages.gameover.FinalTournamentNotifyServer;
import com.qotrt.messages.gameover.GameOverServer;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class FinalTournamentView extends Observer {

	public FinalTournamentView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questionFinalTournament"), 
				x -> questionCardTournament(mapper.convertValue(x.getNewValue(), int[].class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("gamewinners"), 
				x -> setWinners(mapper.convertValue(x.getNewValue(), int[].class))));

	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

	private void setWinners(int[] players) {
		sendMessage(new GameOverServer(players));
	}

	private void questionCardTournament(int[] players) {
		sendMessage(new FinalTournamentNotifyServer(players));
	}
}
