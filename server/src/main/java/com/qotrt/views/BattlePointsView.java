package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.StoryCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.messages.game.BattlePointsServer;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.QuestModel;
import com.qotrt.model.UIPlayer;

public class BattlePointsView extends Observer {

	private BattlePointCalculator bpc;
	private StoryCard sc;

	public BattlePointsView(SimpMessagingTemplate messagingTemplate, PlayerManager pm, ArrayList<UIPlayer> players) {
		super(messagingTemplate, players);
		this.bpc = new BattlePointCalculator(pm);

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("middlecard"), 
				x -> sc = (mapper.convertValue(x.getNewValue(), StoryCard.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("battlePoints"), 
				x -> battlePoints(mapper.convertValue(x.getNewValue(), Player.class))));

		events.add(new GenericPairTyped<>(x -> x.getPropertyName().equals("battlePointsStage"), 
				x -> battlePointsStage(mapper.convertValue(x.getNewValue(), QuestModel.class))));
	}

	private void battlePoints(Player p) {
		if(sc == null) {
			logger.info("Story card is empty");
			return;
		}
		logger.info("Story card not empty: " + sc + " calculating bp");
		int points = bpc.calculatePlayer(p, sc);
		sendMessage(new BattlePointsServer(p.getID(), points, ZONE.HAND));
	}

	private void battlePointsStage(QuestModel quest){
		HashMap<Integer, ZONE> map = new HashMap<Integer, ZONE>();
		map.put(0, ZONE.STAGE1);
		map.put(1, ZONE.STAGE2);
		map.put(2, ZONE.STAGE3);
		map.put(3, ZONE.STAGE4);
		map.put(4, ZONE.STAGE5);

		Player p = quest.getPlayerWhoSponsor().get(0);
		for(int i = 0; i < quest.getQuest().getNumStages(); i++){
			int points = bpc.calculateStage(quest.getQuest().getStage(i).getStageCards(), sc);
			sendMessage(new BattlePointsServer(p.getID(), points, map.get(i)));	
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}
}
