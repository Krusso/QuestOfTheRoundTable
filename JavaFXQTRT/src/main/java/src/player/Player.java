package src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Card;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.views.PlayerView;

public class Player {

	final static Logger logger = LogManager.getLogger(Player.class);
	
	public static enum STATE {
		NEUTRAL, QUESTIONED, YES, NO, PICKING, EVENTDISCARDING, WIN, WINNING, GAMEWON,
		SPONSORING, QUESTQUESTIONED
, BIDDING, TESTDISCARD, QUESTPICKING, QUESTJOINQUESTIONED, DISCARDING, QUESTQUESTIONEDCANT
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
	
	public void increaseLevel() {
		if(rank == Rank.RANKS.SQUIRE && shields >= 5) {
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
		
		logger.info("Player id: " + ID + " current rank: " + rank + " current shields: " + shields);
		if(pv != null) pv.update(rank, ID);
		increaseLevel();
	}
	
	public void addCards(ArrayList<AdventureCard> cards) {
		for(AdventureCard card: cards) {
			logger.info("Player id: " + ID + " adding card " + card.getName());
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
		logger.info("Player id: " + ID + " setting state " + question);
		if(pv != null) pv.updateState(question, ID);
	}
	
	protected void setState(STATE question, int numStages) {
		this.question = question;
		logger.info("Player id: " + ID + " setting state " + question + " stages: " + numStages);
		if(pv != null) pv.updateState(question, ID, numStages);
	}
	
	protected void setState(STATE question, int i, TYPE type) {
		this.question = question;
		logger.info("Player id: " + ID + " setting state " + question + " discard: " + i + " type: " + type);
		if(pv != null) pv.updateState(question, ID,i, type);
	}

	public void changeShields(int shields) {
		this.shields += shields;
		if(this.shields < 0) {
			this.shields = 0;
		}
		
		logger.info("Changing shields for player: " + ID + " to: " + this.shields);
		if(pv != null ) pv.updateShieldCount(ID, this.shields);
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
		logger.info("Player id: " + ID + " setting face down: " + cards.toString());
		faceDown.addCards(list);
		if(pv != null) pv.updateFaceDown(list, ID);
	}
	
	public void setQuestDown(List<List<Card>> quest) {
		questDown = quest;
		if(pv != null) pv.updateQuestDown(questDown, ID);
	}
	
	public void flipStage(int stage) {
		questUp.add(questDown.get(stage));
		logger.info("Flipping stage: " + stage);
		if(pv != null) pv.updateQuestUp(questDown.get(stage), ID, stage);
	}

	public RANKS getRank() {
		return this.rank;
	}

	public void flipCards() {
		logger.info("Face down cards: " + faceDown.getDeck());
		faceUp.addCards(faceDown.drawTopCards(faceDown.size()));
		faceUp.getDeck().forEach(i -> {
			if(i.getName().equals("Sir Tristan")) {
				tristan = true;
			} else if(i.getName().equals("Queen Iseult")) {
				iseult = true;
			}
		});
		logger.info("Face up deck: " + faceUp.getDeck());
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
		logger.info("Player id: " + ID + " discarded type: " + type);
		logger.info("Player id: " + ID + " discarded cards: " + Arrays.toString(removedCards.stream().map(i -> i.getName()).toArray(String[]::new)));
		if(pv != null) pv.discard(ID, removedCards);
	}
	
	public int getShields() {
		return this.shields;
	}

	public int getTypeCount(TYPE type) {
		return hand.typeCount(type);
	}
	
	protected ArrayList<AdventureCard> removeCards(String[] split) {
		ArrayList<AdventureCard> removed = new ArrayList<AdventureCard>();
		for(String cardName: split) {
			removed.add(hand.getCardByName(cardName));
		}
		logger.info("Player id: " + ID + " removing cards " + Arrays.toString(split));
		return removed;
	}
	
	public Card getCard(String cardName) {
		return hand.getCardByName(cardName);
	}

	public int getCardCount() {
		return hand.size();
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
