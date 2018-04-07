package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.Rank;
import com.qotrt.messages.game.ShieldCountServer;
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.messages.hand.FaceUpDiscardServer;
import com.qotrt.messages.hand.FaceUpServer;
import com.qotrt.messages.rank.RankServer;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class PlayerView extends Observer {
		
	public PlayerView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);
		
		Function<PropertyChangeEvent, Boolean> funcF = x -> x.getPropertyName().equals("increaseLevel");
		Consumer<PropertyChangeEvent> funcC = x -> playerIncreasedLevel(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF1 = x -> x.getPropertyName().equals("addCards");
		Consumer<PropertyChangeEvent> funcC1 = x -> playerAddCards(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF2 = x -> x.getPropertyName().equals("changeShields");
		Consumer<PropertyChangeEvent> funcC2 = x -> playerChangeShields(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF3 = x -> x.getPropertyName().equals("discardType");
		Consumer<PropertyChangeEvent> funcC3 = x -> playerDiscardType(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF4 = x -> x.getPropertyName().equals("flipCards");
		Consumer<PropertyChangeEvent> funcC4 = x -> flipCards(mapper.convertValue(x.getNewValue(), Player.class));
		
		events.add(new GenericPairTyped<>(funcF, funcC));
		events.add(new GenericPairTyped<>(funcF1, funcC1));
		events.add(new GenericPairTyped<>(funcF2, funcC2));
		events.add(new GenericPairTyped<>(funcF3, funcC3));
		events.add(new GenericPairTyped<>(funcF4, funcC4));
	}

	private void playerIncreasedLevel(GenericPair e) {
		int player = mapper.convertValue(e.value, Integer.class);
		Rank.RANKS rank = mapper.convertValue(e.key, Rank.RANKS.class);
		sendMessage(new RankServer(player, rank));
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

	private void playerChangeShields(GenericPair e) {
		int player = mapper.convertValue(e.value, Integer.class);
		int shields = mapper.convertValue(e.key, Integer.class);
		sendMessage(new ShieldCountServer(player, shields));
	}
	
	private void playerAddCards(GenericPair e) {
		GenericPair[] cards = mapper.convertValue(e.key, GenericPair[].class);
		int player = mapper.convertValue(e.value, Integer.class);
		sendMessage(new AddCardsServer(player, cards));
	}
	
	private void playerDiscardType(GenericPair e) {
		GenericPair[] cards = mapper.convertValue(e.key, GenericPair[].class);
		int player = mapper.convertValue(e.value, Integer.class);
		sendMessage(new FaceUpDiscardServer(player, cards));
	}
	
	private void flipCards(Player p) {
		sendMessage(new FaceUpServer(p.getID()));
	}
}