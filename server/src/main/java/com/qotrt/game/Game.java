package com.qotrt.game;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.GameOverStoryCard;
import com.qotrt.cards.StoryCard;
import com.qotrt.deck.DeckManager;
import com.qotrt.gameplayer.AIPlayer;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModel;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.DiscardModel;
import com.qotrt.model.EventModel;
import com.qotrt.model.FinalTournamentModel;
import com.qotrt.model.Observable;
import com.qotrt.model.QuestModel;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.TournamentModel;
import com.qotrt.model.UIPlayer;
import com.qotrt.sequence.DiscardSequenceManager;
import com.qotrt.sequence.GameSequenceSimpleFactory;
import com.qotrt.sequence.SequenceManager;
import com.qotrt.util.WebSocketUtil;
import com.qotrt.views.BattlePointsView;
import com.qotrt.views.BoardView;
import com.qotrt.views.DiscardView;
import com.qotrt.views.EventView;
import com.qotrt.views.FinalTournamentView;
import com.qotrt.views.HubView;
import com.qotrt.views.Observer;
import com.qotrt.views.PlayerView;
import com.qotrt.views.QuestView;
import com.qotrt.views.TournamentView;

public class Game extends Observable {

	final static Logger logger = LogManager.getLogger(Game.class);

	private UUID uuid = UUID.randomUUID();
	private int gameSize;
	private String gameName;
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<UIPlayer> players = new ArrayList<UIPlayer>();
	private ArrayList<AIPlayer> aiplayers = new ArrayList<AIPlayer>();
	private PlayerManager pm;
	private HubView hv;
	private RIGGED rigged;
	public BoardModelMediator bmm;
	private Boolean discard;
	private Boolean racing;

	public UUID getUUID() {
		return this.uuid;
	}

	public Game(SimpMessagingTemplate messagingTemplate, 
			String gameName, 
			int capacity, 
			RIGGED rigged, 
			com.qotrt.messages.game.AIPlayer[] aiPlayers2, 
			Boolean discard, 
			Boolean racing) {

		this.messagingTemplate = messagingTemplate;
		this.gameName = gameName;
		this.rigged = rigged;
		this.gameSize = capacity;
		this.discard = discard;
		this.racing = racing;
		for(com.qotrt.messages.game.AIPlayer x: aiPlayers2) {
			logger.info("strategy: " + x.strat);
			aiplayers.add(new AIPlayer(x.strat,this));
		}
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
				fireEvent("gameStart", null, 1);

				for(int i = 0; i < aiplayers.size(); i++) {
					players.add(new UIPlayer("none-matching-session-id", "ai player " + i));
				}

				// model creation
				logger.info("creating models");
				BoardModel bm = new BoardModel();
				DeckManager dm = new DeckManager();
				pm = new PlayerManager(gameSize, 
						players.toArray(new UIPlayer[players.size()]), 
						dm, 
						rigged);
				TournamentModel tm = new TournamentModel();
				QuestModel qm = new QuestModel();
				DiscardModel dmm = new DiscardModel();
				EventModel em = new EventModel();
				FinalTournamentModel ftm = new FinalTournamentModel();
				bmm = new BoardModelMediator(tm, qm, bm, dmm, em, ftm);

				// view creation
				logger.info("creating views");
				Observer pv = new PlayerView(messagingTemplate, players);
				Observer bv = new BoardView(messagingTemplate, players);
				Observer tv = new TournamentView(messagingTemplate, players);
				Observer qv = new QuestView(messagingTemplate, players);
				Observer bpv = new BattlePointsView(messagingTemplate, pm, players);
				Observer ev = new EventView(messagingTemplate, players);
				Observer dv = new DiscardView(messagingTemplate, players);
				Observer ftv = new FinalTournamentView(messagingTemplate, players);

				logger.info("setting up model subscriptions");
				// subscriptions
				logger.info("setting up pm subscription");
				pm.subscribe(pv);
				pm.subscribe(bpv);
				logger.info("setting up bm subscription");
				bm.subscribe(bv);
				bm.subscribe(bpv);
				logger.info("setting up tm subscription");
				tm.subscribe(tv);
				logger.info("setting up qm subscription");
				qm.subscribe(qv);
				qm.subscribe(bpv);
				dmm.subscribe(dv);
				em.subscribe(ev);
				ftm.subscribe(ftv);

				logger.info("creating discard sequence manager: " + discard);
				if(discard) {
					pm.setDiscardSequenceManager(new DiscardSequenceManager(bmm));
				}

				logger.info("starting pm");
				pm.start();

				logger.info("starting AI players");
				for(int i = 0; i < aiplayers.size(); i++) {
					aiplayers.get(i).startAIPlayer(pm.players[pm.players.length - 1 - i], pm,  bmm);
					bm.subscribe(aiplayers.get(i));
					tm.subscribe(aiplayers.get(i));
					qm.subscribe(aiplayers.get(i));
				}

				logger.info("finished setup");

				GameSequenceSimpleFactory gsm = new GameSequenceSimpleFactory();
				while(true) {
					logger.info("Next Turn");
					pm.nextTurn();
					StoryCard s = dm.getStoryCard(1).get(0);
					logger.info("Next card being played: " + s.getName());
					bm.setCard(s);

					SequenceManager sm = gsm.createStoryManager(bm.getCard());
					logger.info("Running: " + sm);
					sm.start(pm, bmm, racing);

					boolean winners = pm.rankUp();
					if(winners) {					
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sm = gsm.createStoryManager(GameOverStoryCard.GAMEOVER);
						sm.start(pm, bmm, racing);
						break;
					}

					logger.info("Waiting for player to continue to next turn");
					// wait for a bit of time then proceed with next turn
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
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
					WebSocketUtil.createHeaders(i.getSessionID()));
		});
	}

	public boolean addPlayer(UIPlayer player) {
		logger.info("trying to add: " + player.getSessionID());
		if(players.size() == gameSize) {
			logger.info("game full not adding");
			return false;
		}

		players.add(player);
		hv.addWebSocket(player);
		fireEvent("players", null, players.toArray(new UIPlayer[players.size()]));

		if(players.size() + aiplayers.size() == gameSize) {
			startGame();
		}

		logger.info("game not full adding player: " + player.getSessionID());
		return true;
	}

	public int getPlayerCount() {
		return this.players.size();
	}

	public int getPlayerCapacity() {
		return this.gameSize;
	}

	public String getGameName() {
		return this.gameName;
	}



	public int getAICount() {
		return aiplayers.size();
	}
}
