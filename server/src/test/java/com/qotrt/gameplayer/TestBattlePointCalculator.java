package com.qotrt.gameplayer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBattlePointCalculator {
	
	PlayerManager pm;
	Player p1;
	ArrayList<AdventureCard> cards;
	@Before
	public void before() {
		pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		p1 = new Player(0, new UIPlayer("", "",1));
		pm.players[0] = p1;
		cards = new ArrayList<AdventureCard>();
		pm.nextTurn();
	}
	
	@Test
	public void testLancelot() {
		cards.add(new AllyCard("Sir Lancelot",15,25, TYPE.ALLIES));
		p1.addCards(cards);
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants, new QuestCard("Test of the Green Knight",4,new String[] {"Green Knight", "Sir Gawain"}));
		assertEquals(20,scores.get(0).intValue());
		scores = new BattlePointCalculator(pm).calculatePoints(participants, new QuestCard("Defend the Queen's Honor",4,new String[] {"All", "Sir Lancelot"}));
		assertEquals(30,scores.get(0).intValue());
	}
	
	@Test
	public void testGawain() {
		cards.add(new AllyCard("Sir Gawain",10,20, TYPE.ALLIES));
		p1.addCards(cards);
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants, new QuestCard("Test of the Green Knight",4,new String[] {"Green Knight", "Sir Gawain"}));
		assertEquals(25,scores.get(0).intValue());
		scores = new BattlePointCalculator(pm).calculatePoints(participants, new QuestCard("Defend the Queen's Honor",4,new String[] {"All", "Sir Lancelot"}));
		assertEquals(15,scores.get(0).intValue());
	}
	
	@Test
	public void testPercival() {
		cards.add(new AllyCard("Sir Percival",5,20, TYPE.ALLIES));
		p1.addCards(cards);
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants, new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"}));
		assertEquals(25,scores.get(0).intValue());
	}
	
	@Test
	public void testTristanIseult() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		p1.addCards(cards);
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants, null);
		assertEquals(25,scores.get(0).intValue());
	}
	
	@Test
	public void testAllies() {
		cards.add(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("King Pellinore",10,10,0,4, TYPE.ALLIES));
		p1.addCards(cards);
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants, null);
		assertEquals(40,scores.get(0).intValue());
	}
	
	@Test
	public void testWeaponCalculations() {
		pm = new PlayerManager(5, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", "",1));
		Player p2 = new Player(1, new UIPlayer("", "",1));
		Player p3 = new Player(2, new UIPlayer("", "",1));
		Player p4 = new Player(3, new UIPlayer("", "",1));
		Player p5 = new Player(4, new UIPlayer("", "",1));
		pm.players[0] = p1;
		pm.players[1] = p2;
		pm.players[2] = p3;
		pm.players[3] = p4;
		pm.players[4] = p5;
		pm.nextTurn();
		
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
		cards.add(new WeaponCard("Battle-ax", 15, TYPE.WEAPONS));
		cards.add(new WeaponCard("Sword",10, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCards(cards);
		ArrayList<AdventureCard> cards2 = new ArrayList<AdventureCard>();
		cards2.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		cards2.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p2.addCards(cards2);
		ArrayList<AdventureCard> cards3 = new ArrayList<AdventureCard>();
		cards3.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		cards3.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
		cards3.add(new WeaponCard("Battle-ax", 15, TYPE.WEAPONS));
		p3.addCards(cards3);
		ArrayList<AdventureCard> cards4 = new ArrayList<AdventureCard>();
		cards4.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards4.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p4.addCards(cards4);
		ArrayList<AdventureCard> cards5 = new ArrayList<AdventureCard>();
		p5.addCards(cards5);
		
		
		cards.stream().forEach(i -> p1.setFaceDown(p1.getCardByID(i.id)));
		p1.flipCards();
		cards2.stream().forEach(i -> p2.setFaceDown(p2.getCardByID(i.id)));
		p2.flipCards();
		cards3.stream().forEach(i -> p3.setFaceDown(p3.getCardByID(i.id)));
		p3.flipCards();
		cards4.stream().forEach(i -> p4.setFaceDown(p4.getCardByID(i.id)));
		p4.flipCards();
		cards5.stream().forEach(i -> p5.setFaceDown(p5.getCardByID(i.id)));
		p5.flipCards();
		
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		participants.add(p2);
		participants.add(p3);
		participants.add(p4);
		participants.add(p5);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants, null);
		assertEquals(95,scores.get(0).intValue());
		assertEquals(55,scores.get(1).intValue());
		assertEquals(70,scores.get(2).intValue());
		assertEquals(20,scores.get(3).intValue());
		assertEquals(5,scores.get(4).intValue());
	}

}
