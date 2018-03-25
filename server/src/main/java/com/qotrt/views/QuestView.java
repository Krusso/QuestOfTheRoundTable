package com.qotrt.views;

import java.beans.PropertyChangeEvent;

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

public class QuestView extends Observer {

	public QuestView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
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
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired: " + evt.getPropertyName());
		
		if(evt.getPropertyName().equals("questionSponsor")) {
			questionSponsor((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questStage")) {
			questStage((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questionQuest")) {
			questionQuest((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questionCardQuest")) {
			questionCardQuest((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questWinners")) {
			questWinners((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("passStage")) {
			questWinners(new GenericPair(evt.getNewValue(), WINTYPES.PASSSTAGE));
		}
		
		if(evt.getPropertyName().equals("bid")) {
			bid((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("discardQuest")) {
			discardQuest((GenericPair) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("flipStage")) {
			flipStage((GenericPair) evt.getNewValue());
		}
	}
}
