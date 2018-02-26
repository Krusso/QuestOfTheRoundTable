package src.client;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

public class TestAITournamentJoin extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestAITournamentJoin.class);

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
		press(KeyCode.C);
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
		clickOn(START_TURN);
		
		//p0 is going to accept the quest
		clickOn(ACCEPT);
		
		Pane stage1 = gbc.stages[0];
		Pane stage2 = gbc.stages[1];
		
		drag(gbc.findCardInHand("Thieves")).moveTo(stage1).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Green Knight")).moveTo(stage2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);

		WaitForAsyncUtils.waitForFxEvents();
		
		//p1 AI turn declines
		clickOn(START_TURN);
		
		//p2 AI turn accepts
		clickOn(START_TURN);
		
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		clickOn(START_TURN);
		

		assertEquals("Player #[3] won the game!", gbc.toast.getText());
		
	}
}
