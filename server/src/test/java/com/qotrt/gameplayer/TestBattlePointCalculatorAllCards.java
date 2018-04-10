package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.StoryCard;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.deck.StoryDeck;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBattlePointCalculatorAllCards {
	
	PlayerManager pm;
	Player p1;
	ArrayList<AdventureCard> cards;
	ArrayList<StoryCard> storyCards;
	AdventureDeck ad;
	StoryDeck sd;
	@Before
	public void before() {
		pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		p1 = new Player(0, new UIPlayer("", "",1));
		pm.players[0] = p1;
		cards = new ArrayList<AdventureCard>();
		storyCards = new ArrayList<StoryCard>();
		pm.nextTurn();
		ad = new AdventureDeck();
		sd = new StoryDeck();
		ad.populate();
		sd.populate();
		storyCards.add(sd.getCardByName("Search for the Holy Grail"));
		storyCards.add(sd.getCardByName("Test of the Green Knight"));
		storyCards.add(sd.getCardByName("Search for the Questing Beast"));
		storyCards.add(sd.getCardByName("Defend the Queen's Honor"));
		storyCards.add(sd.getCardByName("Rescue the Fair Maiden"));
		storyCards.add(sd.getCardByName("Journey Through the Enchanted Forest"));
		storyCards.add(sd.getCardByName("Vanquish King Arthur's Enemies"));
		storyCards.add(sd.getCardByName("Slay the Dragon"));
		storyCards.add(sd.getCardByName("Boar Hunt"));
		storyCards.add(sd.getCardByName("Repel the Saxon Raiders"));

		storyCards.add(sd.getCardByName("Tournament at Camelot"));
		storyCards.add(sd.getCardByName("Tournament at Orkney"));
		storyCards.add(sd.getCardByName("Tournament at Tintagel"));
		storyCards.add(sd.getCardByName("Tournament at York"));
	}
	
	@Test
	public void testCards() {
		
		testPoints(new int [] {35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35}, "Excalibur");
		testPoints(new int [] {25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25}, "Lance");
		testPoints(new int [] {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20}, "Battle-ax");
		testPoints(new int [] {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "Sword");
		testPoints(new int [] {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "Horse");
		testPoints(new int [] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, "Dagger");
		
		testPoints(new int [] {75, 55, 55, 75, 55, 55, 55, 75, 55, 55, 55, 55, 55, 55}, "Dragon");
		testPoints(new int [] {45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45}, "Giant");
		testPoints(new int [] {35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35}, "Mordred");
		testPoints(new int [] {45, 45, 30, 45, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30}, "Green Knight");
		testPoints(new int [] {40, 30, 30, 40, 40, 30, 30, 30, 30, 30, 30, 30, 30, 30}, "Black Knight");
		testPoints(new int [] {35, 25, 25, 35, 25, 35, 25, 25, 25, 25, 25, 25, 25, 25}, "Evil Knight");
		
		testPoints(new int [] {30, 20, 20, 30, 20, 20, 20, 20, 20, 30, 20, 20, 20, 20}, "Saxon Knight");
		testPoints(new int [] {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20}, "Robber Knight");
		testPoints(new int [] {25, 15, 15, 25, 15, 15, 15, 15, 15, 25, 15, 15, 15, 15}, "Saxons");
		testPoints(new int [] {20, 10, 10, 20, 10, 10, 10, 10, 20, 10, 10, 10, 10, 10}, "Boar");
		testPoints(new int [] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, "Thieves");
		
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Test of Valor");
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Test of Temptation");
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Test of Morgan Le Fey");
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Test of the Questing Beast");
		
		testPoints(new int [] {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20}, "Sir Galahad");
		testPoints(new int [] {20, 20, 20, 30, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20}, "Sir Lancelot");
		testPoints(new int [] {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "King Arthur");
		testPoints(new int [] {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "Sir Tristan");
		testPoints(new int [] {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "King Pellinore");
		testPoints(new int [] {15, 25, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "Sir Gawain");
		testPoints(new int [] {25, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, "Sir Percival");
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Queen Guinevere");
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Queen Iseult");
		testPoints(new int [] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, "Merlin");
		
		
		testPoints(new int [] {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, "Amour");
	}
	
	private void testPoints(int[] bpExpected, String cardName) {
		p1.getFaceUp().getDeck().clear();
		cards = new ArrayList<AdventureCard>();
		cards.add(ad.getCardByName(cardName));
		p1.addCards(cards);
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> points = new ArrayList<Integer>();
		storyCards.forEach(i -> {
			points.add(new BattlePointCalculator(pm).calculatePoints(participants, i).get(0).intValue());
		});
		Assert.assertEquals(Arrays.toString(points.toArray()), Arrays.toString(bpExpected));
	}
}
