package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.StoryCard;
import com.qotrt.messages.game.MiddleCardServer;

public class BoardView extends View implements PropertyChangeListener {

	public BoardView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}
	
	private void middleCardFlipped(StoryCard s) {
		MiddleCardServer mcs = new MiddleCardServer(s.getName());
		
		sendMessage("/queue/response", mcs);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("middlecard")) {
			middleCardFlipped((StoryCard) evt.getNewValue());
		}
	}
	
}
