package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessagingTemplate;

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
import com.qotrt.model.GenericPair2;

public class QuestView extends Observer {

	public QuestView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);

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
		
		events.add(new GenericPair2<>(funcF, funcC));
		events.add(new GenericPair2<>(funcF1, funcC1));
		events.add(new GenericPair2<>(funcF2, funcC2));
		events.add(new GenericPair2<>(funcF3, funcC3));
		events.add(new GenericPair2<>(funcF4, funcC4));
		events.add(new GenericPair2<>(funcF5, funcC5));
		events.add(new GenericPair2<>(funcF6, funcC6));
		events.add(new GenericPair2<>(funcF7, funcC7));
		events.add(new GenericPair2<>(funcF8, funcC8));

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
