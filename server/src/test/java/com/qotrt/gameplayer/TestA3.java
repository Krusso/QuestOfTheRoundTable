package com.qotrt.gameplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.Card;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.cards.TestCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.QuestModel;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.TournamentModel;
import com.qotrt.model.UIPlayer;
import com.qotrt.sequence.Quest;

public class TestA3 {
	final static Logger logger = LogManager.getLogger(TestA3.class);
	@Test
	public void doIParticipateInTournament() {
		PlayerManager pm = new PlayerManager(2, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		Player p2 = new Player(0, new UIPlayer("", ""));
		AbstractAI player = new A3(new Player(0, new UIPlayer("", "")), pm, new BoardModelMediator(new TournamentModel(), null, null));
		pm.players[0] = p1;
		pm.players[1] = p2;
		//what a great test good job me
		assertEquals(true, player.doIParticipateInTournament());
		
		pm.nextTurn();
		p2.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		assertEquals(false, player.doIParticipateInTournament());
	}
	
	@Test
	public void playCardsForTournament() {
		PlayerManager pm = new PlayerManager(2, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		Player p2 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		pm.players[1] = p2;
		pm.nextTurn();
		
		TournamentModel tm = new TournamentModel();
		tm.questionJoinPlayers(pm.round());
		tm.acceptTournament(p2);
		tm.playersWhoJoined();
		
		
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A3(p1, pm, new BoardModelMediator(tm, null, null));
		assertTrue(compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Lance"}));
		
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(tm, null, null));
		assertTrue(compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Lance", "Sword", "Dagger", "Battle-ax"}));
		
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Boar",5,15, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Boar",5,15, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(tm, null, null));
		assertTrue(compare(player.playCardsForTournament(),  new String[] {"Battle-ax"}));

	}
	
	@Test
	public void doISponserAQuest() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		
		pm.nextTurn();
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertEquals(null, player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Green Knight",25,40, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertEquals(null, player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Saxon Knight",15,25, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertEquals(true, compareList(player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Thieves"}, {"Dragon"}}));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		p1.hand.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertEquals(true, compareList(player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Thieves"}, {"Dragon"}}));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		p1.hand.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new TestCard("Test of Valor", TYPE.TESTS));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertEquals(true, compareList(player.doISponsorAQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Test of Valor"}, {"Dragon"}}));
	}
	
	@Test
	public void doIParticipateInQuest() {
		PlayerManager pm = new PlayerManager(2, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		Player p2 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		pm.players[1] = p2;
		pm.nextTurn();
		
		QuestModel tm = new QuestModel();
		Quest quest = new Quest(new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"}), tm);
		tm.setQuest(quest, new ArrayList<Player>());
		tm.questionJoinQuest(Arrays.asList(pm.players));
		tm.acceptQuest(p2);
		tm.playerWhoJoined();
		
		logger.info("Testing 1");
		
		AbstractAI player = new A3(p1, pm, new BoardModelMediator(null, tm, null));
		assertEquals(true, player.doIParticipateInQuest(new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"})));
		
		logger.info("Testing 2");
		tm.getQuest().getStage(0).addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(null, tm, null));
		assertEquals(true, player.doIParticipateInQuest(new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"})));
		
		logger.info("Testing 3");
		tm.getQuest().getStage(0).addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		tm.getQuest().getStage(0).addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(null, tm, null));
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"})));
		
	}
	
	@Test
	public void playCardsForFoeQuest() {
		PlayerManager pm = new PlayerManager(2, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		Player p2 = new Player(0, new UIPlayer("", ""));
		pm.players[0] = p1;
		pm.players[1] = p2;
		pm.nextTurn();
		
		QuestModel tm = new QuestModel();
		Quest quest = new Quest(new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"}), tm);
		tm.setQuest(quest, new ArrayList<Player>());
		tm.questionJoinQuest(Arrays.asList(pm.players));
		tm.acceptQuest(p2);
		tm.playerWhoJoined();
		quest.advanceStage();
		
		logger.info("Testing 1");
		
		QuestCard card = new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"});
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A3(p1, pm, new BoardModelMediator(null, tm, null));
		assertTrue(compare(player.playCardsForFoeQuest(card), 
				new String[] {}));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("", ""));
		tm.getQuest().getStage(0).addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(null, tm, null));
		assertTrue(compare(player.playCardsForFoeQuest(card), 
				new String[] {"Excalibur"}));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("", ""));
		tm.getQuest().getStage(0).addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		tm.getQuest().getStage(0).addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.hand.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.hand.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(null, tm, null));
		assertTrue(compare(player.playCardsForFoeQuest(card), 
				new String[] {"Excalibur", "Sword", "King Arthur"}));
		
	}
	
	@Test
	public void nextBid() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertTrue(-1 == player.nextBid(Integer.MAX_VALUE));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertTrue(-1 == player.nextBid(1));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertTrue(-1 == player.nextBid(1));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		assertTrue(2 == player.nextBid(0));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(1 == player.nextBid(0));
		
		logger.info("Testing 6");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(3 == player.nextBid(0));
		
		logger.info("Testing 7");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(5 == player.nextBid(0));
	}
	
	@Test
	public void discardAfterWinningTest() {
		logger.info("Testing 1");
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 2");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 3");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {}));
		
		logger.info("Testing 4");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 1;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {"Thieves", "Thieves"}));
		
		logger.info("Testing 5");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 2;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {"Dragon"}));
		
		logger.info("Testing 6");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 2;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {"Dragon", "Dragon", "Dragon"}));
		
		logger.info("Testing 7");
		p1 = new Player(0, new UIPlayer("", ""));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A3(p1, pm, new BoardModelMediator(new TournamentModel(), null, null));
		player.rounds = 2;
		assertTrue(compare(player.discardAfterWinningTest(), new String[] {"Dragon", "Dagger", "Dagger", "Thieves", "Thieves"}));
	}

	public static boolean compareList(List<List<AdventureCard>> playCardsForTournmanet, String[][] strings) {
		boolean flag = true;
		logger.info("3: " + playCardsForTournmanet);
		if(strings.length != playCardsForTournmanet.size()) return false;
		
		for(int i = 0; i < strings.length;i++) {
			flag = flag & compare(playCardsForTournmanet.get(i), strings[i]);
		}
		return flag;
	}
	
	public static boolean compare( List<AdventureCard> playCardsForTournament, String[] string) {
		ArrayList<String> cards = new ArrayList<String>(Arrays.asList(string));
		logger.info("1: " + cards);
		logger.info("2: " + playCardsForTournament);
		for(Card card: playCardsForTournament) {
			for(int i = 0; i < cards.size(); i++) {
				if(cards.get(i).equals(card.getName())) {
					cards.remove(i);
					break;
				}
			}
		}
		return cards.size() == 0 && playCardsForTournament.size() == string.length;
	}
}
