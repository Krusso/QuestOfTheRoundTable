package com.qotrt.game;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MimeTypeUtils;

import com.qotrt.cards.StoryCard;
import com.qotrt.deck.DeckManager;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModel;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.Observable;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.TournamentModel;
import com.qotrt.model.UIPlayer;
import com.qotrt.sequence.GameSequenceSimpleFactory;
import com.qotrt.sequence.SequenceManager;
import com.qotrt.views.BoardView;
import com.qotrt.views.HubView;
import com.qotrt.views.PlayerView;
import com.qotrt.views.TournamentView;

public class Game extends Observable {

	final static Logger logger = LogManager.getLogger(Game.class);

	private UUID uuid = UUID.randomUUID();
	private int gameSize;
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<UIPlayer> players = new ArrayList<UIPlayer>();
	private PlayerManager pm;
	private HubView hv;
	private RIGGED rigged;
	public BoardModelMediator bmm;

	public UUID getUUID() {
		return this.uuid;
	}

	public Game(SimpMessagingTemplate messagingTemplate, int capacity, RIGGED rigged) {
		this.messagingTemplate = messagingTemplate;
		this.rigged = rigged;
		this.gameSize = capacity;
		logger.info("messaging template: " + this.messagingTemplate);
		hv = new HubView(this.messagingTemplate);
		subscribe(hv);
	}

	public Player getPlayerBySessionID(String sessionID) {
		for(Player player: pm.players) {
			if(player.compareSessionID(sessionID)) {
				return player;
			}
		}
		return null;
	}

	public void startGame() {
		Executors.newScheduledThreadPool(1).execute(new Runnable() {
			@Override
			public void run() {
				logger.info("Starting game");

				// model creation
				BoardModel bm = new BoardModel();
				DeckManager dm = new DeckManager();
				pm = new PlayerManager(gameSize, 
						players.toArray(new UIPlayer[players.size()]), 
						dm, 
						rigged);
				TournamentModel tm = new TournamentModel();
				bmm = new BoardModelMediator(tm);

				// view creation
				PlayerView pv = new PlayerView(messagingTemplate);
				BoardView bv = new BoardView(messagingTemplate);
				TournamentView tv = new TournamentView(messagingTemplate);

				//subscriptions
				players.forEach(i -> { 
					pv.addWebSocket(i);
					bv.addWebSocket(i);
					tv.addWebSocket(i);
				});

				pm.subscribe(pv);
				bm.subscribe(bv);
				tm.subscribe(tv);

				pm.start();

				GameSequenceSimpleFactory gsm = new GameSequenceSimpleFactory();
				while(true) {
					logger.info("Next Turn");
					pm.nextTurn();
					StoryCard s = dm.getStoryCard(1).get(0);
					logger.info("Next card being played: " + s.getName());
					bm.setCard(s);

					SequenceManager sm = gsm.createStoryManager(bm.getCard());
					sm.start(pm, bmm);

					boolean winners = pm.rankUp();
					if(winners) {
						//pvs.joinFinalTournament(pm.getAllWithState(Player.STATE.WINNING), Player.STATE.WINNING);
						sm = gsm.createStoryManager(StoryCard.GAMEOVER);
						//sm.start(actions, pm, bm);
						break;
					}

					logger.info("Waiting for player to continue to next turn");
					// wait for a bit of time then proceed with next turn
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				logger.info("Game is done");
			}
		});
	}

	public void sendMessageToAllPlayers(String destination, Object objectToSend) {
		players.forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(), 
					destination, 
					objectToSend, 
					createHeaders(i.getSessionID()));
		});
	}

	// TODO: move to some util class
	private MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		headerAccessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
		return headerAccessor.getMessageHeaders();
	}

	public boolean addPlayer(UIPlayer player) {

		if(players.size() == gameSize) {
			return false;
		}

		players.add(player);
		hv.addWebSocket(player);
		fireEvent("players", null, players.toArray(new UIPlayer[players.size()]));

		if(players.size() == gameSize) {
			startGame();
		}

		return true;
	}

	public int getPlayerCount() {
		return this.players.size();
	}

	public int getPlayerCapacity() {
		return this.gameSize;
	}
}
