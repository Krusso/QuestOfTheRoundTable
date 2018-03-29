package com.qotrt.scenarios;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import src.game_logic.AdventureCard;

public class TestScenario4 extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestScenario4.class);

	GameBoardController gbc;
	TitleScreenController tsc;


	final String START_TURN = "#startTurn";
	final String ACCEPT = "#accept";
	final String END_TURN = "#endTurn";
	final String TOAST = "#toast";
	final String DISCARD = "#discard";
	final String USE_MERLIN = "#useMerlin";
	final String CONTINUE = "#nextTurn";
	@Before
	public void setup2Players(){
		//Rig the game
		press(KeyCode.RIGHT);
		clickOn(NEW_GAME_BUTTON_ID);
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);

		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_HUMAN_ID);
		clickOn(MENU_BUTTON_2_ID);
		clickOn(TITLE_PANE_2_ID);
		sleep(1000);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		clickOn(START_BUTTON_ID); 

		WaitForAsyncUtils.waitForFxEvents();

		logger.info("m: " + m);
		//get tsc
		tsc = m.getTitleScreenController();
	}

	/**
	 * Search for the holy grail quest
	 * test of the green knight quest
	 * tournament at york
	 * pox
	 * plague
	 * queens favor
	 * court called to camelot
	 * kings call to arms
	 * kings recognition
	 */
	@Test
	public void testLongScenario() throws InterruptedException {
		sleep(1000);
		gbc = tsc.getGameBoardController();
		// start turn first player
		clickOn(START_TURN);

		Pane stage1 = gbc.stages[0];
		Pane stage2 = gbc.stages[1];
		Pane stage3 = gbc.stages[2];
		Pane stage4 = gbc.stages[3];
		Pane stage5 = gbc.stages[4];
		ImageView thieves = gbc.findCardInHand("Thieves");
		ImageView saxons = gbc.findCardInHand("Saxons");
		ImageView robber = gbc.findCardInHand("Robber Knight");
		ImageView mordred = gbc.findCardInHand("Mordred");
		ImageView greenknight = gbc.findCardInHand("Green Knight");
		//p0 is going to accept the quest
		clickOn(ACCEPT);

		Thread.sleep(100);
		drag(thieves).moveTo(stage1).release(MouseButton.PRIMARY);
		drag(robber).moveTo(stage2).release(MouseButton.PRIMARY);
		drag(saxons).moveTo(stage3).release(MouseButton.PRIMARY);
		drag(mordred).moveTo(stage4).release(MouseButton.PRIMARY);
		drag(greenknight).moveTo(stage5).release(MouseButton.PRIMARY);

		Thread.sleep(100);
		assertEquals("5", gbc.bpTextStage0.getText());
		assertEquals("15", gbc.bpTextStage1.getText());
		assertEquals("20", gbc.bpTextStage2.getText());
		assertEquals("30", gbc.bpTextStage3.getText());
		assertEquals("40", gbc.bpTextStage4.getText());

		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();

		//2nd player's turn, we're going to accept the quest
		Pane fdc2 = gbc.playerFaceDown1;
		Pane fdc1 = gbc.playerFaceDown0;
		clickOn(START_TURN); 
		clickOn(ACCEPT);

		WaitForAsyncUtils.waitForFxEvents();

		ImageView discard = gbc.playerManager.players[1].hand.getDeck().get(12).imgView;
		ImageView sirpercival2 = gbc.findCardInHand("Sir Percival");
		drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		//finsh discarding so we'll click it now
		clickOn(DISCARD);

		//now p2 should be choosing cards to play for the quest stage 1.
		//now play a card for the quest dagger
		drag(sirpercival2).moveTo(fdc2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(START_TURN);

		Thread.sleep(100);
		//continue next stage 2
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		Thread.sleep(100);
		//continue next stage 3
		discard = gbc.playerManager.players[1].hand.getDeck().get(12).imgView;
		drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		//finsh discarding so we'll click it now
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(DISCARD);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);


		//continue next stage 4
		Thread.sleep(100);
		discard = gbc.playerManager.players[1].hand.getDeck().get(12).imgView;
		drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		//finsh discarding so we'll click it now
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(DISCARD);
		ImageView Lance2 = gbc.findCardInHand("Lance");
		drag(Lance2).moveTo(fdc2).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		//continue next stage 5
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		Lance2 = gbc.findCardInHand("Lance");
		drag(Lance2).moveTo(fdc2).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		Thread.sleep(100);
		clickOn(START_TURN);
		//back to player 1 discarding extra cards
		while(gbc.playerManager.players[0].hand.size() > 12) {
			discard = gbc.playerManager.players[0].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(DISCARD);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);
		WaitForAsyncUtils.waitForFxEvents();

		//player 2 turn to sponsor next quest
		clickOn(ACCEPT);


		thieves = gbc.findCardInHand("Thieves");
		saxons = gbc.findCardInHand("Saxons");
		robber = gbc.findCardInHand("Robber Knight");
		greenknight = gbc.findCardInHand("Green Knight");

		WaitForAsyncUtils.waitForFxEvents();
		drag(thieves).moveTo(stage1).release(MouseButton.PRIMARY);
		drag(saxons).moveTo(stage2).release(MouseButton.PRIMARY);
		drag(robber).moveTo(stage3).release(MouseButton.PRIMARY);
		drag(greenknight).moveTo(stage4).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		assertEquals("5", gbc.bpTextStage0.getText());
		assertEquals("10", gbc.bpTextStage1.getText());
		assertEquals("15", gbc.bpTextStage2.getText());
		assertEquals("40", gbc.bpTextStage3.getText());

		Thread.sleep(100);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();

		//1nd player's turn, we're going to accept the quest
		Thread.sleep(100);
		clickOn(START_TURN); 
		Thread.sleep(100);
		clickOn(ACCEPT);

		WaitForAsyncUtils.waitForFxEvents();

		while(gbc.playerManager.players[0].hand.size() > 12) {
			discard = gbc.playerManager.players[0].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		ImageView sirgawain = gbc.findCardInHand("Sir Gawain");
		//finsh discarding so we'll click it now
		Thread.sleep(100);
		clickOn(DISCARD);

		//now p1 should be choosing cards to play for the quest stage 1.
		//now play a card for the quest dagger
		drag(sirgawain).moveTo(fdc1).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		Thread.sleep(100);
		//continue next stage 2
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		Thread.sleep(100);
		//continue next stage 3
		while(gbc.playerManager.players[0].hand.size() > 12) {
			discard = gbc.playerManager.players[0].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		//finsh discarding so we'll click it now
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(DISCARD);
		Thread.sleep(100);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		//continue next stage 4
		while(gbc.playerManager.players[0].hand.size() > 12) {
			discard = gbc.playerManager.players[0].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		//finsh discarding so we'll click it now
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(DISCARD);
		WaitForAsyncUtils.waitForFxEvents();
		Lance2 = gbc.findCardInHand("Lance");
		drag(Lance2).moveTo(fdc1).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		clickOn(END_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);

		Thread.sleep(100);
		clickOn(START_TURN);
		//back to player 2 discarding extra cards
		while(gbc.playerManager.players[1].hand.size() > 12) {
			discard = gbc.playerManager.players[1].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(DISCARD);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(START_TURN);
		WaitForAsyncUtils.waitForFxEvents();


		//Quest over back to player 1 both will join the tournament
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(ACCEPT);

		// join tournament as player 2
		Thread.sleep(100);
		clickOn(START_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(ACCEPT);
		while(gbc.playerManager.players[1].hand.size() > 12) {
			discard = gbc.playerManager.players[1].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		Thread.sleep(100);
		clickOn(DISCARD);

		// play lance and amour as player 1
		Thread.sleep(100);
		clickOn(START_TURN);
		WaitForAsyncUtils.waitForFxEvents();
		drag(gbc.findCardInHand("Lance")).moveTo(fdc1).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Amour")).moveTo(fdc1).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		clickOn(END_TURN);

		// play mordred as player 2 and amour
		Thread.sleep(100);
		clickOn(START_TURN);
		int index = 0;
		for(AdventureCard c : gbc.playerManager.getPlayerHand(1)) {
			if(c.getName().equals("Mordred")) {
				final int index1 = index;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						gbc.useMordred(c, index1, gbc.playerManager.getCurrentPlayer(), 0, 0, "Sir Gawain");
					}
				});
				break;
			}
			index++;
		}
		Thread.sleep(100);
		drag(gbc.findCardInHand("Amour")).moveTo(fdc2).release(MouseButton.PRIMARY);
		Thread.sleep(100);
		clickOn(END_TURN);

		//reveal
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);

		// pox
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);

		//plague 
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);

		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		while(gbc.playerManager.players[0].hand.size() > 12) {
			discard = gbc.playerManager.players[0].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(DISCARD);

		Thread.sleep(100);
		clickOn(START_TURN);

		/*queens favor
		 * court called to camelot
		 * kings call to arms
		 * kings recognition */

		//queens favor
		Thread.sleep(100);
		while(gbc.playerManager.players[1].hand.size() > 12) {
			discard = gbc.playerManager.players[1].hand.getDeck().get(12).imgView;
			drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(DISCARD);

		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);

		//court called to camelot
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);

		//kings call to arms
		Thread.sleep(100);
		discard = gbc.findCardInHand("Battle-ax");
		drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(END_TURN);
		
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		Thread.sleep(100);
		discard = gbc.findCardInHand("Excalibur");
		drag(discard).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(END_TURN);
		
		
		clickOn(START_TURN);
		Thread.sleep(100);
		clickOn(START_TURN);

		assertEquals(0, gbc.playerManager.players[0].getShields());
		assertEquals(0, gbc.playerManager.players[1].getShields());
	}
}
