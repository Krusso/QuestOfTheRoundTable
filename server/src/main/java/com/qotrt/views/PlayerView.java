package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

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
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("increaseLevel"), 
				x -> playerIncreasedLevel(mapper.convertValue(x.getNewValue(), GenericPair.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("addCards"), 
				 x -> playerAddCards(mapper.convertValue(x.getNewValue(), GenericPair.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("changeShields"), 
				x -> playerChangeShields(mapper.convertValue(x.getNewValue(), GenericPair.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("discardType"), 
				x -> playerDiscardType(mapper.convertValue(x.getNewValue(), GenericPair.class))));
		
		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("flipCards"), 
				x -> flipCards(mapper.convertValue(x.getNewValue(), Player.class))));
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