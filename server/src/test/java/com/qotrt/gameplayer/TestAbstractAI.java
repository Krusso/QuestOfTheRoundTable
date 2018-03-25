package com.qotrt.gameplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.model.RiggedModel.RIGGED;
import com.qotrt.model.UIPlayer;

public class TestAbstractAI {
	final static Logger logger = LogManager.getLogger(TestAbstractAI.class);

	@Test
	public void testDiscardWhenHandFull() {
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		
		pm.players[0] = p1;
		
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		AbstractAI player = new A1(p1, pm);
		assertEquals(2, player.discardWhenHandFull(2).size());
	}

	@Test
	public void testDiscardKingsCalltoArmsWeaponsFoes() {
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		
		pm.players[0] = p1;
		
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
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
		PlayerManager pm = new PlayerManager(1, new UIPlayer[] {}, null, RIGGED.ONE);
		Player p1 = new Player(0, new UIPlayer("", ""));
		
		pm.players[0] = p1;
		
		pm.players[0] = p1;
		p1.hand.addCard(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
		p1.hand.addCard(new WeaponCard("Lance",20, TYPE.WEAPONS));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
		p1.hand.addCard(new FoeCard("Thieves",5, TYPE.FOES));
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
