package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.StoryCard;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class BoardView extends Observer {

	public BoardView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("middlecard"), 
				x -> middleCardFlipped(mapper.convertValue(x.getNewValue(), StoryCard.class))));
	}

	private void middleCardFlipped(StoryCard s) {
		MiddleCardServer mcs = new MiddleCardServer(s.getName());
		sendMessage(mcs);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}
}
