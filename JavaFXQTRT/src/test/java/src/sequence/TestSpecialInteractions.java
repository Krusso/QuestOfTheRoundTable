package src.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.EventCard;
import src.game_logic.TournamentCard;
import src.game_logic.WeaponCard;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.hand.HandFullClient;
import src.player.Player;
import src.player.PlayerManager;
import src.socket.OutputController;
import src.views.PlayerView;
import src.views.PlayersView;

public class TestSpecialInteractions {

	final static Logger logger = LogManager.getLogger(TestSpecialInteractions.class);
	
	@Test
	public void discardAmourAlliesWhenHandFull() throws InterruptedException {
		QOTRTQueue input = new QOTRTQueue();
		dsm = new DiscardSequenceManager(input, pm, bm);
		pm.setDiscardSequenceManager(dsm);
		input.setPlayerManager(pm);
		Runnable task2 = () -> { dsm.start(input, pm, bm); };
		new Thread(task2).start();
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		Player player = pm.round().next();
		player.addCards(cards);
		assertEquals(17,player.hand.size());
		
		input.put(new HandFullClient(0, new String[] {"Horse"}, new String[] {"Amour", "Queen Iseult"}));
		
		Thread.sleep(500);
		assertTrue(player.iseult);
		assertTrue(player.getFaceDownDeck().findCardByName("Amour") != null);
		assertTrue(player.getFaceDownDeck().size() == 2);
	}
	
	@Test
	public void testRecieveDiscardMessage() throws InterruptedException {
		QOTRTQueue input = new QOTRTQueue();
		EventSequenceManager esm = new EventSequenceManager(new EventCard("Queen's Favor"));
		dsm = new DiscardSequenceManager(input, pm, bm);
		pm.setDiscardSequenceManager(dsm);
		Runnable task2 = () -> { esm.start(input, pm, bm); };
		new Thread(task2).start();
		LinkedBlockingQueue<Message> actualOutput = oc.internalQueue;
		while(true) {
			Message string = actualOutput.take();
			logger.info(string.message);
			if(string.message == MESSAGETYPES.DISCARDHANDFULL) break;
		}
		assertTrue(true);
	}
	
	@Test
	public void testDiscardWhenFullHand() throws InterruptedException {
		QOTRTQueue input = new QOTRTQueue();
		dsm = new DiscardSequenceManager(input, pm, bm);
		pm.setDiscardSequenceManager(dsm);
		input.setPlayerManager(pm);
		Runnable task2 = () -> { dsm.start(input, pm, bm); };
		new Thread(task2).start();
		ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
		cards.add(new AllyCard("Sir Tristan",10,20, TYPE.ALLIES));
		cards.add(new AllyCard("Queen Iseult",0,0,2,4, TYPE.ALLIES));
		cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
		cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
		cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
		Player player = pm.round().next();
		player.addCards(cards);
		assertEquals(17,player.hand.size());
		
		input.put(new HandFullClient(0, new String[] {"Horse", "Amour"}, new String[] {}));
		Thread.sleep(500);
		assertEquals(15, player.hand.size());
	}
	
	DeckManager dm;
	PlayerManager pm;
	BoardModel bm;
	PlayerView pv;
	OutputController oc;
	LinkedBlockingQueue<String> output;
	DiscardSequenceManager dsm;
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
