package src.client;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class TestAITournamentCreateA2 extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestAITournamentCreateA2.class);

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
		clickOn(MENU_OPTION_2_AI2_ID);
		clickOn(MENU_BUTTON_2_ID);
		clickOn(TITLE_PANE_2_ID);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		
		clickOn(MENU_BUTTON_3_ID);
		clickOn(MENU_OPTION_3_AI_ID);
		clickOn(MENU_BUTTON_3_ID);
		clickOn(TITLE_PANE_3_ID);
		try {
			Thread.sleep(1000);
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
	public void testAITournament() throws InterruptedException {		
		gbc = tsc.getGameBoardController();
				
		// start turn first player
		Thread.sleep(20);
		clickOn(START_TURN);
		
		//p0 is going to accept the quest
		Thread.sleep(20);
		clickOn(DECLINE);
		
		//p1 AI turn accepts
		Thread.sleep(20);
		clickOn(START_TURN);
		
		//p2 AI turn accepts
		Thread.sleep(20);
		clickOn(START_TURN);

		Thread.sleep(20);
		clickOn(ACCEPT);
		Thread.sleep(20);
		
		clickOn(START_TURN);
		Thread.sleep(20);
		
		// p0
		clickOn(START_TURN);
		Thread.sleep(20);
		while(gbc.playerManager.players[0].hand.size() > 12) {
			drag(gbc.playerManager.players[0].hand.getDeck().get(12).imgView).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(DISCARD);

		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		
		clickOn(START_TURN);
		Thread.sleep(20);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gbc.bidSlider.setValue(3);
			}
		});
		
		Thread.sleep(100);
		clickOn(END_TURN);
		
		clickOn(START_TURN);
		Thread.sleep(20);
		
		clickOn(START_TURN);
		Thread.sleep(20);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gbc.bidSlider.setValue(5);
			}
		});
		
		Thread.sleep(100);
		clickOn(END_TURN);
		
		assertEquals("Select: 5 cards to discard", gbc.toast.getText());
	}
}
