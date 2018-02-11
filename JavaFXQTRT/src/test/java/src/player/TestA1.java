package src.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.FoeCard;
import src.game_logic.QuestCard;
import src.game_logic.Rank;
import src.game_logic.WeaponCard;

public class TestA1 {

	@Test
	public void doIParticipateInTournament() {
		AbstractAI player = new A1(new UIPlayer(0), new UIPlayerManager(0));
		//what a great test good job me
		assertEquals(false, player.doIParticipateInTournament());

		UIPlayerManager pm = new UIPlayerManager(1);
		pm.setPlayerRank(0, Rank.RANKS.CHAMPION);
		pm.setCurrentPlayer(0);
		player = new A1(new UIPlayer(0), pm);
		assertEquals(true, player.doIParticipateInTournament());
	}

	@Test
	public void playCardsForTournament() {
		UIPlayer p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, null);
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {}));
		
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {"Excalibur"}));
		
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Excalibur"}));

		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Lance"}));
	}
	
	@Test
	public void doISponserAQuest() {
		// TODO
	}
	
	@Test
	public void doIParticipateInQuest() {
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, null);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,"All Saxons")));
	
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,"All Saxons")));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));		
		p1.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.addCard(new FoeCard("Green Knight",25,40, TYPE.FOES));
		player = new A1(p1, null);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,"All Saxons")));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, null);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,"All Saxons")));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));		
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, null);
		assertEquals(true, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,"All Saxons")));		
	}

	
	@Test
	public void playCardsForFoeQuest() {
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, null);
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(false), 
				new String[] {"Excalibur", "Lance"}));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(false), 
				new String[] {"Sword", "Excalibur"}));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(false), 
				new String[] {"King Arthur", "Sword"}));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.addCard(new AmourCard("Amour",10,1, TYPE.AMOUR));
		player = new A1(p1, null);
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(false), 
				new String[] {"Amour", "King Arthur"}));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.addCard(new AmourCard("Amour",10,1, TYPE.AMOUR));
		player = new A1(p1, null);
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(true), 
				new String[] {"Amour", "Excalibur", "Sword", "King Arthur"}));
	}
	
	@Test
	public void nextBid() {
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, null);
		assertTrue(-1 == player.nextBid(1, Integer.MAX_VALUE));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, null);
		assertTrue(-1 == player.nextBid(1, 0));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A1(p1, null);
		assertTrue(-1 == player.nextBid(1, 0));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, null);
		assertTrue(2 == player.nextBid(1, 0));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A1(p1, null);
		assertTrue(-1 == player.nextBid(2, 0));
		
		System.out.println("Testing 6");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A1(p1, null);
		assertTrue(-1 == player.nextBid(2, 0));
		
		System.out.println("Testing 7");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, null);
		assertTrue(-1 == player.nextBid(2, 0));
	}
	
	@Test
	public void discardAfterWinningTest() {
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {"Thieves", "Thieves"}));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 6");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 7");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, null);
		assertTrue(TestA2.compare(player.discardAfterWinningTest(1), new String[] {"Thieves", "Thieves"}));
	}
}
