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
import com.qotrt.messages.gameover.FinalTournamentFinishPickingClient;


@Controller
public class FinalTournamentController {
	
	final static Logger logger = LogManager.getLogger(FinalTournamentController.class);
	
	@Autowired
	private Hub hub;
	
	@MessageMapping("/game.finishSelectingFinalTournament")	
	public void finishSelectingFinalTournament(SimpMessageHeaderAccessor headerAccessor, 
			@Payload FinalTournamentFinishPickingClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		logger.info("finish selecting cards: " + chatMessage.player);
		game.bmm.getFinalTournamentModel().finishSelectingCards(player);
	}

}