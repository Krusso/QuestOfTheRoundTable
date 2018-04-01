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
import com.qotrt.messages.special.MerlinClient;
import com.qotrt.messages.special.MerlinServer;
import com.qotrt.messages.special.MordredClient;
import com.qotrt.messages.special.MordredServer;
import com.qotrt.model.GenericPair;


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
				p.getFaceUp().findCardByID(chatMessage.opponent);
				player.getCardByID(chatMessage.mordred);
				game.sendMessageToAllPlayers("/queue/response", new MordredServer(player.getID(),
						chatMessage.mordred, chatMessage.opponent, ""));
				return;
			}
		}
		
		game.sendMessageToAllPlayers("/queue/response", new MordredServer(player.getID(),
				chatMessage.mordred, chatMessage.opponent, "Mordred can only kill Allies"));
		
	}
	
	@MessageMapping("/game.playMerlin")
	public void playMerlin(SimpMessageHeaderAccessor headerAccessor, 
			@Payload MerlinClient chatMessage) {


		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		
		GenericPair[] e = game.bmm.getQuestModel().merlinCan();
		
		if(e != null) {
			game.sendMessageToAllPlayers("/queue/response", new MerlinServer(player.getID(), e, ""));
		} else {
			game.sendMessageToAllPlayers("/queue/response", new MerlinServer(player.getID(), new GenericPair[] {}, ""));
		}
		
	}

}