package com.qotrt.controller;

import java.util.HashMap;

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
import com.qotrt.model.CanPick;
import com.qotrt.model.DiscardModel;
import com.qotrt.model.EventModel;


@Controller
public class PlayCardController {

	@Autowired
	private Hub hub;

	@MessageMapping("/game.discardFullHand")
	public void discardFull(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		
		
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		DiscardModel dm = game.bmm.getDiscardModel();
		String response = "";
		
		if(dm.can() && 
				chatMessage.zoneTo.equals(ZONE.DISCARD) && 
				chatMessage.zoneFrom.equals(ZONE.HAND)) {
			response = dm.playCard(player, chatMessage.card, player.hand);
		} else if(dm.can() && 
				chatMessage.zoneTo.equals(ZONE.DISCARD) && 
				chatMessage.zoneFrom.equals(ZONE.FACEDOWN)) {
			response = dm.playCard(player, chatMessage.card, player.getFaceDownDeck());
		} else if(dm.can() && 
				chatMessage.zoneTo.equals(ZONE.HAND) && 
				chatMessage.zoneFrom.equals(ZONE.DISCARD)) {
			player.hand.addCard(dm.getCard(player, chatMessage.card));
		} else if(dm.can() && 
				chatMessage.zoneTo.equals(ZONE.HAND) && 
				chatMessage.zoneFrom.equals(ZONE.FACEDOWN)) {
			player.hand.addCard(player.getFaceDownDeck().getCardByID(chatMessage.card));
		} else if(dm.can() && 
				chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				chatMessage.zoneFrom.equals(ZONE.DISCARD)) {
			
			
			AdventureCard c = dm.findCard(player, chatMessage.card);
			response = c.playFaceDown(player.getFaceDownDeck(), player.getFaceUp());
			if(!response.equals("") || !c.getType().equals(TYPE.AMOUR) || c.getType().equals(TYPE.ALLIES)) {
				response = "can only play amour or allies face down during discarding";
			} else {
				player.getFaceDownDeck().addCard(dm.getCard(player, chatMessage.card));
			}
			
			
		} else if(dm.can() && 
				chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				chatMessage.zoneFrom.equals(ZONE.HAND)) {

			AdventureCard c = player.hand.findCardByID(chatMessage.card);
			response = c.playFaceDown(player.getFaceDownDeck(), player.getFaceUp());
			if(!response.equals("") || !c.getType().equals(TYPE.AMOUR) || c.getType().equals(TYPE.ALLIES)) {
				response = "can only play amour or allies face down during discarding";
			} else {
				player.getFaceDownDeck().addCard(player.hand.getCardByID(chatMessage.card));
			}
			
		} else {
			response = "not a playable zone currently";
		}

		checkValidityAndSend(game, player, chatMessage, response);
	}
	
	@MessageMapping("/game.discardEvent")
	public void discardEvent(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		EventModel em = game.bmm.getEventModel();
		String response = "";
		
		if(em.can() && 
				chatMessage.zoneTo.equals(ZONE.DISCARD) && 
				chatMessage.zoneFrom.equals(ZONE.HAND)) {
			response = em.playCard(player, chatMessage.card, player.hand);
		} else if(em.can() && 
				chatMessage.zoneTo.equals(ZONE.DISCARD) && 
				chatMessage.zoneFrom.equals(ZONE.FACEDOWN)) {
			response = em.playCard(player, chatMessage.card, player.getFaceDownDeck());
		} else if(em.can() && 
				chatMessage.zoneTo.equals(ZONE.HAND) && 
				chatMessage.zoneFrom.equals(ZONE.DISCARD)) {
			player.hand.addCard(em.getCard(player, chatMessage.card));
		} else if(em.can() && 
				chatMessage.zoneTo.equals(ZONE.HAND) && 
				chatMessage.zoneFrom.equals(ZONE.FACEDOWN)) {
			player.hand.addCard(player.getFaceDownDeck().getCardByID(chatMessage.card));
		} else if(em.can() && 
				chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				chatMessage.zoneFrom.equals(ZONE.DISCARD)) {
			
			
			AdventureCard c = em.findCard(player, chatMessage.card);
			response = c.playFaceDown(player.getFaceDownDeck(), player.getFaceUp());
			if(!response.equals("") || !c.getType().equals(TYPE.AMOUR) || c.getType().equals(TYPE.ALLIES)) {
				response = "can only play amour or allies face down during discarding";
			} else {
				player.getFaceDownDeck().addCard(em.getCard(player, chatMessage.card));
			}
			
			
		} else if(em.can() && 
				chatMessage.zoneTo.equals(ZONE.FACEDOWN) && 
				chatMessage.zoneFrom.equals(ZONE.HAND)) {

			AdventureCard c = player.hand.findCardByID(chatMessage.card);
			response = c.playFaceDown(player.getFaceDownDeck(), player.getFaceUp());
			if(!response.equals("") || !c.getType().equals(TYPE.AMOUR) || c.getType().equals(TYPE.ALLIES)) {
				response = "can only play amour or allies face down during discarding";
			} else {
				player.getFaceDownDeck().addCard(player.hand.getCardByID(chatMessage.card));
			}
			
		} else {
			response = "not a playable zone currently";
		}

		checkValidityAndSend(game, player, chatMessage, response);
	}
	
	@MessageMapping("/game.discardBid")
	public void discard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		Game game = hub.getGameBySessionID(headerAccessor.getSessionId());
		Player player = game.getPlayerBySessionID(headerAccessor.getSessionId());
		String response = "";

		if(game.bmm.getQuestModel().canDiscard() && 
				chatMessage.zoneTo.equals(ZONE.DISCARD) && chatMessage.zoneFrom.equals(ZONE.HAND)) {
			game.bmm.getQuestModel().addDiscard(player.getCardByID(chatMessage.card));
		} else if(game.bmm.getQuestModel().canDiscard() && 
				chatMessage.zoneTo.equals(ZONE.HAND) && chatMessage.zoneFrom.equals(ZONE.DISCARD)) {
			player.hand.addCard(game.bmm.getQuestModel().getDiscardCard(chatMessage.card));
		} else {
			response = "not a playable zone currently";
		}

		checkValidityAndSend(game, player, chatMessage, response);
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
		if(map.containsKey(chatMessage.zoneFrom) && 
				map.containsKey(chatMessage.zoneTo) && 
				game.bmm.getQuestModel().canPickCardsForStage()){
			response = game.bmm.getQuestModel().attemptMove(map.get(chatMessage.zoneFrom), map.get(chatMessage.zoneTo), chatMessage.card);
		} else if(chatMessage.zoneFrom.equals(ZONE.HAND) && 
				map.containsKey(chatMessage.zoneTo) && 
				game.bmm.getQuestModel().canPickCardsForStage()){
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
				chatMessage.zoneFrom.equals(ZONE.HAND) && pick.canPick()) {
			String response = verifyFaceDownCard(player, chatMessage.card);
			if(response.equals("")) {
				player.setFaceDown(player.getCardByID(chatMessage.card));
				validMove(game, player, chatMessage);
			} else {
				// error message
				invalidMove(game, player, chatMessage, response);
			}
		} else if(chatMessage.zoneTo.equals(ZONE.HAND) && 
				chatMessage.zoneFrom.equals(ZONE.FACEDOWN) && pick.canPick()) {
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