package com.qotrt.scenarios;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class TestAIQuestSponsorA1 extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestAIQuestSponsorA1.class);

	GameBoardController gbc;
	TitleScreenController tsc;


	final String START_TURN = "#startTurn";
	final String ACCEPT = "#accept";
	final String DECLINE = "#decline";
	final String END_TURN = "#endTurn";
	final String TOAST = "#toast";
	final String DISCARD = "#discard";
	final String USE_MERLIN = "#useMerlin";
	final String CONTINUE = "#nextTurn";
	@Before
	public void setup3Players(){
		//Rig the game
		press(KeyCode.D);
		clickOn(NEW_GAME_BUTTON_ID);
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);

		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_AI_ID);
		clickOn(MENU_BUTTON_2_ID);
		clickOn(TITLE_PANE_2_ID);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		
		clickOn(MENU_BUTTON_3_ID);
		clickOn(MENU_OPTION_3_AI2_ID);
		clickOn(MENU_BUTTON_3_ID);
		clickOn(TITLE_PANE_3_ID);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		clickOn(PREV_SHIELD_BUTTON_3_ID);
		
		clickOn(START_BUTTON_ID); 

		WaitForAsyncUtils.waitForFxEvents();

		logger.info("m: " + m);
		//get tsc
		tsc = m.getTitleScreenController();
	}
	
	@Test
	public void testAIQuestSponsorA1() throws InterruptedException {	
		Thread.sleep(100);
		WaitForAsyncUtils.waitForFxEvents();
		gbc = tsc.getGameBoardController();
				
		// start turn first player
		clickOn(START_TURN);
		
		//p0 is going to accept the quest
		clickOn(DECLINE);
		
		//p1 AI turn accepts
		clickOn(START_TURN);
		
		Thread.sleep(100);
		//p2 AI turn accepts
		clickOn(START_TURN);
		
		clickOn(ACCEPT);
		Thread.sleep(100);
		
		clickOn(START_TURN);
		Thread.sleep(100);
		
		// p0
		clickOn(START_TURN);
		Thread.sleep(100);
		while(gbc.playerManager.players[0].hand.size() > 12) {
			drag(gbc.playerManager.players[0].hand.getDeck().get(12).imgView).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(DISCARD);
		
		Thread.sleep(100);
		clickOn(START_TURN);
		
		clickOn(START_TURN);
		Thread.sleep(100);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gbc.bidSlider.setValue(6);
			}
		});
		
		Thread.sleep(100);
		clickOn(END_TURN);
		
		clickOn(START_TURN);
		Thread.sleep(100);
		
		clickOn(START_TURN);
		Thread.sleep(100);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gbc.bidSlider.setValue(10);
			}
		});
		
		Thread.sleep(100);
		clickOn(END_TURN);
		
		Thread.sleep(100);
		clickOn(START_TURN);
		Thread.sleep(100);
		
		clickOn(START_TURN);
		Thread.sleep(100);
		
		assertEquals("Select 10 cards to discard", gbc.toast.getText());
	}
}
