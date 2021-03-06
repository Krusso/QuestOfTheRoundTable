package com.qotrt.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.Player;
import com.qotrt.hub.Hub;
import com.qotrt.messages.special.CheatClient;
import com.qotrt.messages.special.CheatServer;
import com.qotrt.messages.special.MerlinClient;
import com.qotrt.messages.special.MerlinServer;
import com.qotrt.messages.special.MordredClient;
import com.qotrt.messages.special.MordredServer;
import com.qotrt.model.GenericPair;
import com.qotrt.sequence.Quest;


@Controller
public class SpecialInteractionController {

	final static Logger logger = LogManager.getLogger(SpecialInteractionController.class);
	
	@Autowired
	private Hub hub;


	@MessageMapping("/game.playMordred")
	public void playMordred(SimpMessageHeaderAccessor headerAccessor, 
			@Payload MordredClient chatMessage) {


		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		for(Player p: game.getPlayers()) {
			if(p.getFaceUp().findCardByID(chatMessage.opponent) != null && 
					player.findCardByID(chatMessage.mordred) != null &&
					p.getFaceUp().findCardByID(chatMessage.opponent).getType() == TYPE.ALLIES) {
				// remove card
				p.getFaceUp().getCardByID(chatMessage.opponent);
				player.getCardByID(chatMessage.mordred);
				game.sendMessageToAllPlayers("/queue/response", new MordredServer(player.getID(),
						p.getID(),chatMessage.mordred, chatMessage.opponent, ""));
				game.bmm.getBoardModel().fireMordred();
				return;
			}
		}
		
		game.sendMessageToAllPlayers("/queue/response", new MordredServer(player.getID(),
				-1, chatMessage.mordred, chatMessage.opponent, "Mordred can only kill Allies"));
		
	}
	
	@MessageMapping("/game.playMerlin")
	public void playMerlin(SimpMessageHeaderAccessor headerAccessor, 
			@Payload MerlinClient chatMessage) {


		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		
		GenericPair[] e = game.bmm.getQuestModel().merlinCan(chatMessage.stage);
		
		if(e != null) {
			if(player.hand.findCardByID(chatMessage.merlin) != null) {
				player.getFaceUp().addCard(player.hand.getCardByID(chatMessage.merlin));
			}
			
			game.sendMessageToAllPlayers("/queue/response", new MerlinServer(player.getID(), chatMessage.merlin, chatMessage.stage, e, ""));
		} else {
			game.sendMessageToAllPlayers("/queue/response", new MerlinServer(player.getID(), chatMessage.merlin, chatMessage.stage, 
					new GenericPair[] {}, "Can only use merlin once per quest"));
		}
		
	}
	
	@MessageMapping("/game.cheat")
	public void cheat(SimpMessageHeaderAccessor headerAccessor, 
			@Payload CheatClient chatMessage) {


		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());

		Quest quest = game.bmm.getQuestModel().getQuest();	
		if(quest == null) {
			return;
		}
		int[] cards = new int[quest.getNumStages()];
		for(int i = 0; i < quest.getNumStages(); i++) {
			cards[i] = quest.getStage(i).getStageCards().size();
		}
		
		game.sendMessageToAllPlayers("/queue/response", new CheatServer(player.getID(), cards));
		
	}

}