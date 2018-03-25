package com.qotrt.views;

import java.beans.PropertyChangeEvent;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.StoryCard;
import com.qotrt.messages.game.MiddleCardServer;

public class BoardView extends Observer {

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
	
	// TODO: use this
	//	@Subscribe
	//	public void storyCard(StoryCard event) {
	//		System.out.println("whahsdhads1");
	//		System.out.println(event.getName());
	//	}
	//
	//	@Subscribe
	//	public void storyCard(GenericPair2<Integer, Integer> event) {
	//		System.out.println("whahsdhads2: " + event.value);
	//		//System.out.println(event.getName());
	//	}
	//
	//
	//	@Subscribe
	//	public void storyCard1(GenericPair2<Integer, String> event) {
	//		if(event.value instanceof String) {
	//			System.out.println("whahsdhads3: " + event.value);
	//		}
	//		//System.out.println(event.getName());
	//	}



}
