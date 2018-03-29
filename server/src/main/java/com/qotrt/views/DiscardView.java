package com.qotrt.views;

import java.beans.PropertyChangeEvent;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class DiscardView extends Observer {

	public DiscardView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

}
