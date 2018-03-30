package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.messages.discard.HandFullFinishPickingServer;
import com.qotrt.messages.discard.HandFullServer;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class DiscardView extends Observer {

	public DiscardView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);
		
		Function<PropertyChangeEvent, Boolean> funcF = 
				x -> x.getPropertyName().equals("discard");
		Consumer<PropertyChangeEvent> funcC = 
				x -> discard(mapper.convertValue(x.getNewValue(), Player[].class));
		
		Function<PropertyChangeEvent, Boolean> funcF1 = 
				x -> x.getPropertyName().equals("finishHandDiscard");
		Consumer<PropertyChangeEvent> funcC1 = 
				x -> finishDiscard(mapper.convertValue(x.getNewValue(), Player.class));
		

		events.add(new GenericPairTyped<>(funcF, funcC));
		events.add(new GenericPairTyped<>(funcF1, funcC1));
		
	}

	private void finishDiscard(Player p) {
		sendMessage("/queue/response", new HandFullFinishPickingServer(p.getID(), true, ""));
	}
	
	private void discard(Player[] players) {
		for(Player p: players) {
			sendMessage("/queue/response", new HandFullServer(p.getID(), p.hand.size() - 12));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

}
