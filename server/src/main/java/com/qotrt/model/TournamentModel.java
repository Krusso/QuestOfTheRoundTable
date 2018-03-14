package com.qotrt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.qotrt.gameplayer.Player;

public class TournamentModel extends Observable{

	private List<Player> questionJoinPlayers = new ArrayList<Player>();
	private List<Player> joinPlayers = new ArrayList<Player>();
	private List<Player> winners = new ArrayList<Player>();
	private String message;
	private Boolean question = false;

	public void clearList() {
		questionJoinPlayers.clear();
	}

	public synchronized void setMessage(String message) {
		this.message = message;
	}
	
	public synchronized void questionJoinPlayers(Iterator<Player> players) {
		question = true;
		players.forEachRemaining(i -> questionJoinPlayers.add(i));
		fireEvent("questiontournament", null, 
				questionJoinPlayers.stream().mapToInt(i -> i.getID()).toArray());
	}

	public synchronized void acceptTournament(Player player) {
		System.out.println("player: " + player + " attempting to join");
		if(question) {
			System.out.println("player: " + player + " joined tournament");
			joinPlayers.add(player);
			fireEvent("jointournament", null, player);
		} else {
			System.out.println("player: " + player + " joined tournament too late");
		}
	}
	
	public synchronized List<Player> playersWhoJoined(){
		System.out.println("Getting players who joined the tournament");
		System.out.println("Players: " + joinPlayers);
		question = false;
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