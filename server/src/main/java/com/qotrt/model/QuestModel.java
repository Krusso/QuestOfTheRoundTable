package com.qotrt.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qotrt.cards.AdventureCard;
import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.SingleShotConfirmation;
import com.qotrt.gameplayer.Player;
import com.qotrt.sequence.Quest;
import com.qotrt.util.PlayerUtil;

public class QuestModel extends Observable implements PropertyChangeListener {

	
	private Confirmation sponsor = new SingleShotConfirmation("questionSponsor", 
			"jointournament", 
			"declinetournament");
	
	private Confirmation participate = new SingleShotConfirmation("questionQuest", 
			"joinquest", 
			null);
	
	private List<Player> participatents = new ArrayList<Player>();
	private Quest quest;
	
	
	public QuestModel() {
		sponsor.subscribe(this);
		participate.subscribe(this);
		cards.subscribe(this);
	}
	
	public void setQuest(Quest quest) {
		this.quest = quest;
	}
	
	@Override
	//propagating events up
	public void propertyChange(PropertyChangeEvent arg0) {
		fireEvent(arg0.getPropertyName(), arg0.getOldValue(), arg0.getNewValue());
	}
			
	private List<Player> winners = new ArrayList<Player>();
	private String message;
	
	public synchronized void questionSponsorPlayers(List<Player> potentialSponsors) {
		sponsor.start(potentialSponsors);
	}
	
	public synchronized void acceptSponsor(Player player) {
		sponsor.accept(player, "player: " + player + " attempting to sponsor",
				"player: " + player.getID() + " sponsored quest", 
				"player: " + player + " attempted to sponsor too late");
	}
	
	public synchronized void declineSponsor(Player player) {
		sponsor.decline(player, "player: " + player + " attempting to decline sponsorship",
				"player: " + player + " declined sponsorship", 
				"player: " + player + " decline sponsorship too late");
	}
	
	public synchronized List<Player> getPlayerWhoSponsor() {
		return sponsor.get();
	}
	
	public synchronized void setMessage(String message) {
		this.message = message;
	}
	
	public synchronized void setWinners(List<Player> winners) {
		this.winners = winners;
		fireEvent("questWinners", null, 
				new GenericPair(
						this.winners.stream().mapToInt(i -> i.getID()).toArray(),
						this.message));
	}

	public synchronized List<List<AdventureCard>> getStageCards() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public synchronized void questionJoinQuest(List<Player> potentialQuestPlayers) {
		participate.start(potentialQuestPlayers);
	}

	public synchronized void acceptQuest(Player player) {
		participate.accept(player, "player: " + player + " attempting to accept quest",
				"player: " + player.getID() + " accept quest",
				"player: " + player + " attempted to accept quest too late");
	}
	
	public synchronized void declineQuest(Player player) {
		participate.decline(player, "player: " + player + " attempting to decline quest",
				"player: " + player + " decline quest", 
				"player: " + player + " decline quest too late");
	}

	public synchronized List<Player> playerWhoJoined() {
		System.out.println("Getting players who joined the quest");
		List<Player> x = participate.get();
		System.out.println("Players: " + Arrays.toString(PlayerUtil.playersToIDs(x)));
		participatents = x;
		return x;
	}

	public synchronized void passStage(List<Player> passedStage) {
		participatents = passedStage;
		fireEvent("passStage", null, PlayerUtil.playersToIDs(passedStage));
	}

	private Confirmation cards = new SingleShotConfirmation("questionCardQuest", 
			"cardQuest", 
			null);
	
	public synchronized void questionCardsStage() {
		cards.start(participatents);
	}

	public synchronized void finishSelectingCards(Player player) {
		cards.accept(player, "player: " + player + " attempted to finish selecting cards", 
				"player: " + player + " finished selecting cards", 
				"player: " + player + " finish selecting cards too late");
	}
	
	public synchronized void finishPicking() {
		cards.get();
	}

	public synchronized void flipStage() {
		// TODO Auto-generated method stub
		
	}

	public synchronized void questionBid(List<Player> winners2) {
		// TODO Auto-generated method stub
		
	}

	public synchronized Player getBidWinners() {
		// TODO Auto-generated method stub
		return null;
	}

	public synchronized void discardCards(Player bidWinner) {
		// TODO Auto-generated method stub
		
	}

	public synchronized String[] getDiscardCards() {
		// TODO Auto-generated method stub
		return null;
	}
	
}