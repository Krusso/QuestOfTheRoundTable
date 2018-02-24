package client;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.client.GameBoardController;
import src.client.Main;
import src.client.TitleScreenController;
import src.socket.Server;

public class TestTitleScreen extends TestFXBase{

	final String NEW_GAME_BUTTON_ID = "#newGame";
	final String MENU_BUTTON_1_ID = "#b1";
	final String MENU_PANE_ID = "#menuPane";
	final String START_BUTTON_ID = "#start";
	final String ERROR_MESSAGE_ID = "#errorMsg";
	final String PLAYER_SELECT_ID = "#playerSelect";
	
	
	/**
	 * Tests to ensure menu pane will display after new game button is clicked
	 * @throws InterruptedException 
	 */
	@Test
	public void testStartGame() {
		clickOn(NEW_GAME_BUTTON_ID);
		verifyThat(MENU_PANE_ID, (Pane mp)->{
			boolean isPaneVisible = mp.isVisible();
			System.out.println(isPaneVisible);
			return !isPaneVisible;
		});
		verifyThat(PLAYER_SELECT_ID, (Pane ps) -> {
			boolean isPaneVisible = ps.isVisible();
			System.out.println(isPaneVisible);
			ObservableList<Node> children = ps.getChildren();
			return isPaneVisible;
		});
		WaitForAsyncUtils.waitForFxEvents();
		//no players
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must have at least 1 human player to start the game")) {
				return true;
			}
			return false;
		});
		//p0 = ai
		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_AI_ID);
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must have at least 1 human player to start the game")) {
				return true;
			}
			return false;
		});
		//p0 = human
		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_HUMAN_ID);
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must have at least 2 players to start game")) {
				return true;
			}
			return false;
		});
		//p0 0 = human p1 = ai (shields are same tho)
		clickOn(MENU_BUTTON_3_ID);
		clickOn(MENU_OPTION_3_AI_ID);
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Players cannot have the same shield")) {
				return true;
			}
			return false;
		});
		clickOn(TITLE_PANE_2_ID);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must choose players in order, (e.g if you have player 1, player 0 must exists as well)")) {
				return true;
			}
			return false;
		});
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);
		clickOn(TITLE_PANE_1_ID);
		clickOn(NEXT_SHIELD_BUTTON_1_ID);
		clickOn(NEXT_SHIELD_BUTTON_1_ID);
		clickOn(START_BUTTON_ID);
		

		WaitForAsyncUtils.waitForFxEvents();
		TitleScreenController tsc = m.getTitleScreenController();
		GameBoardController gbc = tsc.getGameBoardController();
//		System.out.println(gbc);
		assertTrue(gbc != null);
	}

}
