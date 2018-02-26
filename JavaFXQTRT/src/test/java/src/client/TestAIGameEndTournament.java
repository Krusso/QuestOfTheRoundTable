package src.client;

import static org.junit.Assert.assertEquals;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.input.KeyCode;


public class TestAIGameEndTournament extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestAIGameEndTournament.class);

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
		press(KeyCode.E);
		clickOn(NEW_GAME_BUTTON_ID);
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);

		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_AI_ID);
		clickOn(MENU_BUTTON_2_ID);
		clickOn(TITLE_PANE_2_ID);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		
		clickOn(MENU_BUTTON_3_ID);
		clickOn(MENU_OPTION_3_AI2_ID);
		clickOn(MENU_BUTTON_3_ID);
		clickOn(TITLE_PANE_3_ID);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clickOn(PREV_SHIELD_BUTTON_3_ID);
		
		clickOn(START_BUTTON_ID); 

		WaitForAsyncUtils.waitForFxEvents();

		logger.info("m: " + m);
		//get tsc
		tsc = m.getTitleScreenController();
		
		WaitForAsyncUtils.waitForFxEvents();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void testAITournament() throws InterruptedException {
		gbc = tsc.getGameBoardController();
		
		// start turn first player
		clickOn(START_TURN);
		
		//event over
		clickOn(START_TURN);
		
		Thread.sleep(20);
		clickOn(START_TURN);
		
		//select cards for final tournament
		clickOn(END_TURN);
		
		Thread.sleep(20);
		//A1
		clickOn(START_TURN);
		
		Thread.sleep(20);
		//A2
		clickOn(START_TURN);
		
		Thread.sleep(100);
		
		
		assertEquals("Player #[2] won the game!", gbc.toast.getText());
	}
}