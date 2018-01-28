package src.player;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.views.PlayerView;

public class Player {

	public static enum STATE {
			QUESTIONED, YES, NO, PICKING, DISCARDING, WIN
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

	public STATE getQuestion() {
		return question;
	}
	
	protected void setState(STATE question) {
		this.question = question;
		pv.updateState(question, ID);
	}
	
	protected void setState(STATE question, int i, TYPE type) {
		this.question = question;
		pv.updateState(question, ID,i, type);
	}

	protected void changeShields(int shields) {
		this.shields += shields;
		if(shields < 0) {
			shields = 0;
		}
	}

	protected void setFaceDown(String[] cards) {
		List<AdventureCard> list = new ArrayList<AdventureCard>();
		for(String card: cards) {
			list.add(hand.getCardByName(card));
		}
		faceDown.addCards(list);
		pv.updateFaceDown(list, ID);
	}

	public RANKS getRank() {
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
		faceUp.discardType(TYPE.WEAPONS);
	}
	
	public void discardAmours() {
		faceUp.discardType(TYPE.AMOUR);
	}
	public int getShields() {
		return this.shields;
	}
	public int getTotalShields() {
		int toReturn = this.getShields();
		if(rank == Rank.RANKS.KNIGHT) {
			toReturn += 5;
		} else if (rank == Rank.RANKS.CHAMPION) {
			toReturn += 7;
		}
		return toReturn;
	}

	protected void discardAllies() {
		faceUp.discardType(TYPE.ALLIES);
	}

	public int weaponCount() {
		return hand.typeCount(TYPE.WEAPONS);
	}

	public int foeCount() {
		return hand.typeCount(TYPE.FOES);
	}
	
	protected void removeCards(String[] split) {
		for(String cardName: split) {
			hand.getCardByName(cardName);
		}
	}

	public int getCardCount() {
		return hand.size();
	}
}
