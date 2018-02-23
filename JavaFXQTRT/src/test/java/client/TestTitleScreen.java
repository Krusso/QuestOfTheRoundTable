package client;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TestTitleScreen extends TestFXBase{
	
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
		
		assertTrue(lookup(START_BUTTON_ID).query() == null);
	}

}
