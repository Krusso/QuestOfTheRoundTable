package com.qotrt.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.Player;
import com.qotrt.hub.Hub;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;


@Controller
public class PlayCardController {

	@Autowired
	private Hub hub;

	@PostConstruct
	private void init() {
		System.out.println("created 2");
	}

	@PreDestroy
	private void destroy() {
		//activeSessionManager.removeListener(this);
		System.out.println("deleted");
	}


	@MessageMapping("/game.playCardTournament")
	public void playCard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());

		//TODO: find a way to refactor this, maybe put it in the cards themselves?

		if(chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				(game.bmm.getTournamentModel().canPick())) {
			String response = verifyFaceDownCard(player, chatMessage.card);
			if(response.equals("")) {
				player.setFaceDown(player.getCardByID(chatMessage.card), ZONE.HAND);
			} else {
				game.sendMessageToAllPlayers("/queue/response", 
						new PlayCardServer(player.getID(), 
								chatMessage.card, 
								chatMessage.zoneFrom, 
								chatMessage.zoneFrom, 
								response));
			}
		} else if(chatMessage.zoneTo.equals(ZONE.FACEUP)) {
			//TODO: handle this case
		} else if(chatMessage.zoneTo.equals(ZONE.DISCARD)) {
			//TODO: handle this case
		} else if(chatMessage.zoneTo.equals(ZONE.HAND)) {
			//TODO: handle this case


			//			if(chatMessage.zoneFrom.equals(ZONE.FACEDOWN)) {
			//				player
			//			}

		} else if(chatMessage.zoneTo.equals(ZONE.STAGE1)) {
			//TODO: handle this case
		} else if(chatMessage.zoneTo.equals(ZONE.STAGE2)) {
			//TODO: handle this case
		} else if(chatMessage.zoneTo.equals(ZONE.STAGE3)) {
			//TODO: handle this case
		} else if(chatMessage.zoneTo.equals(ZONE.STAGE4)) {
			//TODO: handle this case			
		} else if(chatMessage.zoneTo.equals(ZONE.STAGE5)) {
			//TODO: handle this case
		}
	}

	private String verifyFaceDownCard(Player player, int card) {
		AdventureCard c = player.findCardByID(card);
		System.out.println("Player: " + player.getID() + " trying to play: " + c.getName() + " id: " + card);
		switch(c.getType()) {
		case ALLIES:
			System.out.println("Card is ally can play");
			return "";
		case AMOUR:
			for(AdventureCard a: player.getFaceDownDeck().getDeck()) {
				if(a.getType() == TYPE.AMOUR) {
					System.out.println("amour card already face dont cant play");
					return "Amour card already in facedown pane cant play more than 1";
				}
			}
			System.out.println("amour card not already face down can play");
			return "";
		case FOES:
			System.out.println("cant play foes card");
			return "Cant play foe cards for tournament";
		case TESTS:
			System.out.println("cant play tests card");
			return "Cant play test cards for tournament";
		case WEAPONS:
			for(AdventureCard a: player.getFaceDownDeck().getDeck()) {
				if(a.getName().equals(c.getName())) {
					System.out.println("cant play duplicate weapon cards");
					return "Cant play duplicate weapons";
				}
			}
			System.out.println("can play this card");
			return "";
		default:
			System.out.println("error");
			return "error play card with unknown type";
		}
	}
}