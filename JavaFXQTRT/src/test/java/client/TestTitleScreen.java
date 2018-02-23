package client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
	public void ensureMenuPaneShows() {
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
			
			for(Node n : children) {
				System.out.println(n);
				if(n instanceof Button) {
					Button b = (Button)n;
					if(b.getId().equals("start")) {
						System.out.println(b);
					}
				}
			}
			return isPaneVisible;
		});
		Pane ps = find(PLAYER_SELECT_ID);
		
		Button n =  findFromPane(ps, "start");
		System.out.println(n);
		WaitForAsyncUtils.waitForFxEvents();
	
//		verifyThat(START_BUTTON_ID, (Button start) -> {
//			clickOn(START_BUTTON_ID);
//			return true;
//		});
		clickOn(START_BUTTON_ID);
		sleep(1000);
		Text errorMsg = find(ERROR_MESSAGE_ID);
		assertTrue(errorMsg.getText().equals("Must have at least 1 human player to start the game"));
	}

}
