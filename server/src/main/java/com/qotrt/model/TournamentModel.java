package com.qotrt.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.qotrt.gameplayer.Player;

public class TournamentModel extends Observable{

	public CountDownLatch cdl = new CountDownLatch(1);
	private int questioned = 0;
	private List<Player> questionJoinPlayers = new ArrayList<Player>();
	private List<Player> joinPlayers = new ArrayList<Player>();
	private List<Player> winners = new ArrayList<Player>();
	private String message;

	public void clearList() {
		questionJoinPlayers.clear();
	}

	public synchronized void setMessage(String message) {
		this.message = message;
	}
	
	public synchronized void questionJoinPlayers(Iterator<Player> players) {
		players.forEachRemaining(i -> questionJoinPlayers.add(i));
		questioned = questionJoinPlayers.size();
		fireEvent("questiontournament", null, questionJoinPlayers);
	}

	public synchronized void acceptTournament(Player player) {
		System.out.println("player: " + player + " attempting to join");
		if(questioned > 0) {
			questioned--;
			System.out.println("player: " + player + " joined tournament");
			joinPlayers.add(player);
			fireEvent("jointournament", null, player);
			checkIfCanOpenLatch();
		} else {
			System.out.println("player: " + player + " joined tournament too late");
		}
	}
	
	public synchronized void declineTournament(Player player) {
		System.out.println("player: " + player + " declined tournament");
		questioned--;
		checkIfCanOpenLatch();
	}
	
	private void checkIfCanOpenLatch() {
		if(questioned == 0) {
			cdl.countDown();
		}
	}
	
	public synchronized List<Player> playersWhoJoined(){
		System.out.println("Getting players who joined the tournament");
		System.out.println("Players: " + joinPlayers);
		questioned = -1;
		return this.joinPlayers;
	}

	public synchronized void setWinners(List<Player> winners) {
		this.winners = winners;
		this.joinPlayers = winners;
		fireEvent("tournamentwinners", null, new Pair(this.winners, this.message));
	}

	public void questionCards() {
		fireEvent("questioncardtournament", null, joinPlayers);
	}
}

class Pair {
	public List<Player> players;
	public String message;
	
	public Pair(List<Player> players, String message) {
		this.players = players;
		this.message = message;
	}
}