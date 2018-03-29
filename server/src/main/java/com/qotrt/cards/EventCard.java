package com.qotrt.cards;

import com.qotrt.cards.events.EventImplementation;

public class EventCard extends StoryCard {
	

	public EventImplementation implementation;
	
	public EventCard(String name, EventImplementation implementation) {
		super(name, TYPE.EVENT);
		this.implementation = implementation;
	}
	
}