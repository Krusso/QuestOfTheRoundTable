package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Rank;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.messages.game.ShieldCountServer;
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.messages.rank.RankServer;
import com.qotrt.model.GenericPair;

public class PlayerView extends View {
		
	public PlayerView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}

	private void playerIncreasedLevel(GenericPair e) {
		sendMessage("/queue/response", new RankServer((int) e.value, (Rank.RANKS) e.key));
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired: playerview: " + evt.getPropertyName());
		if(evt.getPropertyName().equals("increaseLevel")) {
			playerIncreasedLevel((GenericPair) evt.getNewValue());
		} 
		
		if(evt.getPropertyName().equals("addCards")) {
			playerAddCards((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("changeShields")) {
			playerChangeShields((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("moveCard")) {
			moveCard((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("discardType")) {
			playerDiscardType((GenericPair) evt.getNewValue());
		}
		System.out.println("finished updating after event fired");
	}

	private void playerChangeShields(GenericPair e) {
		sendMessage("/queue/response", new ShieldCountServer((int) e.value, (int) e.key));
	}
	
	private void playerAddCards(GenericPair e) {
		sendMessage("/queue/response", new AddCardsServer((int)e.value, (GenericPair[]) e.key));
	}
	
	private void moveCard(GenericPair e) {
		GenericPair p = (GenericPair) e.key;
		sendMessage("/queue/response", new PlayCardServer((int)e.value, (int) p.key, (ZONE) p.value,  ZONE.FACEDOWN, ""));
	}
	
	private void playerDiscardType(GenericPair e) {
		// TODO: implement
	}
}