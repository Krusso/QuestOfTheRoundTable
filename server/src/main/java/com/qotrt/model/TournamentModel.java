package com.qotrt.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

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
		cdl = new CountDownLatch(1);
		players.forEachRemaining(i -> questionJoinPlayers.add(i));
		questioned = questionJoinPlayers.size();
		fireEvent("questiontournament", null, 
				questionJoinPlayers.stream().mapToInt(i -> i.getID()).toArray());
	}

	public synchronized void acceptTournament(Player player) {
		System.out.println("player: " + player + " attempting to join");
		if(questioned > 0) {
			questioned--;
			System.out.println("player: " + player.getID() + " joined tournament");
			joinPlayers.add(player);
			fireEvent("jointournament", null, player);
			checkIfCanOpenLatch();
		} else {
			System.out.println("player: " + player + " joined tournament too late");
		}
	}

	public synchronized void declineTournament(Player player) {
		System.out.println("player: " + player + " attempting to decline");
		if(questioned > 0) {
			questioned--;
			System.out.println("player: " + player + " decline tournament");
			fireEvent("declinetournament", null, player);
			checkIfCanOpenLatch();
		} else {
			System.out.println("player: " + player + " decline tournament too late");
		}
	}

	private void checkIfCanOpenLatch() {
		if(questioned == 0) {
			cdl.countDown();
		}
	}

	public synchronized List<Player> playersWhoJoined(){
		System.out.println("Getting players who joined the tournament");
		System.out.println("Players: " + joinPlayers.stream().map(i -> i.getID()).collect(Collectors.toList()));
		questioned = -1;
		return this.joinPlayers;
	}

	public synchronized void setWinners(List<Player> winners) {
		this.winners = winners;
		this.joinPlayers = winners;
		fireEvent("tournamentwinners", null, 
				new GenericPair(
						this.winners.stream().mapToInt(i -> i.getID()).toArray(),
						this.message));
	}

	public void questionCards() {
		fireEvent("questioncardtournament", null, joinPlayers);
	}
}