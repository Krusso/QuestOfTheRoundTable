package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.qotrt.model.GenericPair;

public class PlayerView extends View implements PropertyChangeListener {
	
	private void playerIncreasedLevel(GenericPair e) {
		System.out.println("sending message: " + e);
		//Arrays.stream(UIPlayers)
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("increaseLevel")) {
			playerIncreasedLevel((GenericPair) evt.getNewValue());
		}
	}
}