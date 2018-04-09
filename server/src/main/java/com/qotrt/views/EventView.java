package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.events.EventDiscardCardsServer;
import com.qotrt.messages.events.EventDiscardFinishPickingServer;
import com.qotrt.messages.events.EventDiscardOverServer;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class EventView extends Observer {

	public EventView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("discardEvent"), 
				x -> discard(mapper.convertValue(x.getNewValue(), Player[].class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("finishDiscard"), 
				x -> finish(mapper.convertValue(x.getNewValue(), Player.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("finishEvent"), 
				x -> finishEvent(mapper.convertValue(x.getNewValue(), Integer.class))));
	}


	private void finishEvent(Integer i) {
		sendMessage(new EventDiscardOverServer());
	}

	private void finish(Player p) {
		sendMessage(new EventDiscardFinishPickingServer(p.getID(), true, ""));
	}

	private void discard(Player[] players) {
		for(Player p: players){
			if(p.getTypeCount(TYPE.WEAPONS) >= 1) {
				sendMessage(new EventDiscardCardsServer(p.getID(),
						"Discard: 1 WEAPON card",
						Arrays.stream(players).mapToInt(i -> i.getID()).toArray()
						));
			} else if(p.getTypeCount(TYPE.FOES) >= 1) {
				sendMessage(new EventDiscardCardsServer(p.getID(),
						"Discard: " + Math.min(2, p.getTypeCount(TYPE.FOES)) + " FOE cards",
						Arrays.stream(players).mapToInt(i -> i.getID()).toArray()
						));
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}
}
