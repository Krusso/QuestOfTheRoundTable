package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.messages.quest.BidDiscardFinishPickingServer;
import com.qotrt.messages.quest.QuestBidServer;
import com.qotrt.messages.quest.QuestDiscardCardsServer;
import com.qotrt.messages.quest.QuestJoinServer;
import com.qotrt.messages.quest.QuestPickCardsServer;
import com.qotrt.messages.quest.QuestPickStagesServer;
import com.qotrt.messages.quest.QuestSponsorServer;
import com.qotrt.messages.quest.QuestUpServer;
import com.qotrt.messages.quest.QuestWinServer;
import com.qotrt.messages.quest.QuestWinServer.WINTYPES;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class QuestView extends Observer {

	public QuestView(SimpMessagingTemplate messagingTemplate, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);

		Function<PropertyChangeEvent, Boolean> funcF = 
				x -> x.getPropertyName().equals("questionSponsor");

		Consumer<PropertyChangeEvent> funcC = 
				x -> questionSponsor(mapper.convertValue(x.getNewValue(), int[].class));
		
		Function<PropertyChangeEvent, Boolean> funcF1 = 
						x -> x.getPropertyName().equals("questStage");
		Consumer<PropertyChangeEvent> funcC1 = 
				x -> questStage(mapper.convertValue(x.getNewValue(), GenericPair.class));
		
		Function<PropertyChangeEvent, Boolean> funcF2 = 
						x -> x.getPropertyName().equals("questionQuest");
		Consumer<PropertyChangeEvent> funcC2 = 
				x -> questionQuest(mapper.convertValue(x.getNewValue(), int[].class));
		
		Function<PropertyChangeEvent, Boolean> funcF3 = 
						x -> x.getPropertyName().equals("questionCardQuest");
						
		Consumer<PropertyChangeEvent> funcC3 = 
				x -> questionCardQuest(mapper.convertValue(x.getNewValue(), int[].class));
		
		Function<PropertyChangeEvent, Boolean> funcF4 = 
						x -> x.getPropertyName().equals("questWinners");
						
		Consumer<PropertyChangeEvent> funcC4 = 
				x -> questWinners(mapper.convertValue(x.getNewValue(), GenericPair.class));
				
		Function<PropertyChangeEvent, Boolean> funcF5 = 
						x -> x.getPropertyName().equals("passStage");
						
		Consumer<PropertyChangeEvent> funcC5 = 
				x -> questWinners(new GenericPair(x.getNewValue(), WINTYPES.PASSSTAGE));
				
		Function<PropertyChangeEvent, Boolean> funcF6 = 
						x -> x.getPropertyName().equals("bid");
						
		Consumer<PropertyChangeEvent> funcC6 = 
				x -> bid(mapper.convertValue(x.getNewValue(), int[].class));
				
		Function<PropertyChangeEvent, Boolean> funcF7 = 
						x -> x.getPropertyName().equals("discardQuest");
						
		Consumer<PropertyChangeEvent> funcC7 = 
				x -> discardQuest(mapper.convertValue(x.getNewValue(), GenericPair.class));
				
		Function<PropertyChangeEvent, Boolean> funcF8 = 
						x -> x.getPropertyName().equals("flipStage");
						
		Consumer<PropertyChangeEvent> funcC8 = 
				x -> flipStage(mapper.convertValue(x.getNewValue(), GenericPair.class));
				
		Function<PropertyChangeEvent, Boolean> funcF9 = 
						x -> x.getPropertyName().equals("discardQuestFinish");
						
		Consumer<PropertyChangeEvent> funcC9 = 
				x -> finishDiscard(mapper.convertValue(x.getNewValue(), Player.class));		
				
		events.add(new GenericPairTyped<>(funcF, funcC));
		events.add(new GenericPairTyped<>(funcF1, funcC1));
		events.add(new GenericPairTyped<>(funcF2, funcC2));
		events.add(new GenericPairTyped<>(funcF3, funcC3));
		events.add(new GenericPairTyped<>(funcF4, funcC4));
		events.add(new GenericPairTyped<>(funcF5, funcC5));
		events.add(new GenericPairTyped<>(funcF6, funcC6));
		events.add(new GenericPairTyped<>(funcF7, funcC7));
		events.add(new GenericPairTyped<>(funcF8, funcC8));
		events.add(new GenericPairTyped<>(funcF9, funcC9));

	}

	private void finishDiscard(Player p) {
		sendMessage("/queue/response", new BidDiscardFinishPickingServer(p.getID(), true, ""));
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

	private void questionSponsor(int[] players) {
		if(players.length == 0) {
			sendMessage("/queue/response", new QuestSponsorServer(-1, players));
		}

		for(int i: players) {
			sendMessage("/queue/response", new QuestSponsorServer(i, players));
		}
	}

	private void questStage(GenericPair e) {
		sendMessage("/queue/response", new QuestPickStagesServer(((int[])e.key)[0], (int)e.value));
	}

	private void questionQuest(int[] players) {
		for(int i: players) {
			sendMessage("/queue/response", new QuestJoinServer(i, players));
		}
	}

	private void questionCardQuest(int[] players) {
		for(int i: players) {
			sendMessage("/queue/response", new QuestPickCardsServer(i, players));
		}
	}

	private void questWinners(GenericPair e) {
		sendMessage("/queue/response", new QuestWinServer((int[]) e.key, (WINTYPES) e.value));
	}

	private void bid(int[] e) {
		sendMessage("/queue/response", new QuestBidServer(e[0], e[1], e[2], e[3]));
	}

	private void discardQuest(GenericPair e) {
		sendMessage("/queue/response", new QuestDiscardCardsServer(((int[])e.key)[0], (int) e.value));
	}

	private void flipStage(GenericPair e) {
		sendMessage("/queue/response", new QuestUpServer((String[]) e.key, (int) e.value));
	}


}
