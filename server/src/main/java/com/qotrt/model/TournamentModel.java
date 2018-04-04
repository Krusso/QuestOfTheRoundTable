package com.qotrt.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.tournament.TournamentWinServer.WINTYPES;

public class TournamentModel extends Observable implements PropertyChangeListener, CanPick {

	private Confirmation join;
	
	private Confirmation questionCards;
	
	public TournamentModel(Boolean racing) {
		
		questionCards = new MultiShotConfirmation("questioncardtournament", 
				null, null, racing);
		
		join = new MultiShotConfirmation("questiontournament", 
				"jointournament", 
				"declinetournament", racing);
		
		join.subscribe(this);
		questionCards.subscribe(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		fireEvent(arg0.getPropertyName(), arg0.getOldValue(), arg0.getNewValue());
	}
	
	private WINTYPES message;

	public synchronized void setMessage(WINTYPES win) {
		this.message = win;
	}

	public synchronized void questionJoinPlayers(Iterator<Player> players) {
		List<Player> x = new ArrayList<Player>();
		players.forEachRemaining(i -> x.add(i));
		join.start(x);
	}

	public synchronized void acceptTournament(Player player) {
		join.accept(player, "player: " + player + " attempting to join",
				"player: " + player.getID() + " joined tournament",
				"player: " + player + " joined tournament too late");
	}

	public synchronized void declineTournament(Player player) {
		join.decline(player, "player: " + player + " attempting to decline",
				"player: " + player + " decline tournament",
				"player: " + player + " decline tournament too late");
	}

	public synchronized List<Player> playersWhoJoined(){
		logger.info("Getting players who joined the tournament");
		List<Player> x = join.get();
		logger.info("Players: " + x.stream().map(i -> i.getID()).collect(Collectors.toList()));
		return x;
	}

	public synchronized void setWinners(List<Player> winners) {
		fireEvent("tournamentwinners", null, 
				new GenericPair(
						winners.stream().mapToInt(i -> i.getID()).toArray(),
						this.message));
	}
	
	public void questionCards(List<Player> toQuestion) {
		questionCards.start(toQuestion);
	}
	
	public void finishSelectingCards(Player player) {
		questionCards.accept(player, "player: " + player + " attempted to finish selecting cards", 
				"player: " + player + " finished selecting cards", 
				"player: " + player + " finish selecting cards too late");
	}
	
	public synchronized void finishPicking() {
		questionCards.get();
	}
	
	public synchronized boolean canPick() {
		return questionCards.can();
	}
	
	public CountDownLatch join() {
		return join.getCountDownLatch();
	}
	
	public CountDownLatch questionCards() {
		return questionCards.getCountDownLatch();
	}
}