package src.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.FoeCard;
import src.game_logic.WeaponCard;

public class TestAbstractAI {
	final static Logger logger = LogManager.getLogger(TestAbstractAI.class);

	@Test
	public void testDiscardWhenHandFull() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm);
		assertEquals(2, player.discardWhenHandFull(2).size());
	}

	@Test
	public void testDiscardKingsCalltoArmsWeaponsFoes() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		AbstractAI player = new A1(p1, pm);
		
		List<AdventureCard> discarded = player.discardKingsCalltoArms(2, TYPE.WEAPONS);
		assertTrue(discarded.get(0).getName().equals("Excalibur") || discarded.get(0).getName().equals("Lance"));
		assertTrue(discarded.get(1).getName().equals("Excalibur") || discarded.get(1).getName().equals("Lance"));
		assertEquals(2, discarded.size());
		
		discarded = player.discardKingsCalltoArms(2, TYPE.FOES);
		assertTrue(discarded.get(0).getName().equals("Thieves"));
		assertTrue(discarded.get(1).getName().equals("Thieves"));
		assertEquals(2, discarded.size());
	}
	
	@Test
	public void testDiscardKingsCalltoArmsWeaponFoes1() {
		UIPlayerManager pm = new UIPlayerManager(1);
		
		UIPlayer p1 = new UIPlayer(0);
		pm.players[0] = p1;
		p1.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		AbstractAI player = new A1(p1, pm);
		
		List<AdventureCard> discarded = player.discardKingsCalltoArms(1, TYPE.WEAPONS);
		assertTrue(discarded.get(0).getName().equals("Excalibur") || discarded.get(0).getName().equals("Lance"));
		assertEquals(1, discarded.size());
		
		discarded = player.discardKingsCalltoArms(1, TYPE.FOES);
		assertTrue(discarded.get(0).getName().equals("Thieves"));
		assertEquals(1, discarded.size());
		
		discarded = player.discardKingsCalltoArms(0, TYPE.FOES);
		assertEquals(0, discarded.size());
	}
}
