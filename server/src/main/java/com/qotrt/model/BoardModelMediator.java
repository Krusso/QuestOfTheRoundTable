package com.qotrt.model;

public class BoardModelMediator {

	private TournamentModel tm;
	private QuestModel qm;
	private BoardModel bm;
	private DiscardModel dm;
	private EventModel em;

	public BoardModelMediator(TournamentModel tm, QuestModel qm, BoardModel bm, DiscardModel dm, EventModel em) {
		this.tm = tm;
		this.qm = qm;
		this.bm = bm;
		this.dm = dm;
		this.em = em;
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

	public DiscardModel getDiscardModel() {
		return this.dm;
	}

	public EventModel getEventModel() {
		return em;
	}

}
