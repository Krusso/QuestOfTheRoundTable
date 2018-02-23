package client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import junit.framework.Assert;
import src.client.Main;
import src.client.TitleScreenController;
import src.socket.Server;

import static org.testfx.api.FxAssert.*;

import javax.naming.ldap.ManageReferralControl;

public class TestTitleScreen extends TestFXBase{

	final String NEW_GAME_BUTTON_ID = "#newGame";
	final String MENU_BUTTON_1_ID = "#b1";
	final String MENU_PANE_ID = "#menuPane";
	final String START_BUTTON_ID = "#start";
	final String ERROR_MESSAGE_ID = "#errorMsg";
	final String PLAYER_SELECT_ID = "#playerSelect";
		
	@Override
	public void start(Stage stage) throws Exception {
		Runnable task2 = () -> { Server.main(null); };
		// start the thread
		new Thread(task2).start();
		new Main().start(stage);
	}
	
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
		clickOn("#b2");
		clickOn("#b2a");
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must have at least 1 human player to start the game")) {
				return true;
			}
			return false;
		});
		//p0 = human
		clickOn("#b2");
		clickOn("#b2h");
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must have at least 2 players to start game")) {
				return true;
			}
			return false;
		});
		//p0 0 = human p1 = ai (shields are same tho)
		clickOn("#b3");
		clickOn("#b3a");
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Players cannot have the same shield")) {
				return true;
			}
			return false;
		});
		clickOn("#tp2");
		clickOn("#s2next");
		clickOn(START_BUTTON_ID);
		verifyThat(ERROR_MESSAGE_ID, (Text t)->{
			if(t.getText().equals("Must choose players in order, (e.g if you have player 1, player 0 must exists as well)")) {
				return true;
			}
			return false;
		});
		clickOn("#b1");
		clickOn("#b1h");
		clickOn("#tp1");
		clickOn("#s1next");
		clickOn("#s1next");
		clickOn(START_BUTTON_ID);
		
		assertTrue(lookup(START_BUTTON_ID).query() == null);
	}

}
