package src.player;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import src.client.UIPlayerManager;
import src.game_logic.WeaponCard;
import src.game_logic.AdventureCard.TYPE;

public class TestAbstractAI {
	final static Logger logger = LogManager.getLogger(TestAbstractAI.class);

	@Test
	public void testDiscardWhenHandFull() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm);
		assertTrue(player.discardWhenHandFull(2).size() == 2);
	}

}
