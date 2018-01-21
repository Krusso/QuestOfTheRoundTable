package src.player;

import java.util.ArrayList;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureDeck;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.views.PlayerView;

public class Player {

	
	public static enum STATE {
			QUESTIONED, YES, NO, PICKING
	};
	
	private RANKS rank;
	private AdventureDeck advDeck;
	private PlayerView pv;
	private final int ID;
	private STATE question;
	private int shields;
	
	public Player(int id) {
		rank = Rank.RANKS.SQUIRE;
		advDeck = new AdventureDeck();
		ID = id;
		shields = 0;
	}
	
	protected int getID() {
		return ID;
	}
	
	protected void increaseLevel() {
		if(rank == null) {
			rank = Rank.RANKS.SQUIRE;
		} else if(rank == Rank.RANKS.SQUIRE) {
			rank = Rank.RANKS.KNIGHT;
		} else if (rank == Rank.RANKS.KNIGHT) {
			rank = Rank.RANKS.CHAMPION;
		}
		pv.update(rank, ID);
	}
	
	protected void addCards(ArrayList<AdventureCard> cards) {
		for(AdventureCard card: cards) {
			advDeck.addCard(card, 1);
		}
		pv.updateCards(cards, ID);
	}

	protected String hand() {
		return advDeck.toString();
	}

	protected void subscribe(PlayerView pv) {
		this.pv = pv;
	}

	protected STATE getQuestion() {
		return question;
	}

	protected void setState(STATE question) {
		this.question = question;
		pv.updateState(question, ID);
	}

	protected void addShields(int shields) {
		this.shields += shields;
	}

}
