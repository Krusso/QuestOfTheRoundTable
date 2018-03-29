package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Rank;
import com.qotrt.messages.game.ShieldCountServer;
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.messages.rank.RankServer;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPair2;

public class PlayerView extends Observer {
		
	public PlayerView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
		
		Function<PropertyChangeEvent, Boolean> funcF = x -> x.getPropertyName().equals("increaseLevel");
		Consumer<PropertyChangeEvent> funcC = x -> playerIncreasedLevel(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF1 = x -> x.getPropertyName().equals("addCards");
		Consumer<PropertyChangeEvent> funcC1 = x -> playerAddCards(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF2 = x -> x.getPropertyName().equals("changeShields");
		Consumer<PropertyChangeEvent> funcC2 = x -> playerChangeShields(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF3 = x -> x.getPropertyName().equals("discardType");
		Consumer<PropertyChangeEvent> funcC3 = x -> playerDiscardType(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		events.add(new GenericPair2<>(funcF, funcC));
		events.add(new GenericPair2<>(funcF1, funcC1));
		events.add(new GenericPair2<>(funcF2, funcC2));
		events.add(new GenericPair2<>(funcF3, funcC3));
	}

	private void playerIncreasedLevel(GenericPair e) {
		sendMessage("/queue/response", new RankServer((int) e.value, (Rank.RANKS) e.key));
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

	private void playerChangeShields(GenericPair e) {
		sendMessage("/queue/response", new ShieldCountServer((int) e.value, (int) e.key));
	}
	
	private void playerAddCards(GenericPair e) {
		sendMessage("/queue/response", new AddCardsServer((int)e.value, (GenericPair[]) e.key));
	}
	
	private void playerDiscardType(GenericPair e) {
		// TODO: implement
	}
}