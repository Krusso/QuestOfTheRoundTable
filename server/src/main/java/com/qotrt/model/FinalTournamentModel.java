package com.qotrt.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.gameplayer.Player;

public class FinalTournamentModel extends Observable implements PropertyChangeListener, CanPick {

	private Confirmation questionCards = new MultiShotConfirmation("questionFinalTournament", 
			null, 
			null);
	
	public FinalTournamentModel() {
		questionCards.subscribe(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		fireEvent(arg0.getPropertyName(), arg0.getOldValue(), arg0.getNewValue());
	}

	public synchronized void setWinners(List<Player> winners) {
		fireEvent("gamewinners", null, 
						winners.stream().mapToInt(i -> i.getID()).toArray());
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
	
	public CountDownLatch questionCards() {
		return questionCards.getCountDownLatch();
	}
}