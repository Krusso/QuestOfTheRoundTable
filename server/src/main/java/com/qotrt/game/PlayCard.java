package com.qotrt.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qotrt.cards.AdventureCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.model.CanPick;
import com.qotrt.model.Discard;

@Component
public class PlayCard {

	final static Logger logger = LogManager.getLogger(PlayCard.class);
	
	public String discardCard(Game game, Player player, Discard dm, PlayCardClient chatMessage) {
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
		} else {
			response = "not a playable zone currently";
		}

		return response;
	}
	
	public void playCard(Game game, Player player, CanPick pick, PlayCardClient chatMessage) {
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


	public void invalidMove(Game game, Player player, PlayCardClient chatMessage, String response) {
		game.sendMessageToAllPlayers("/queue/response", 
				new PlayCardServer(player.getID(), 
						chatMessage.card, 
						chatMessage.zoneFrom, 
						chatMessage.zoneFrom,
						response));
	}

	public void validMove(Game game, Player player, PlayCardClient chatMessage) {
		if(game.bmm != null && game.bmm.getBoardModel() != null) {
			game.bmm.getBoardModel().fireMordred();	
		}
		game.sendMessageToAllPlayers("/queue/response", 
				new PlayCardServer(player.getID(), 
						chatMessage.card, 
						chatMessage.zoneFrom, 
						chatMessage.zoneTo,
						""));
	}


	public void checkValidityAndSend(Game game, Player player, PlayCardClient chatMessage, String response) {
		if(response.equals("")) {
			validMove(game, player, chatMessage);
		} else {
			invalidMove(game, player, chatMessage, response);
		}
	}

	public String verifyFaceDownCard(Player player, int card) {
		AdventureCard c = player.findCardByID(card);
		logger.info("player: " + player.getID());
		logger.info("card id: " + card);
		logger.info("card: " + c);
		logger.info("Player: " + player.getID() + " trying to play: " + c.getName() + " id: " + card);
		return c.playFaceDown(player.getFaceDownDeck(), player.getFaceUp());
	}
	
}
