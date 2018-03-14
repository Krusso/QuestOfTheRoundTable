package com.qotrt.controller;

import java.util.concurrent.atomic.AtomicInteger;

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


@Controller
public class PlayCardController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private Hub hub;

	private Game game;
	private Player player;

	@PostConstruct
	private void init() {
		System.out.println("created 2");
	}

	@PreDestroy
	private void destroy() {
		//activeSessionManager.removeListener(this);
		System.out.println("deleted");
	}

	private void setPlayerGame(SimpMessageHeaderAccessor headerAccessor) {
		this.game = hub.getGameBySessionID(headerAccessor.getSessionId());
		this.player = game.getPlayerBySessionID(headerAccessor.getSessionId());
	}
	
	@MessageMapping("/game.playCard")
	public void playCard(SimpMessageHeaderAccessor headerAccessor, 
			@Payload PlayCardClient chatMessage) {
		setPlayerGame(headerAccessor);
		if(chatMessage.zone.equals(ZONE.FACEDOWN)) {
			if(verifyFaceDownCard(player, chatMessage.card)) {
				player.setFaceDown(player.findCardByID(chatMessage.card));
			}
		} else if(chatMessage.zone.equals(ZONE.FACEUP)) {
			//TODO: handle this case
		} else if(chatMessage.zone.equals(ZONE.DISCARD)) {
			//TODO: handle this case
		} else if(chatMessage.zone.equals(ZONE.HAND)) {
			//TODO: handle this case
		} else if(chatMessage.zone.equals(ZONE.STAGE1)) {
			//TODO: handle this case
		} else if(chatMessage.zone.equals(ZONE.STAGE2)) {
			//TODO: handle this case
		} else if(chatMessage.zone.equals(ZONE.STAGE3)) {
			//TODO: handle this case
		} else if(chatMessage.zone.equals(ZONE.STAGE4)) {
			//TODO: handle this case			
		} else if(chatMessage.zone.equals(ZONE.STAGE5)) {
			//TODO: handle this case
		}
	}

	private boolean verifyFaceDownCard(Player player, int card) {
		AdventureCard c = player.findCardByID(card);
		switch(c.getType()) {
		case ALLIES:
			return true;
		case AMOUR:
			for(AdventureCard a: player.getFaceDownDeck().getDeck()) {
				if(a.getType() == TYPE.AMOUR) {
					return false;
				}
			}
			return true;
		case FOES:
			return false;
		case TESTS:
			return false;
		case WEAPONS:
			for(AdventureCard a: player.getFaceDownDeck().getDeck()) {
				if(a.getName().equals(c.getName())) {
					return false;
				}
			}
			return true;
		default:
			return false;
		}
	}
}