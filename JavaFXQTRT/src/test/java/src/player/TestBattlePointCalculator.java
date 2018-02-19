package src.player;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AllyCard;
import src.game_logic.WeaponCard;

public class TestBattlePointCalculator {
	
	PlayerManager pm;
	Player p1;
	ArrayList<AdventureCard> cards;
	@Before
	public void before() {
		pm = new PlayerManager(1, null, true);
		p1 = new Player(0);
		pm.players[0] = p1;
		cards = new ArrayList<AdventureCard>();
		pm.nextTurn();
	}
	
	@Test
	public void testTristanIseult() {
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants);
		assertEquals(25,scores.get(0).intValue());
	}
	
	@Test
	public void testAllies() {
		cards.add(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Pellinore",10,10,0,4, TYPE.ALLIES));
		p1.addCards(cards);
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants);
		assertEquals(40,scores.get(0).intValue());
	}
	
	@Test
	public void testWeaponCalculations() {
		pm = new PlayerManager(5, null, true);
		Player p1 = new Player(0);
		Player p2 = new Player(1);
		Player p3 = new Player(2);
		Player p4 = new Player(3);
		Player p5 = new Player(4);
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
		p2.addCards(cards);
		p3.addCards(cards);
		p4.addCards(cards);
		p5.addCards(cards);
		
		
		p1.setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		p1.flipCards();
		p2.setFaceDown(new String[] {"Excalibur", "Lance"});
		p2.flipCards();
		p3.setFaceDown(new String[] {"Excalibur", "Lance", "Battle-ax"});
		p3.flipCards();
		p4.setFaceDown(new String[] {"Dagger", "Horse"});
		p4.flipCards();
		p5.setFaceDown(new String[] {});
		p5.flipCards();
		
		ArrayList<Player> participants = new ArrayList<Player>();
		participants.add(p1);
		participants.add(p2);
		participants.add(p3);
		participants.add(p4);
		participants.add(p5);
		ArrayList<Integer> scores = new BattlePointCalculator(pm).calculatePoints(participants);
		assertEquals(95,scores.get(0).intValue());
		assertEquals(55,scores.get(1).intValue());
		assertEquals(70,scores.get(2).intValue());
		assertEquals(20,scores.get(3).intValue());
		assertEquals(5,scores.get(4).intValue());
	}

}
