package src.player;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import src.game_logic.AdventureCard;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.QuestCard;
import src.game_logic.AdventureCard.TYPE;

public class TestBidCalculator {

	@Test
	public void testKingPellinore() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Pellinore",10,10,0,4, TYPE.ALLIES));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Sir Pellinore")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator().maxBid(p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(6,scores);
		int cardsToBid = new BidCalculator().cardsToBid(6, p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testAmour() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Amour")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator().maxBid(p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(3,scores);
		int cardsToBid = new BidCalculator().cardsToBid(3, p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testKingArthur() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("King Arthur",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("King Arthur")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator().maxBid(p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(4,scores);
		int cardsToBid = new BidCalculator().cardsToBid(4, p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testQueenGuinevere() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Queen Guinevere")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator().maxBid(p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(5,scores);
		int cardsToBid = new BidCalculator().cardsToBid(5, p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testQueenIseult() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Queen Iseult")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator().maxBid(p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(3,scores);
		int cardsToBid = new BidCalculator().cardsToBid(3, p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(1, cardsToBid);
	}
	
	@Test
	public void testQueenIseultAndTristan() {
		Player p1 = new Player(0);
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Queen Iseult") || i.getName().equals("Sir Tristan")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator().maxBid(p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(5,scores);
		int cardsToBid = new BidCalculator().cardsToBid(5, p1, new QuestCard("Search for the Questing Beast",4,"None"));
		assertEquals(1, cardsToBid);
	}
}
