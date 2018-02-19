package src.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.Card;
import src.game_logic.FoeCard;
import src.game_logic.QuestCard;
import src.game_logic.TestCard;
import src.game_logic.WeaponCard;

public class TestA2 {
	
	@Test
	public void doIParticipateInTournament() {
		AbstractAI player = new A2(new UIPlayer(0), null);
		//what a great test good job me
		assertEquals(true, player.doIParticipateInTournament());
	}
	
	@Test
	public void playCardsForTournament() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, pm);
		assertEquals(true, compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Lance"}));
		
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertEquals(true, compare(player.playCardsForTournament(),  new String[] {"Excalibur"}));
		
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertEquals(true, compare(player.playCardsForTournament(),  new String[] {"Excalibur", "Lance"}));
		
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Boar",5,15, TYPE.FOES));
		p1.addCard(new FoeCard("Boar",5,15, TYPE.FOES));
		p1.addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertEquals(true, compare(player.playCardsForTournament(),  new String[] {"Battle-ax"}));

		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Boar",5,15, TYPE.FOES));
		p1.addCard(new AllyCard("Sir Lancelot",15,25, TYPE.ALLIES));
		p1.addCard(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertEquals(true, compare(player.playCardsForTournament(), new String[] {"Battle-ax", "Sir Lancelot"}));
	}
	
	@Test
	public void doISponserAQuest() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, pm);
		List<Player> participants = new ArrayList<Player>();
		assertEquals(true, compareList(player.doISponserAQuest(participants, 
				new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {}));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.addCard(new FoeCard("Green Knight",25,40, TYPE.FOES));
		player = new A2(p1, pm);
		assertEquals(true, compareList(player.doISponserAQuest(participants, 
				new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {}));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Saxon Knight",15,25, TYPE.FOES));
		p1.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, pm);
		assertEquals(true, compareList(player.doISponserAQuest(participants, 
				new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Thieves"}, {"Dragon"}}));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		p1.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, pm);
		assertEquals(true, compareList(player.doISponserAQuest(participants, 
				new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Thieves"}, {"Dragon"}}));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
		p1.addCard(new FoeCard("Saxons",10,20, TYPE.FOES));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new TestCard("Test of Valor", TYPE.TESTS));
		player = new A2(p1, pm);
		assertEquals(true, compareList(player.doISponserAQuest(participants, 
				new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})), 
				new String[][] {{"Test of Valor"}, {"Dragon"}}));
	}
	
	@Test
	public void doIParticipateInQuest() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, pm);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Mordred",30, TYPE.FOES));
		p1.addCard(new FoeCard("Green Knight",25,40, TYPE.FOES));
		player = new A2(p1, pm);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, pm);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertEquals(false, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new WeaponCard("Horse",10, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Horse",10, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertEquals(true, player.doIParticipateInQuest(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"})));
	}
	
	@Test
	public void playCardsForFoeQuest() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		System.out.println("Testing 1");
		QuestCard card = new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"});
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, pm);
		assertTrue(compare(player.playCardsForFoeQuest(false, card), 
				new String[] {}));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertTrue(compare(player.playCardsForFoeQuest(false, card), 
				new String[] {"Sword"}));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertTrue(compare(player.playCardsForFoeQuest(false, card), 
				new String[] {"King Arthur"}));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.addCard(new AmourCard("Amour",10,1, TYPE.AMOUR));
		player = new A2(p1, pm);
		assertTrue(compare(player.playCardsForFoeQuest(false, card), 
				new String[] {"Amour"}));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Sword",10, TYPE.WEAPONS));
		p1.addCard(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
		p1.addCard(new AmourCard("Amour",10,1, TYPE.AMOUR));
		player = new A2(p1, pm);
		assertTrue(compare(player.playCardsForFoeQuest(true, card), 
				new String[] {"Amour", "Excalibur", "Sword", "King Arthur"}));
	}
	
	@Test
	public void nextBid() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, pm);
		assertTrue(-1 == player.nextBid(1, Integer.MAX_VALUE));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertTrue(-1 == player.nextBid(1, 0));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(-1 == player.nextBid(1, 0));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(2 == player.nextBid(1, 0));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(1 == player.nextBid(2, 0));
		
		System.out.println("Testing 6");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(3 == player.nextBid(2, 0));
		
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
		player = new A2(p1, pm);
		assertTrue(5 == player.nextBid(2, 0));
	}
	
	@Test
	public void discardAfterWinningTest() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		System.out.println("Testing 1");
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 2");
		p1 = new UIPlayer(0);
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 3");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Giant",40, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(1), new String[] {}));
		
		System.out.println("Testing 4");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(1), new String[] {"Thieves", "Thieves"}));
		
		System.out.println("Testing 5");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(2), new String[] {"Dragon"}));
		
		System.out.println("Testing 6");
		p1 = new UIPlayer(0);
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		p1.addCard(new FoeCard("Dragon",50,70, TYPE.FOES));
		player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(2), new String[] {"Dragon", "Dragon", "Dragon"}));
		
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
		player = new A2(p1, pm);
		assertTrue(compare(player.discardAfterWinningTest(2), new String[] {"Dragon", "Dagger", "Dagger", "Thieves", "Thieves"}));
	}

	public static boolean compareList(List<List<Card>> playCardsForTournmanet, String[][] strings) {
		boolean flag = true;
		System.out.println("3: " + playCardsForTournmanet);
		if(strings.length != playCardsForTournmanet.size()) return false;
		
		for(int i = 0; i < strings.length;i++) {
			flag = flag & compare(playCardsForTournmanet.get(i), strings[i]);
		}
		return flag;
	}
	
	public static boolean compare( List<Card> playCardsForTournament, String[] string) {
		ArrayList<String> cards = new ArrayList<String>(Arrays.asList(string));
		System.out.println("1: " + cards);
		System.out.println("2: " + playCardsForTournament);
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
