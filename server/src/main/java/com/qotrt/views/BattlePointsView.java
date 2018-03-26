package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.StoryCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.messages.game.BattlePointsServer;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.model.QuestModel;

public class BattlePointsView extends Observer {

	private BattlePointCalculator bpc;
	private StoryCard sc;
	
	public BattlePointsView(SimpMessagingTemplate messagingTemplate, PlayerManager pm) {
		super(messagingTemplate);
		this.bpc = new BattlePointCalculator(pm);
	}

	private void battlePoints(Player p) {
		int points = bpc.calculatePlayer(p, sc);
		sendMessage("/queue/response", new BattlePointsServer(p.getID(), points, ZONE.HAND));
	}
	
	private void battlePointsStage(QuestModel quest){
		HashMap<Integer, ZONE> map = new HashMap<Integer, ZONE>();
		map.put(0, ZONE.STAGE1);
		map.put(1, ZONE.STAGE2);
		map.put(2, ZONE.STAGE3);
		map.put(3, ZONE.STAGE4);
		map.put(4, ZONE.STAGE5);
		
		Player p = quest.getPlayerWhoSponsor().get(0);
		for(int i = 0; i < quest.getQuest().getNumCards(); i++){
			int points = bpc.calculateStage(quest.getQuest().getStage(i).getStageCards(), sc);
			sendMessage("/queue/response", new BattlePointsServer(p.getID(), points, map.get(i)));	
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("event got fired");
		if(evt.getPropertyName().equals("battlePoints")) {
			battlePoints((Player) evt.getNewValue());
		}
		
		if(evt.getPropertyName().equals("battlePointsStage")){
			battlePointsStage((QuestModel) evt.getNewValue());
		}
		
		// TODO handle middle card message
	}
}
