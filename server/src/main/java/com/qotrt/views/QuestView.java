package com.qotrt.views;

import java.beans.PropertyChangeEvent;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.messages.quest.QuestJoinServer;
import com.qotrt.messages.quest.QuestPickCardsServer;
import com.qotrt.messages.quest.QuestPickStagesServer;
import com.qotrt.messages.quest.QuestSponsorServer;

public class QuestView extends View {

	public QuestView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate);
	}
	
	private void questionSponsor(int[] players) {
		for(int i: players) {
			sendMessage("/queue/response", new QuestSponsorServer(i, players));
		}
	}
	
	private void questStage(int[] players) {
		// TODO: fix this hard coded 5
		sendMessage("/queue/response", new QuestPickStagesServer(players[0], 5));
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
	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		
		if(evt.getPropertyName().equals("questionSponsor")) {
			questionSponsor((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questStage")) {
			questStage((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questionQuest")) {
			questionQuest((int[]) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("questionCardQuest")) {
			questionCardQuest((int[]) evt.getNewValue());
		}
	}
}
