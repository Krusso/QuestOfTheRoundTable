package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Rank;
import com.qotrt.messages.game.ShieldCountServer;
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.messages.hand.FaceDownServer;
import com.qotrt.messages.rank.RankServer;
import com.qotrt.model.GenericPair;

public class PlayerView extends View implements PropertyChangeListener {
		
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
		
		if(evt.getPropertyName().equals("setFaceDown")) {
			playerSetFaceDown((GenericPair) evt.getNewValue());
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
	
	private void playerSetFaceDown(GenericPair e) {
		sendMessage("/queue/response", new FaceDownServer((int)e.value, (int) e.key));
	}
	
	private void playerDiscardType(GenericPair e) {
		// TODO: implement
	}
}