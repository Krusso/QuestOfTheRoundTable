package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.gameplayer.Player;
import com.qotrt.messages.quest.BidDiscardFinishPickingServer;
import com.qotrt.messages.quest.FinishPickingStagesServer;
import com.qotrt.messages.quest.QuestBidServer;
import com.qotrt.messages.quest.QuestDiscardCardsServer;
import com.qotrt.messages.quest.QuestJoinServer;
import com.qotrt.messages.quest.QuestJoinedServer;
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

		events.add(new GenericPairTyped<>( x -> x.getPropertyName().equals("questionSponsor"), 
				x -> questionSponsor(mapper.convertValue(x.getNewValue(), int[].class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questStage"), 
				x -> questStage(mapper.convertValue(x.getNewValue(), GenericPair.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questionQuest"), 
				x -> questionQuest(mapper.convertValue(x.getNewValue(), int[].class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questionCardQuest"), 
				x -> questionCardQuest(mapper.convertValue(x.getNewValue(), int[].class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questWinners"), 
				x -> questWinners(mapper.convertValue(x.getNewValue(), GenericPair.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("passStage"), 
				x -> questWinners(new GenericPair(x.getNewValue(), WINTYPES.PASSSTAGE))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("bid"), 
				x -> bid(mapper.convertValue(x.getNewValue(), int[].class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("discardQuest"), 
				x -> discardQuest(mapper.convertValue(x.getNewValue(), GenericPair.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("flipStage"), 
				x -> flipStage(mapper.convertValue(x.getNewValue(), GenericPair.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("discardQuestFinish"), 
				x -> finishDiscard(mapper.convertValue(x.getNewValue(), Player.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("questStageDone"), 
				x -> questStageDone(mapper.convertValue(x.getNewValue(), Player.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("joinQuest"), 
				x -> joinQuest(mapper.convertValue(x.getNewValue(), Player.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("declineQuest"),
				x -> declineQuest(mapper.convertValue(x.getNewValue(), Player.class))));

	}

	private void declineQuest(Player player) {
		sendMessage(new QuestJoinedServer(player.getID(), false));
	}

	private void joinQuest(Player player) {
		sendMessage(new QuestJoinedServer(player.getID(), true));
	}

	private void questStageDone(Player player) {
		sendMessage(new FinishPickingStagesServer(player.getID(), true, ""));
	}

	private void finishDiscard(Player p) {
		sendMessage(new BidDiscardFinishPickingServer(p.getID(), true, ""));
	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

	private void questionSponsor(int[] players) {
		if(players.length == 0) {
			sendMessage(new QuestSponsorServer(-1, players));
		}

		for(int i: players) {
			sendMessage(new QuestSponsorServer(i, players));
		}
	}

	private void questStage(GenericPair e) {
		sendMessage(new QuestPickStagesServer(((int[])e.key)[0], (int)e.value));
	}

	private void questionQuest(int[] players) {
		for(int i: players) {
			sendMessage(new QuestJoinServer(i, players));
		}
	}

	private void questionCardQuest(int[] players) {
		for(int i: players) {
			sendMessage(new QuestPickCardsServer(i, players));
		}
	}

	private void questWinners(GenericPair e) {
		sendMessage(new QuestWinServer((int[]) e.key, (WINTYPES) e.value));
	}

	private void bid(int[] e) {
		sendMessage(new QuestBidServer(e[0], e[1], e[2], e[3]));
	}

	private void discardQuest(GenericPair e) {
		sendMessage(new QuestDiscardCardsServer(((int[])e.key)[0], (int) e.value));
	}

	private void flipStage(GenericPair e) {
		sendMessage(new QuestUpServer((String[]) e.key, (int) e.value));
	}


}
