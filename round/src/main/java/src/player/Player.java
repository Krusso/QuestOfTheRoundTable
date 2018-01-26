package src.player;

import java.util.ArrayList;
import java.util.List;

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
	private AdventureDeck hand;
	private AdventureDeck faceDown;
	private AdventureDeck faceUp;
	private PlayerView pv;
	private final int ID;
	private STATE question;
	private int shields;
	
	public Player(int id) {
		rank = Rank.RANKS.SQUIRE;
		hand = new AdventureDeck();
		faceDown = new AdventureDeck();
		faceUp = new AdventureDeck();
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
			hand.addCard(card, 1);
		}
		pv.updateCards(cards, ID);
	}

	protected String hand() {
		return hand.toString();
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

	protected void setFaceDown(String[] cards) {
		List<AdventureCard> list = new ArrayList<AdventureCard>();
		for(String card: cards) {
			list.add(hand.getCardByName(card));
		}
		faceDown.addCards(list);
		pv.updateFaceDown(list, ID);
	}

	protected RANKS getRank() {
		return this.rank;
	}

	protected void flipCards() {
		faceUp.addCards(faceDown.drawCards(faceDown.size()));
		pv.updateFaceUp(faceUp, ID);
	}

	protected final AdventureDeck getFaceUp() {
		return this.faceUp;
	}

	public void discardWeapons() {
		faceUp.discardWeapons();
	}
	
	public void discardAmours() {
		faceUp.discardAmours();
	}

}
