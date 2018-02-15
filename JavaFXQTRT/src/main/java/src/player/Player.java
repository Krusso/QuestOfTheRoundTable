package src.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Card;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.views.PlayerView;

public class Player {

	public static enum STATE {
		NEUTRAL, QUESTIONED, YES, NO, PICKING, DISCARDING, WIN, WINNING, GAMEWON,
		SPONSORING, QUESTQUESTIONED // >:(
, BIDDING, TESTDISCARD, QUESTPICKING, QUESTJOINQUESTIONED
	};
	
	protected RANKS rank;
	public AdventureDeck hand;
	protected AdventureDeck faceDown;
	private AdventureDeck faceUp;
	private List<List<Card>> questDown;
	private List<List<Card>> questUp;
	private PlayerView pv;
	private final int ID;
	private STATE question;
	protected int shields;
	public boolean tristan = false;
	public boolean iseult = false;
	
	public Player(int id) {
		rank = Rank.RANKS.SQUIRE;
		hand = new AdventureDeck();
		faceDown = new AdventureDeck();
		faceUp = new AdventureDeck();
		ID = id;
		shields = 0;
		questUp = new ArrayList<List<Card>>();
	}
	
	public int getID() {
		return ID;
	}
	
	protected void increaseLevel() {
		if(rank == null) {
			rank = Rank.RANKS.SQUIRE;
		} else if(rank == Rank.RANKS.SQUIRE && shields >= 5) {
			rank = Rank.RANKS.KNIGHT;
			changeShields(-5);
		} else if (rank == Rank.RANKS.KNIGHT && shields >= 7) {
			rank = Rank.RANKS.CHAMPION;
			changeShields(-7);
		} else if (rank == Rank.RANKS.CHAMPION && shields >= 10) {
			rank = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
			changeShields(-10);
		} else {
			return;
		}
		if(pv != null) pv.update(rank, ID);
	}
	
	public void addCards(ArrayList<AdventureCard> cards) {
		for(AdventureCard card: cards) {
			hand.addCard(card, 1);
		}
		if(pv != null) pv.updateCards(cards, ID);
	}

	public String hand() {
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
		if(pv != null) pv.updateState(question, ID);
	}
	
	protected void setState(STATE question, int numStages) {
		this.question = question;
		if(pv != null) pv.updateState(question, ID, numStages);
	}
	
	protected void setState(STATE question, int i, TYPE type) {
		this.question = question;
		if(pv != null) pv.updateState(question, ID,i, type);
	}

	protected void changeShields(int shields) {
		this.shields += shields;
		if(shields < 0) {
			shields = 0;
		}
		if(pv != null ) pv.updateShieldCount(ID, shields);
	}
	
	public int faceDownDeckLength() {
		return faceDown.size();
	}
	
	public AdventureDeck getFaceDownDeck() {
		return this.faceDown;
	}


	public void setFaceDown(String[] cards) {
		List<AdventureCard> list = new ArrayList<AdventureCard>();
		for(String card: cards) {
			list.add(hand.getCardByName(card));
		}
		faceDown.addCards(list);
		if(pv != null) pv.updateFaceDown(list, ID);
	}
	
	public void setQuestDown(List<List<Card>> quest) {
		questDown = quest;
		if(pv != null) pv.updateQuestDown(questDown, ID);
	}
	
	public void flipStage(int stage) {
		questUp.add(questDown.get(stage));
		if(pv != null) pv.updateQuestUp(questDown.get(stage), stage, ID);
	}

	public RANKS getRank() {
		return this.rank;
	}

	public void flipCards() {
		faceUp.addCards(faceDown.drawTopCards(faceDown.size()));
		faceUp.getDeck().forEach(i -> {
			if(i.getName().equals("Sir Tristan")) {
				tristan = true;
			} else if(i.getName().equals("Queen Iseult")) {
				iseult = true;
			}
		});
		if(pv != null) pv.updateFaceUp(faceUp, ID);
	}

	public final AdventureDeck getFaceUp() {
		return this.faceUp;
	}

	public void discardType(TYPE type) {
		List<Card> removedCards = faceUp.discardType(type);
		removedCards.forEach(i -> {
			if(i.getName().equals("Sir Tristan")) {
				tristan = false;
			} else if(i.getName().equals("Queen Iseult")) {
				iseult = false;
			}
		});
		if(pv != null) pv.discard(ID, removedCards);
	}
	
	public int getShields() {
		return this.shields;
	}

	public int getTypeCount(TYPE type) {
		return hand.typeCount(type);
	}
	
	protected void removeCards(String[] split) {
		for(String cardName: split) {
			hand.getCardByName(cardName);
		}
	}
	
	public Card getCard(String cardName) {
		return hand.getCardByName(cardName);
	}

	public int getCardCount() {
		return hand.size();
	}
	
	public List<AdventureCard> listOfTypeDecreasingBp(TYPE type){
		// p1 and p2 being flipped is not a typo :) 
		return hand.getDeck().stream().
		sorted((p2,p1) -> Integer.compare(p1.getBattlePoints(), p2.getBattlePoints())).
		filter(i -> i.getType() == type).
		collect(Collectors.toList());
	}
	
	public List<AdventureCard> uniqueListOfTypeDecreasingBp(TYPE type){
		return listOfTypeDecreasingBp(type).stream().map(i -> i.getName()).distinct().map(i -> hand.findCardByName(i))
				.collect(Collectors.toList());
	}

	public void setBidAmount(STATE bidding, int maxBidValue, int i) {
		this.question = bidding;
		if(pv != null) this.pv.setBidAmount(bidding, this.ID, maxBidValue, i);
	}

	public void setDiscardAmount(STATE testdiscard, int cardsToBid) {
		this.question = testdiscard;
		if(pv != null) this.pv.setDiscardAmount(testdiscard, this.ID, cardsToBid);
	}
}
