package com.qotrt.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.qotrt.game.Game;
import com.qotrt.gameplayer.Player;
import com.qotrt.hub.Hub;
import com.qotrt.messages.events.EventDiscardFinishPickingClient;
import com.qotrt.messages.events.EventDiscardFinishPickingServer;


@Controller
public class EventController {
	
	final static Logger logger = LogManager.getLogger(EventController.class);
	
	@Autowired
	private Hub hub;
	
	@MessageMapping("/game.finishSelectingEvent")	
	public void finishSelectingEvent(SimpMessageHeaderAccessor headerAccessor, 
			@Payload EventDiscardFinishPickingClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		logger.info("finish selecting cards: " + chatMessage.player);
		String response = game.bmm.getEventModel().finishDiscarding(player);
		logger.info("response to trying to finish: " + response);
		if(!response.equals("")) {
			game.sendMessageToAllPlayers("/queue/response", new EventDiscardFinishPickingServer(player.getID(), false, response));
		}
	}

}