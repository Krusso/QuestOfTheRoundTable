package src.player;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import src.game_logic.AdventureCard;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.QuestCard;
import src.game_logic.AdventureCard.TYPE;
import src.socket.OutputController;
import src.views.PlayerView;
import src.views.PlayersView;

public class TestBidCalculator {

	
	PlayerManager pm;
	Player p1;
	ArrayList<AdventureCard> cards;
	@Before
	public void before() {
		pm = new PlayerManager(1, null, false);
		p1 = new Player(0);
		pm.players[0] = p1;
		cards = new ArrayList<AdventureCard>();
	}
	
	@Test
	public void testKingPellinore() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Pellinore",10,10,0,4, TYPE.ALLIES));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Sir Pellinore")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}) );
		assertEquals(6,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(6, p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testAmour() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Amour")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(3,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(3, p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testKingArthur() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("King Arthur",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("King Arthur")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(4,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(4, p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testQueenGuinevere() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Queen Guinevere")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(5,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(5, p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testQueenIseult() {
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Queen Iseult")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		pm.nextTurn();
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(3,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(3, p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(1, cardsToBid);
	}
	
	@Test
	public void testQueenIseultAndTristan() {
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().filter(i -> i.getName().equals("Queen Iseult") || i.getName().equals("Sir Tristan")).map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		pm.nextTurn();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(5,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(5, p1, new QuestCard("Search for the Questing Beast",4,new String[] {"None"}));
		assertEquals(1, cardsToBid);
	}
}
