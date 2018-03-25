package com.qotrt.gameplayer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.qotrt.calculator.BidCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.AmourCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.model.UIPlayer;
import com.qotrt.model.RiggedModel.RIGGED;

public class TestBidCalculator {

	
	PlayerManager pm;
	Player p1;
	ArrayList<AdventureCard> cards;
	@Before
	public void before() {
		pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		p1 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		cards = new ArrayList<AdventureCard>();
	}
	
	@Test
	public void testKingPellinore() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("King Pellinore",10,10,0,4, TYPE.ALLIES));
		p1.addCards(cards);
		cards.stream().filter(i -> i.getName().equals("King Pellinore")).forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {}) );
		assertEquals(6,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(6, p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testAmour() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		cards.stream().filter(i -> i.getName().equals("Amour")).forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Defend the Queen's Honor",4,new String[] {"All", "Sir Lancelot"}));
		assertEquals(3,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(3, p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testKingArthur() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("King Arthur",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		cards.stream().filter(i -> i.getName().equals("King Arthur")).forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Journey Through the Enchanted Forest",3,new String[] {"Evil Knight"}));
		assertEquals(4,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(4, p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testQueenGuinevere() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		cards.stream().filter(i -> i.getName().equals("Queen Guinevere")).forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(5,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(5, p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(2, cardsToBid);
	}
	
	@Test
	public void testQueenIseult() {
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		cards.stream().filter(i -> i.getName().equals("Queen Iseult")).forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		pm.nextTurn();
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Vanquish King Arthur's Enemies",3,new String[] {}));
		assertEquals(3,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(3, p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(1, cardsToBid);
	}
	
	@Test
	public void testQueenIseultAndTristan() {
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Guinevere",10,1, TYPE.AMOUR));
		p1.addCards(cards);
		cards.stream().filter(i -> i.getName().equals("Queen Iseult") || i.getName().equals("Sir Tristan")).forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		pm.nextTurn();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		int scores = new BidCalculator(pm).maxBid(p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(5,scores);
		int cardsToBid = new BidCalculator(pm).cardsToBid(5, p1, new QuestCard("Search for the Questing Beast",4,new String[] {}));
		assertEquals(1, cardsToBid);
	}
}
