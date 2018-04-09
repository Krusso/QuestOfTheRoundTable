package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.deck.DeckManager;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;
import com.qotrt.scenarios.ScenarioMaker;
import com.qotrt.sequence.DiscardSequenceManager;
import com.qotrt.views.Observer;

public class PlayerManager {

	private int actualPlayer = -1;
	private int currentPlayer = -1;
	private DeckManager dm;
	private RIGGED rigged;
	private DiscardSequenceManager dsm;
	
	final static Logger logger = LogManager.getLogger(PlayerManager.class);

	public Player[] players;
	public PlayerManager(int numPlayers, UIPlayer[] uiPlayer, DeckManager dm, RIGGED rigged2) {
		this.players = new Player[numPlayers];
		this.dm = dm;
		this.rigged = rigged2;
		for(int i = 0; i < uiPlayer.length; i++) {
			players[i] = new Player(i, uiPlayer[i]);
		}
	}

	public void start() {
		dm.setRigged(rigged);
		for(int i = players.length; i > 0; i--) {
			logger.info("setting cards for: " + i);
			ScenarioMaker sm = new ScenarioMaker();
			sm.getHandRigged(i,players[i - 1], rigged, dm);
			logger.info("finished setting up cards for: " + i);
		}
	}

	public void setDiscardSequenceManager(DiscardSequenceManager dsm) {
		this.dsm = dsm;
	}
	
	
	public void drawCards(List<Player> players, int cards) {
		players.forEach(player -> {
			player.addCards(dm.getAdventureCard(cards));
		});
		if(dsm != null) dsm.start(this, null, false);
	}
	
	public void drawCards(Player player, int cards) {
		if(dm != null) {
			player.addCards(dm.getAdventureCard(cards));	
		}
	}
	
	public void setPlayer(Player playerFind) {
		for(int i = 0; i < players.length; i++) {
			if(players[i]== playerFind) {
				if(currentPlayer == i) return;
				currentPlayer = i;
			}
		}
	}

	public void nextTurn() {
		actualPlayer++;
		if(actualPlayer >= players.length) {
			actualPlayer = 0;
		}
		currentPlayer = actualPlayer;
	}

	public Iterator<Player> round(){
		List<Player> list = new ArrayList<Player>(Arrays.asList(players)).subList(actualPlayer, players.length);
		list.addAll(new ArrayList<Player>(Arrays.asList(players)).subList(0, actualPlayer));
		return list.iterator();
	}

	public void flipCards(Iterator<Player> players) {
		players.forEachRemaining(i -> i.flipCards());
	}

	public void changeShields(List<Player> winners, int shields) {
		winners.forEach(i -> i.changeShields(shields));
	}


	public void discardCards(List<Player> participants) {
		participants.forEach(i -> {
			i.discardType(TYPE.WEAPONS);
			i.discardType(TYPE.AMOUR);
		});
	}

	public void discardWeapons(List<Player> participants) {
		participants.forEach(i -> {
			i.discardType(TYPE.WEAPONS);
		});
	}

	public void discardAllies(List<Player> participants) {
		participants.forEach(i -> {
			i.discardType(TYPE.ALLIES);
		});
	}

	public void discardFromHand(Player player, String[] cards) {
		dm.addAdventureCard(player.removeCards(cards));
	}

	public boolean rankUp() {
		AtomicBoolean winners = new AtomicBoolean();
		round().forEachRemaining(player ->{
			player.increaseLevel();
			if(player.getRank() == Rank.RANKS.KNIGHTOFTHEROUNDTABLE) {
				winners.set(true);
			}
		});
		return winners.get();
	}

	public void subscribe(Observer pv) {
		for(Player p: players) {
			logger.info("player subscription: " + pv + " p: " + p);
			p.subscribe(pv);
		}
		logger.info("finished setting up player subscriptions");
	}

	public Boolean iseultExists() {
		for(Player p: players) {
			for(AdventureCard c: p.getFaceUp().getDeck()) {
				if(c.getName().equals("Queen Iseult")) {
					return true;
				}
			}
		}
		return false;
	}

}
