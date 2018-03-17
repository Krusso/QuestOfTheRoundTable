package com.qotrt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.qotrt.cards.AdventureCard;
import com.qotrt.gameplayer.Player;

public class QuestModel extends Observable {

	public CountDownLatch cdl = new CountDownLatch(1);
	private int questionSponsor = 0;
	private List<Player> questionSponsorPlayers = new ArrayList<Player>();
	private List<Player> questionCantSponsorPlayers = new ArrayList<Player>();
	private Player sponsor = null;
	
	private List<Player> questionAcceptQuest = new ArrayList<Player>();
	private int questionQuest = 0;
			
	private List<Player> winners = new ArrayList<Player>();
	private String message;
	
	public synchronized void questionSponsorPlayers(List<Player> potentialSponsors, List<Player> cantSponsor) {
		this.cdl = new CountDownLatch(1);
		this.questionSponsorPlayers = potentialSponsors;
		this.questionCantSponsorPlayers = cantSponsor;
		this.questionSponsor = this.questionSponsorPlayers.size();
		fireEvent("questionSponsor", null,
				new GenericPair(this.questionSponsorPlayers.stream().mapToInt(i -> i.getID()).toArray(),
						this.questionCantSponsorPlayers.stream().mapToInt(i -> i.getID())));
	}
	
	public synchronized void acceptSponsor(Player player) {
		System.out.println("player: " + player + " attempting to sponsor");
		if(questionSponsor > 0) {
			questionSponsor = 0;
			System.out.println("player: " + player.getID() + " sponsored quest");
			this.sponsor = player;
			fireEvent("jointournament", null, player);
			checkIfCanOpenLatch(cdl, questionSponsor);
		} else {
			System.out.println("player: " + player + " attempted to sponsor too late");
		}
	}
	
	public synchronized void declineSponsor(Player player) {
		System.out.println("player: " + player + " attempting to decline sponsorship");
		if(questionSponsor > 0) {
			questionSponsor--;
			System.out.println("player: " + player + " declined sponsorship");
			checkIfCanOpenLatch(cdl, questionSponsor);
		} else {
			System.out.println("player: " + player + " decline sponsorship too late");
		}
	}
	
	public synchronized Player getPlayerWhoSponsor() {
		System.out.println("Getting players who sponsor the quest");
		System.out.println("Players: " + sponsor + " " + (sponsor != null ? sponsor.getID() : " "));
		questionSponsor = -1;
		return this.sponsor;
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

	public List<List<AdventureCard>> getStageCards() {
		// TODO Auto-generated method stub
		return null;
	}

	public synchronized void questionJoinQuest(List<Player> potentialQuestPlayers) {
		this.cdl = new CountDownLatch(1);
		this.questionAcceptQuest = potentialQuestPlayers;
		this.questionQuest = potentialQuestPlayers.size();
		fireEvent("questionQuest", 
				null,
				this.questionSponsorPlayers.stream().mapToInt(i -> i.getID()).toArray());
	
	}

	public synchronized void acceptQuest(Player player) {
		System.out.println("player: " + player + " attempting to accept quest");
		if(questionQuest > 0) {
			questionQuest = 0;
			System.out.println("player: " + player.getID() + " accept quest");
			this.questionAcceptQuest.add(player);
			fireEvent("joinquest", null, player);
			checkIfCanOpenLatch(cdl, questionQuest);
		} else {
			System.out.println("player: " + player + " attempted to accept quest too late");
		}
	}
	
	public synchronized void declineQuest(Player player) {
		System.out.println("player: " + player + " attempting to decline quest");
		if(questionQuest > 0) {
			questionQuest--;
			System.out.println("player: " + player + " decline quest");
			checkIfCanOpenLatch(cdl, questionQuest);
		} else {
			System.out.println("player: " + player + " decline quest too late");
		}
	}

	public List<Player> playerWhoJoined() {
		System.out.println("Getting players who joined the quest");
		System.out.println("Players: " + questionAcceptQuest.stream().map(i -> i.getID()).collect(Collectors.toList()));
		questionQuest = -1;
		return this.questionAcceptQuest;
	}

	public void passStage(List<Player> winners2) {
		// TODO Auto-generated method stub
		
	}

	public void questionCardsStage(List<Player> winners2) {
		// TODO Auto-generated method stub
		
	}

	public void finishPicking() {
		// TODO Auto-generated method stub
		
	}

	public void flipStage() {
		// TODO Auto-generated method stub
		
	}

	public void questionBid(List<Player> winners2) {
		// TODO Auto-generated method stub
		
	}

	public Player getBidWinners() {
		// TODO Auto-generated method stub
		return null;
	}

	public void discardCards(Player bidWinner) {
		// TODO Auto-generated method stub
		
	}

	public String[] getDiscardCards() {
		// TODO Auto-generated method stub
		return null;
	}
	
}