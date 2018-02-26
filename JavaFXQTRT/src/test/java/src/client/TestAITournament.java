package src.client;

import static org.testfx.api.FxAssert.verifyThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TestAITournament extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestAITournament.class);

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
		press(KeyCode.A);
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
		Pane fdc0 = gbc.playerFaceDown0;
		
		// start turn first player
		clickOn(START_TURN);
		
		//p0 is going to accept the quest
		clickOn(ACCEPT);
		
		//p1 AI turn declines
		clickOn(START_TURN);
		
		//p2 AI turn accepts
		clickOn(START_TURN);
		
		//p0 turn
		clickOn(START_TURN);
		//back to player 1 discarding extra cards
		while(gbc.playerManager.players[0].hand.size() > 12) {
			drag(gbc.playerManager.players[0].hand.getDeck().get(12).imgView).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		WaitForAsyncUtils.waitForFxEvents();
		clickOn(DISCARD);
		
		//p2 turn
		Thread.sleep(20);
		clickOn(START_TURN);
		
		//p1 turn
		Thread.sleep(20);
		clickOn(START_TURN);
		drag(gbc.findCardInHand("Lance")).moveTo(fdc0).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Battle-ax")).moveTo(fdc0).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Excalibur")).moveTo(fdc0).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		
		//p2 AI plays for tournament
		Thread.sleep(20);
		clickOn(START_TURN);

		Thread.sleep(20);
		clickOn(START_TURN);
		
		// p1
		Thread.sleep(20);
		clickOn(START_TURN);
		
		// p2
		Thread.sleep(20);
		clickOn(START_TURN);
		
		// p0
		Thread.sleep(20);
		clickOn(START_TURN);
		clickOn(DECLINE);
		
		// p1
		Thread.sleep(20);
		clickOn(START_TURN);
		
		// p2
		Thread.sleep(20);
		clickOn(START_TURN);
		clickOn(START_TURN);
		
		//p0
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		clickOn(START_TURN);
		clickOn(DECLINE);
		
		//p1
		Thread.sleep(20);
		clickOn(START_TURN);

		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		
		verifyThat(TOAST, (Text t)->{
			if(t.getText().equals("Player: [2] has won the game!")) {
				return true;
			}
			return false;
		});
		
	}
}
