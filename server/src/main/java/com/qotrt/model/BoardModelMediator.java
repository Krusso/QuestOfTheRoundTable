package com.qotrt.model;

public class BoardModelMediator {

	private TournamentModel tm;
	private QuestModel qm;

	public BoardModelMediator(TournamentModel tm, QuestModel qm) {
		this.tm = tm;
		this.qm = qm;
	}
	
	public TournamentModel getTournamentModel() {
		return this.tm;
	}

	public QuestModel getQuestModel() {
		return this.qm;
	}

}
