package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.events.EventDiscardCardsServer;
import com.qotrt.messages.events.EventDiscardFinishPickingServer;
import com.qotrt.messages.events.EventDiscardOverServer;
import com.qotrt.model.GenericPair2;
import com.qotrt.model.UIPlayer;

public class EventView extends Observer {

	public EventView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("discardEvent");
		Consumer<PropertyChangeEvent> func1 = x -> discard(mapper.convertValue(x.getNewValue(), Player[].class));

		Function<PropertyChangeEvent, Boolean> funcC = x -> x.getPropertyName().equals("finishDiscard");
		Consumer<PropertyChangeEvent> funcC1 = x -> finish(mapper.convertValue(x.getNewValue(), Player.class));

		Function<PropertyChangeEvent, Boolean> funcC2 = x -> x.getPropertyName().equals("finishEvent");
		Consumer<PropertyChangeEvent> funcC3 = x -> finishEvent(mapper.convertValue(x.getNewValue(), Integer.class));


		events.add(new GenericPair2<>(func, func1));
		events.add(new GenericPair2<>(funcC, funcC1));
		events.add(new GenericPair2<>(funcC2, funcC3));
	}

	
	private void finishEvent(Integer i) {
		sendMessage("/queue/response", new EventDiscardOverServer());
	}
	
	private void finish(Player p) {
		sendMessage("/queue/response", new EventDiscardFinishPickingServer(p.getID(), true, ""));
	}
	
	private void discard(Player[] players) {
		for(Player p: players){
			if(p.getTypeCount(TYPE.WEAPONS) >= 1) {
				sendMessage("/queue/response", new EventDiscardCardsServer(p.getID(),
						"Discard: 1 WEAPON card",
						Arrays.stream(players).mapToInt(i -> i.getID()).toArray()
						));
			} else if(p.getTypeCount(TYPE.FOES) >= 1) {
				sendMessage("/queue/response", new EventDiscardCardsServer(p.getID(),
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
