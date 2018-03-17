package com.qotrt.model;

public class BoardModelMediator {

	private TournamentModel tm;
	private QuestModel qm;
	private BoardModel bm;

	public BoardModelMediator(TournamentModel tm, QuestModel qm, BoardModel bm) {
		this.tm = tm;
		this.qm = qm;
		this.bm = bm;
	}
	
	public TournamentModel getTournamentModel() {
		return this.tm;
	}

	public QuestModel getQuestModel() {
		return this.qm;
	}

	public BoardModel getBoardModel() {
		return this.bm;
	}

}
