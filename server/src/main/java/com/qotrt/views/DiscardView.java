package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.messages.discard.HandFullFinishPickingServer;
import com.qotrt.messages.discard.HandFullServer;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class DiscardView extends Observer {

	public DiscardView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("discard"), 
				x -> discard(mapper.convertValue(x.getNewValue(), Player[].class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("finishHandDiscard"), 
				x -> finishDiscard(mapper.convertValue(x.getNewValue(), Player.class))));

	}

	private void finishDiscard(Player p) {
		sendMessage(new HandFullFinishPickingServer(p.getID(), true, ""));
	}

	private void discard(Player[] players) {
		for(Player p: players) {
			sendMessage(new HandFullServer(p.getID(), p.hand.size() - 12));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

}
