package com.qotrt.messages.quest;

import java.util.List;

import src.messages.Message;
import src.player.Player;

//from server
public class QuestPassAllServer extends Message {
	public int[] players;
	
	public QuestPassAllServer(List<Player> winners) {
		players = winners.stream().mapToInt(i -> i.getID()).toArray();
	}

	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.PASSALL;
	}

}
