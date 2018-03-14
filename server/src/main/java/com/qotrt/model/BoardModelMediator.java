package com.qotrt.model;

public class BoardModelMediator {

	private TournamentModel tm;

	public BoardModelMediator(TournamentModel tm) {
		this.tm = tm;
	}
	
	public TournamentModel getTournamentModel() {
		return this.tm;
	}

}
