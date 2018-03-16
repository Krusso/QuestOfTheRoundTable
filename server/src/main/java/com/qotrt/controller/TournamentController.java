package com.qotrt.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.qotrt.game.Game;
import com.qotrt.gameplayer.Player;
import com.qotrt.hub.Hub;
import com.qotrt.messages.tournament.TournamentAcceptDeclineClient;


@Controller
public class TournamentController {
	
	@Autowired
	private Hub hub;
	@PostConstruct
	private void init() {
		System.out.println("created 2");
	}

	@PreDestroy
	private void destroy() {
		System.out.println("deleted");
	}

	@MessageMapping("/game.joinTournament")
	public void playCard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload TournamentAcceptDeclineClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		System.out.println("join tournament: " + chatMessage.player);
		if(chatMessage.joined) {
			game.bmm.getTournamentModel().acceptTournament(player);
		} else {
			game.bmm.getTournamentModel().declineTournament(player);
		}
	}
	
	@MessageMapping("/game.finishSelectingTournament")	
	public void finishSelectingTournament(SimpMessageHeaderAccessor headerAccessor, 
			@Payload TournamentAcceptDeclineClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		System.out.println("finish selecting cards: " + chatMessage.player);
		game.bmm.getTournamentModel().finishSelectingCards(player);
	}

}