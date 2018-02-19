package src.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import src.game_logic.AdventureCard;
import src.game_logic.AllyCard;
import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.QuestCard;
import src.game_logic.AdventureCard.TYPE;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.quest.QuestBidClient;
import src.messages.quest.QuestDiscardCardsClient;
import src.messages.quest.QuestDiscardCardsServer;
import src.messages.quest.QuestJoinClient;
import src.messages.quest.QuestPickCardsClient;
import src.messages.quest.QuestPickStagesClient;
import src.messages.quest.QuestSponsorClient;
import src.player.Player;
import src.player.PlayerManager;
import src.player.TestA2;
import src.socket.OutputController;
import src.views.PlayerView;
import src.views.PlayersView;

public class TestQuests {
	final static Logger logger = LogManager.getLogger(TestQuests.class);
	@Test
	public void testDiscardForQuests() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",1,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put(new QuestSponsorClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put(new QuestSponsorClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

		while(true) {
			Message string = actualOutput.take();
			logger.info(string);
			if(string.message == MESSAGETYPES.PICKSTAGES && string.player == 3) break;
		}

		// sending cards for each stage
		input.put(new QuestPickStagesClient(3, new String[] {"Boar"}, 0) );

		// agree to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.JOINQUEST && string.player == i) break;
			}
			input.put(new QuestJoinClient(i, true));
			Thread.sleep(100);
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}

		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Galahad",15, TYPE.ALLIES));
	}


	@Test
	public void testDontNeedToBidAnything() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",1,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put(new QuestSponsorClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put(new QuestSponsorClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

		while(true) {
			Message string = actualOutput.take();
			logger.info(string);
			if(string.message == MESSAGETYPES.PICKSTAGES && string.player == 3) break;
		}

		// sending cards for each stage
		input.put(new QuestPickStagesClient(3, new String[] {"Test of Valor"}, 0) );

		// agree to participate as 1 player
		for(int i = 0; i < 3; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.JOINQUEST && string.player == i) break;
			}
			if(i == 0) {
				input.put(new QuestJoinClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.YES, players.get(i).getQuestion());
			} else {
				input.put(new QuestJoinClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			}
		}

		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		players.get(0).addCards(cards);
		players.get(0).setFaceDown(cards.stream().map(i -> i.getName()).toArray(String[]::new));
		players.get(0).flipCards();

		// bidding first time
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(0).getQuestion());
		input.put(new QuestBidClient(0, 3));

		Thread.sleep(100);
		assertEquals(Player.STATE.TESTDISCARD, players.get(0).getQuestion());

		while(true) {
			Message string = actualOutput.take();
			logger.info(gson.toJson(string));
			if(string.message == MESSAGETYPES.DISCARDQUEST && string.player == 0) {
				assertTrue(((QuestDiscardCardsServer) string).cardsToDiscard == 0);
				break;
			}
		}
	}

	@Test
	public void test2PlayerBidding() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",1,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put(new QuestSponsorClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put(new QuestSponsorClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

				while(true) {
					Message string = actualOutput.take();
					logger.info(string);
					if(string.message == MESSAGETYPES.PICKSTAGES && string.player == 3) break;
				}

		// sending cards for each stage
		input.put(new QuestPickStagesClient(3, new String[] {"Test of Valor"}, 0) );

		// agree to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.JOINQUEST && string.player == i) break;
			}
			if(i == 0) {
				input.put(new QuestJoinClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.YES, players.get(i).getQuestion());
			} else {
				input.put(new QuestJoinClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			}
		}

		// bidding first time
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(0).getQuestion());
		input.put(new QuestBidClient(0, 3));

		Thread.sleep(100);
		assertEquals(Player.STATE.TESTDISCARD, players.get(0).getQuestion());

		while(true) {
			Message string = actualOutput.take();
			logger.info(gson.toJson(string));
			if(string.message == MESSAGETYPES.DISCARDQUEST && string.player == 0) break;
		}

		input.put(new QuestDiscardCardsClient(2, new String[]{"Thieves", "Saxons", "Saxons"}));
		Thread.sleep(100);
		assertEquals(10, players.get(0).getCardCount());
		assertEquals(1, players.get(0).getShields());
		assertEquals(0, players.get(1).getShields());
		assertEquals(0, players.get(2).getShields());
		assertEquals(0, players.get(3).getShields());
	}

	@Test
	public void testNoSponsor() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",3,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as each player
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(gson.toJson(string));
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			input.put(new QuestSponsorClient(i, false));
			Thread.sleep(100);
			assertEquals(Player.STATE.NO, players.get(i).getQuestion());
		}

		// no one accepted
		assertEquals(0,players.get(0).getShields());
		assertEquals(0,players.get(1).getShields());
		assertEquals(0,players.get(2).getShields());
		assertEquals(0,players.get(3).getShields());
	}

	@Test
	public void testNoParticipants() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",3,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put(new QuestSponsorClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put(new QuestSponsorClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

		while(true) {
			Message string = actualOutput.take();
			logger.info(string);
			if(string.message == MESSAGETYPES.PICKSTAGES && string.player == 3) break;
		}

		// sending cards for each stage
		input.put(new QuestPickStagesClient(3, new String[] {"Boar"}, 0) );
		input.put(new QuestPickStagesClient(3, new String[] {"Saxons"}, 1) );
		input.put(new QuestPickStagesClient(3, new String[] {"Saxons","Thieves"}, 2) );

		// decline to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.JOINQUEST && string.player == i) break;
			}
			input.put(new QuestJoinClient(i, false));
			Thread.sleep(100);
			assertEquals(Player.STATE.NO, players.get(i).getQuestion());
		}
	}

	@Test
	public void testBidding() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",1,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put(new QuestSponsorClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put(new QuestSponsorClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

		while(true) {
			Message string = actualOutput.take();
			logger.info(string);
			if(string.message == MESSAGETYPES.PICKSTAGES && string.player == 3) break;
		}

		// sending cards for each stage
		input.put(new QuestPickStagesClient(3, new String[] {"Test of Valor"}, 0) );

		// agree to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.JOINQUEST && string.player == i) break;
			}
			input.put(new QuestJoinClient(i, true));
			Thread.sleep(100);
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}

		// bidding first time
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(0).getQuestion());
		input.put(new QuestBidClient(0, 1));
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(1).getQuestion());
		input.put(new QuestBidClient(1, -1));
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(2).getQuestion());
		input.put(new QuestBidClient(2, 2));


		// bidding second time
		Thread.sleep(100);
		assertEquals(Player.STATE.BIDDING, players.get(0).getQuestion());
		input.put(new QuestBidClient(0, -1));

		Thread.sleep(100);
		assertEquals(Player.STATE.TESTDISCARD, players.get(2).getQuestion());

		while(true) {
			Message string = actualOutput.take();
			logger.info(gson.toJson(string));
			if(string.message == MESSAGETYPES.DISCARDQUEST && string.player == 2) break;
		}

		input.put(new QuestDiscardCardsClient(2, new String[]{"Thieves", "Saxons"}));
		Thread.sleep(100);
		assertEquals(11, players.get(2).getCardCount());
		assertEquals(0, players.get(0).getShields());
		assertEquals(0, players.get(1).getShields());
		assertEquals(1, players.get(2).getShields());
		assertEquals(0, players.get(3).getShields());

	}

	@Test
	public void testFightingFoe() throws InterruptedException {
		QuestSequenceManager qsm = new QuestSequenceManager(new QuestCard("Slay the Dragon",2,"Dragon"));
		QOTRTQueue input = new QOTRTQueue();
		Runnable task2 = () -> { qsm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		Iterator<Player> iter = pm.round();
		ArrayList<Player> players = new ArrayList<Player>();
		iter.forEachRemaining(i -> players.add(i));

		// decline to sponsor as first 3 players, accept to sponsor as 4th
		for(int i = 0; i < 4; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.SPONSERQUEST && string.player == i) break;
			}
			assertEquals(Player.STATE.QUESTQUESTIONED, players.get(i).getQuestion());
			if(i<3) {
				input.put(new QuestSponsorClient(i, false));
				Thread.sleep(100);
				assertEquals(Player.STATE.NO, players.get(i).getQuestion());
			} else {
				input.put(new QuestSponsorClient(i, true));
				Thread.sleep(100);
				assertEquals(Player.STATE.SPONSORING, players.get(i).getQuestion());
			}
		}

		while(true) {
			Message string = actualOutput.take();
			logger.info(string);
			if(string.message == MESSAGETYPES.PICKSTAGES && string.player == 3) break;
		}

		// sending cards for each stage
		input.put(new QuestPickStagesClient(3, new String[] {"Boar"}, 0) );
		input.put(new QuestPickStagesClient(3, new String[] {"Saxons"}, 1) );

		// agree to participate as all players
		for(int i = 0; i < 3; i++) {
			while(true) {
				Message string = actualOutput.take();
				logger.info(string);
				if(string.message == MESSAGETYPES.JOINQUEST && string.player == i) break;
			}
			input.put(new QuestJoinClient(i, true));
			Thread.sleep(100);
			assertEquals(Player.STATE.YES, players.get(i).getQuestion());
		}

		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(0).getQuestion());
		input.put(new QuestPickCardsClient(0, new String[] {"Lance"}));
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(1).getQuestion());
		input.put(new QuestPickCardsClient(0, new String[] {"Lance", "Excalibur"}));
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(2).getQuestion());
		input.put(new QuestPickCardsClient(0, new String[] {"Battle-ax"}));

		while(true) {
			Message string = actualOutput.take();
			logger.info(gson.toJson(string));
			if(string.message == MESSAGETYPES.PICKQUEST && string.player == 0) break;
		}

		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(0).getQuestion());
		input.put(new QuestPickCardsClient(0, new String[] {"Lance"}));
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(1).getQuestion());
		input.put(new QuestPickCardsClient(1, new String[] {"Lance","Excalibur"}));
		Thread.sleep(100);
		assertEquals(Player.STATE.QUESTPICKING, players.get(2).getQuestion());
		input.put(new QuestPickCardsClient(2, new String[] {"Dagger"}));

		Thread.sleep(100);
		assertEquals(12, players.get(2).getCardCount());
		assertEquals(2, players.get(0).getShields());
		assertEquals(2, players.get(1).getShields());
		assertEquals(0, players.get(2).getShields());
		assertEquals(0, players.get(3).getShields());
	}

	DeckManager dm;
	PlayerManager pm;
	BoardModel bm;
	PlayerView pv;
	OutputController oc;
	LinkedBlockingQueue<String> output;
	Gson gson = new Gson();
	@Before
	public void before() {
		output = new LinkedBlockingQueue<String>();
		oc = new OutputController(output);
		dm = new DeckManager();
		pm = new PlayerManager(4, dm, true);
		bm = new BoardModel();
		pv = new PlayerView(oc);
		pm.subscribe(pv);
		PlayersView pvs = new PlayersView(oc);
		pm.subscribe(pvs);
		pm.start();
		pm.nextTurn();
	}

}
