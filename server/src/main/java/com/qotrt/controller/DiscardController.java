package com.qotrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.qotrt.game.Game;
import com.qotrt.gameplayer.Player;
import com.qotrt.hub.Hub;
import com.qotrt.messages.discard.HandFullFinishPickingClient;
import com.qotrt.messages.discard.HandFullFinishPickingServer;


@Controller
public class DiscardController {
	
	@Autowired
	private Hub hub;
	
	@MessageMapping("/game.finishSelectingDiscardHand")	
	public void finishSelectingEvent(SimpMessageHeaderAccessor headerAccessor, 
			@Payload HandFullFinishPickingClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		System.out.println("finish selecting cards: " + chatMessage.player);
		String response = game.bmm.getDiscardModel().finishDiscarding(player);
		System.out.println("response to trying to finish: " + response);
		if(!response.equals("")) {
			game.sendMessageToAllPlayers("/queue/response", new HandFullFinishPickingServer(player.getID(), false, response));
		}
	}

}