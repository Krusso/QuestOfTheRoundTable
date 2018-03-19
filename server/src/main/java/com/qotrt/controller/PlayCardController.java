package com.qotrt.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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


	@MessageMapping("/game.playCardQuestSetup")
	public void playCardQuestSetup(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		
		ZONE[] zones = new ZONE[]{ZONE.STAGE1, ZONE.STAGE2, ZONE.STAGE3, ZONE.STAGE4, ZONE.STAGE5};
		Set<ZONE> mySet = new HashSet<ZONE>(Arrays.asList(zones));
		if(mySet.contains(chatMessage.zoneFrom) && mySet.contains(chatMessage.zoneTo)){
			
		} else if(chatMessage.zoneFrom.equals(ZONE.HAND) && mySet.contains(chatMessage.zoneTo)){
			
		} else if(chatMessage.zoneTo.equals(ZONE.HAND) && mySet.contains(chatMessage.zoneFrom)){
			
		} else {
			
		}
		
	}
	
	@MessageMapping("/game.playCardTournament")
	public void playCard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());

		//TODO: find a way to refactor this, maybe put it in the cards themselves?

		if(chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				game.bmm.getTournamentModel().canPick()) {
			String response = verifyFaceDownCard(player, chatMessage.card);
			if(response.equals("")) {
				player.setFaceDown(player.getCardByID(chatMessage.card), ZONE.HAND);
			} else {
				// error message
				game.sendMessageToAllPlayers("/queue/response", 
						new PlayCardServer(player.getID(), 
								chatMessage.card, 
								chatMessage.zoneFrom, 
								chatMessage.zoneFrom, 
								response));
			}
		} else if(chatMessage.zoneTo.equals(ZONE.HAND) && 
				game.bmm.getTournamentModel().canPick()) {
			//TODO: handle this case
			player.setBackToHandFromFaceDown(chatMessage.card);
		} else {
			// error message
			game.sendMessageToAllPlayers("/queue/response", 
					new PlayCardServer(player.getID(), 
							chatMessage.card, 
							chatMessage.zoneFrom, 
							chatMessage.zoneFrom, 
							"not a playable zone currently"));
		}
	}

	private String verifyFaceDownCard(Player player, int card) {
		AdventureCard c = player.findCardByID(card);
		System.out.println("player: " + player.getID());
		System.out.println("card id: " + card);
		System.out.println("card: " + c);
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