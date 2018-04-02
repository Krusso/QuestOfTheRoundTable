package com.qotrt.gameplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.AmourCard;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.cards.TestCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.gameplayer.Rank.RANKS;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.TournamentModel;
import com.qotrt.model.UIPlayer;

public class TestA1 {
	final static Logger logger = LogManager.getLogger(TestA1.class);
	@Test
	public void doIParticipateInTournament() {
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {new UIPlayer("","",1)}, null, RIGGED.ONE);
		AbstractAI player = new A1(new Player(0, new UIPlayer("","",1)), pm, new BoardModelMediator(null, null, null, null, null, null));
		pm.nextTurn();
		//what a great test good job me
		assertEquals(false, player.doIParticipateInTournament());

		Player p1 = new Player(0, new UIPlayer("","",1));
		
		pm.players[0] = p1;
		p1.rank = RANKS.CHAMPION;
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(false, player.doIParticipateInTournament());
		
		pm.players[0].shields = 10;
		assertEquals(true, player.doIParticipateInTournament());
	}

	@Test
	public void playCardsForTournament() {
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("","",1));
		
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm, new BoardModelMediator(new TournamentModel(false), null, null, null, null, null));
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {}));
		
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(new TournamentModel(false), null, null, null, null, null));
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {"Excalibur"}));
		
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(new TournamentModel(false), null, null, null, null, null));
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {"Excalibur"}));

		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(new TournamentModel(false), null, null, null, null, null));
		assertEquals(true, TestA2.compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Lance"}));
	}
	
	@Test
	public void doISponserAQuest() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("","",1));

		pm.nextTurn();
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(null, player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("","",1));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Green Knight",25,40, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(null,player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("","",1));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Saxon Knight",15,25, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(true, TestA2.compareList(player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Thieves"}, {"Dragon"}}));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("","",1));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		p1.hand.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(true, TestA2.compareList(player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Thieves"}, {"Dragon"}}));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("","",1));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		p1.hand.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new TestCard("Test of Valor", TYPE.TESTS));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(true, TestA2.compareList(player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Test of Valor"}, {"Dragon"}}));
	}
	
	@Test
	public void doIParticipateInQuest() {
		logger.info("Testing 1");
		Player p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, null, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
	
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, null, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));		
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Green Knight",25,40, TYPE.FOES));
		player = new A1(p1, null, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, null, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));		
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, null, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(true, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));		
	}

	
	@Test
	public void playCardsForFoeQuest() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("","",1));
		
		pm.players[0] = p1;
		
		QuestCard card = new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"});
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(card), 
				new String[] {"Excalibur", "Lance"}));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(card), 
				new String[] {"Sword", "Excalibur"}));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.hand.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(card), 
				new String[] {"King Arthur", "Sword"}));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.hand.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.hand.addCard(new AmourCard("Amour",10,1, TYPE.AMOUR));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(card), 
				new String[] {"Amour", "King Arthur"}));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.hand.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.hand.addCard(new AmourCard("Amour",10,1, TYPE.AMOUR));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.playCardsForFoeQuest(card);
		assertTrue(TestA2.compare(player.playCardsForFoeQuest(card), 
				new String[] {"Amour", "Excalibur", "Sword", "King Arthur"}));
	}
	
	@Test
	public void nextBid() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("","",1));
		
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Black Knight",25,35, TYPE.FOES));
		AbstractAI player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(-1 == player.nextBid(Integer.MAX_VALUE));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertEquals(-1,player.nextBid(1));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(-1 == player.nextBid(1));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(2 == player.nextBid(0));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(-1 == player.nextBid(1));
		
		logger.info("Testing 6");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		assertTrue(-1 == player.nextBid(1));
		
		logger.info("Testing 7");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(-1 == player.nextBid(0));
	}
	
	@Test
	public void discardAfterWinningTest() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("","",1));
		
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {"Thieves", "Thieves"}));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 6");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 7");
		p1 = new Player(0, new UIPlayer("","",1));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A1(p1, pm, new BoardModelMediator(null, null, null, null, null, null));
		player.rounds = 1;
		assertTrue(TestA2.compare(player.discardAfterWinningTest(), new String[] {"Thieves", "Thieves"}));
	}
}
