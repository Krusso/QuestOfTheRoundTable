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
import com.qotrt.messages.tournament.TournamentAcceptDeclineClient;
import com.qotrt.messages.tournament.TournamentFinishPickingClient;


@Controller
public class TournamentController {
	
	@Autowired
	private Hub hub;
	
	final static Logger logger = LogManager.getLogger(TournamentController.class);
	
	
	@MessageMapping("/game.joinTournament")
	public void playCard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload TournamentAcceptDeclineClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		logger.info("join tournament: " + chatMessage.player);
		if(chatMessage.joined) {
			game.bmm.getTournamentModel().acceptTournament(player);
		} else {
			game.bmm.getTournamentModel().declineTournament(player);
		}
	}
	
	@MessageMapping("/game.finishSelectingTournament")	
	public void finishSelectingTournament(SimpMessageHeaderAccessor headerAccessor, 
			@Payload TournamentFinishPickingClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		logger.info("finish selecting cards: " + chatMessage.player);
		game.bmm.getTournamentModel().finishSelectingCards(player);
	}

}