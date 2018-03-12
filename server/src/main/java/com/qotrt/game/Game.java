package com.qotrt.game;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.cards.StoryCard;
import com.qotrt.deck.DeckManager;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModel;
import com.qotrt.model.Observable;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;
import com.qotrt.sequence.GameSequenceSimpleFactory;
import com.qotrt.sequence.SequenceManager;
import com.qotrt.views.HubView;

public class Game extends Observable {

	private int gameSize = 2;
	private ArrayList<UIPlayer> players = new ArrayList<UIPlayer>();
	private PlayerManager pm;
	private HubView hv;

	public Game(SimpMessagingTemplate messagingTemplate) {
		hv = new HubView(messagingTemplate);
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
				while(true) {
					System.out.println("Starting game");

					BoardModel bm = new BoardModel();
					DeckManager dm = new DeckManager();
					// TODO: set rigged correctly
					// TODO: set players correctly
					pm = new PlayerManager(gameSize, 
							players.toArray(new UIPlayer[players.size()]), 
							dm, 
							RIGGED.NORMAL);

					pm.start();

					GameSequenceSimpleFactory gsm = new GameSequenceSimpleFactory();
					while(true) {
						System.out.println("Next Turn");
						pm.nextTurn();
						StoryCard s = dm.getStoryCard(1).get(0);
						System.out.println("Next card being played: " + s.getName());
						bm.setCard(s);

						SequenceManager sm = gsm.createStoryManager(bm.getCard());
						//sm.start(actions, pm, bm);

						boolean winners = pm.rankUp();
						if(winners) {
							//pvs.joinFinalTournament(pm.getAllWithState(Player.STATE.WINNING), Player.STATE.WINNING);
							sm = gsm.createStoryManager(StoryCard.GAMEOVER);
							//sm.start(actions, pm, bm);
							break;
						}

						System.out.println("Waiting for player to continue to next turn");
						// wait for a bit of time then proceed with next turn
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					System.out.println("Game is done");
				}
			}
		});
	}

	public void addPlayer(UIPlayer player) {
		players.add(player);
		fireEvent("players", null, players.toArray(new UIPlayer[players.size()]));

		if(players.size() == gameSize) {
			startGame();
		}
	}
}
