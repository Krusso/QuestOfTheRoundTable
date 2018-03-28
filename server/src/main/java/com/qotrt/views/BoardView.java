package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.StoryCard;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.model.GenericPair2;

public class BoardView extends Observer {

	public BoardView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
		
		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("middlecard");
		Consumer<PropertyChangeEvent> func1 = x -> middleCardFlipped(mapper.convertValue(x.getNewValue(), StoryCard.class));
		
		events.add(new GenericPair2<>(func, func1));
	}

	private void middleCardFlipped(StoryCard s) {
		MiddleCardServer mcs = new MiddleCardServer(s.getName());
		sendMessage("/queue/response", mcs);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}
}
