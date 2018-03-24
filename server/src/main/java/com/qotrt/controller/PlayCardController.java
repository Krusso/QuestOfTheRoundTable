package com.qotrt.controller;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.qotrt.cards.AdventureCard;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.Player;
import com.qotrt.hub.Hub;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.model.CanPick;


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
		
		HashMap<ZONE, Integer> map = new HashMap<ZONE, Integer>();
		map.put(ZONE.STAGE1, 0);
		map.put(ZONE.STAGE2, 1);
		map.put(ZONE.STAGE3, 2);
		map.put(ZONE.STAGE4, 3);
		map.put(ZONE.STAGE5, 4);
		
		String response = "";
		System.out.println("attempting to play card: " + chatMessage.card + " to zone: " + chatMessage.zoneTo + " from zone: " + chatMessage.zoneFrom);
		if(map.containsKey(chatMessage.zoneFrom) && map.containsKey(chatMessage.zoneTo) && game.bmm.getQuestModel().canPickCardsForStage()){
			response = game.bmm.getQuestModel().attemptMove(map.get(chatMessage.zoneFrom), map.get(chatMessage.zoneTo), chatMessage.card);
		} else if(chatMessage.zoneFrom.equals(ZONE.HAND) && map.containsKey(chatMessage.zoneTo) && game.bmm.getQuestModel().canPickCardsForStage()){
			response = game.bmm.getQuestModel().attemptMove(map.get(chatMessage.zoneTo), player.findCardByID(chatMessage.card));
			// ie valid move
			if(response.equals("")) {
				// stage has already added the reference to the card just need to remove from the hand
				player.getCardByID(chatMessage.card);
			}
		} else if(chatMessage.zoneTo.equals(ZONE.HAND) && map.containsKey(chatMessage.zoneFrom) && game.bmm.getQuestModel().canPickCardsForStage()){
			AdventureCard c = game.bmm.getQuestModel().getCard(chatMessage.card, map.get(chatMessage.zoneTo));
			player.hand.addCard(c);
		} else {
			response = "not a playable zone currently";
		}
		
		checkValidityAndSend(game, player, chatMessage, response);
	}

	@MessageMapping("/game.playForQuest")
	public void playForQuest(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		playCard(game, player, game.bmm.getQuestModel(), chatMessage);
	}
	
	@MessageMapping("/game.playCardTournament")
	public void playCard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		playCard(game, player, game.bmm.getTournamentModel(), chatMessage);
	}
	
	private void playCard(Game game, Player player, CanPick pick, PlayCardClient chatMessage) {
		if(chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				pick.canPick()) {
			String response = verifyFaceDownCard(player, chatMessage.card);
			if(response.equals("")) {
				player.setFaceDown(player.getCardByID(chatMessage.card));
				validMove(game, player, chatMessage);
			} else {
				// error message
				invalidMove(game, player, chatMessage, response);
			}
		} else if(chatMessage.zoneTo.equals(ZONE.HAND) && 
				pick.canPick()) {
			player.setBackToHandFromFaceDown(chatMessage.card);
			validMove(game, player, chatMessage);
		} else {
			// error message
			invalidMove(game, player, chatMessage, "not a playable zone currently");
		}
	}

	
	private void invalidMove(Game game, Player player, PlayCardClient chatMessage, String response) {
		game.sendMessageToAllPlayers("/queue/response", 
				new PlayCardServer(player.getID(), 
						chatMessage.card, 
						chatMessage.zoneFrom, 
						chatMessage.zoneFrom,
						response));
	}
	
	private void validMove(Game game, Player player, PlayCardClient chatMessage) {
		game.sendMessageToAllPlayers("/queue/response", 
				new PlayCardServer(player.getID(), 
						chatMessage.card, 
						chatMessage.zoneFrom, 
						chatMessage.zoneTo,
						""));
	}
	
	
	private void checkValidityAndSend(Game game, Player player, PlayCardClient chatMessage, String response) {
		if(response.equals("")) {
			validMove(game, player, chatMessage);
		} else {
			invalidMove(game, player, chatMessage, response);
		}
	}
	
	private String verifyFaceDownCard(Player player, int card) {
		AdventureCard c = player.findCardByID(card);
		System.out.println("player: " + player.getID());
		System.out.println("card id: " + card);
		System.out.println("card: " + c);
		System.out.println("Player: " + player.getID() + " trying to play: " + c.getName() + " id: " + card);
		return c.playFaceDown(player.getFaceDownDeck(), player.getFaceUp());
	}
}