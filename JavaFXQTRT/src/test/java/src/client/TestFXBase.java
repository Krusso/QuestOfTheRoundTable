package src.client;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import src.client.Main;
import src.socket.Server;

public abstract class TestFXBase extends ApplicationTest{
	Main m;
	Thread server;

	final String NEW_GAME_BUTTON_ID = "#newGame";
	final String MENU_BUTTON_1_ID = "#b1";
	final String MENU_BUTTON_2_ID = "#b2";
	final String MENU_BUTTON_3_ID = "#b3";
	final String MENU_BUTTON_4_ID = "#b4";

	final String MENU_OPTION_1_HUMAN_ID = "#b1h";
	final String MENU_OPTION_2_HUMAN_ID = "#b2h";
	final String MENU_OPTION_3_HUMAN_ID = "#b3h";
	final String MENU_OPTION_4_HUMAN_ID = "#b4h";

	final String MENU_OPTION_1_AI_ID = "#b1a";
	final String MENU_OPTION_2_AI_ID = "#b2a";
	final String MENU_OPTION_3_AI_ID = "#b3a";

	final String TITLE_PANE_1_ID = "#tp1";
	final String TITLE_PANE_2_ID = "#tp2";
	final String TITLE_PANE_3_ID = "#tp3";
	final String TITLE_PANE_4_ID = "#tp4";

	final String NEXT_SHIELD_BUTTON_1_ID = "#s1next";
	final String NEXT_SHIELD_BUTTON_2_ID = "#s2next";
	final String NEXT_SHIELD_BUTTON_3_ID = "#s3next";
	final String PREV_SHIELD_BUTTON_4_ID = "#s4prev";

	final String MENU_PANE_ID = "#menuPane";
	final String START_BUTTON_ID = "#start";
	final String ERROR_MESSAGE_ID = "#errorMsg";
	final String PLAYER_SELECT_ID = "#playerSelect";
	@Before
	public void setupClass() throws Exception{

	}

	@Override 
	public void start(Stage stage) throws Exception{
		LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
		LinkedBlockingQueue<String> output = new LinkedBlockingQueue<String>();
		Main.input = input;
		Main.output = output;
		Runnable task2 = () -> { new Server(Main.input, Main.output); };
		// start the thread
		server = new Thread(task2);
		server.start();
		m = new Main();
		m.start(stage);
	}

	@After
	public void afterEachTest() throws Exception{
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
		FxToolkit.cleanupApplication(m);
		server.interrupt();
	}

	@SuppressWarnings("unchecked")
	public <T extends Node> T find(final String query) {
		return (T) lookup(query).queryAll().iterator().next();
	} 

	public <T extends Node> T findFromPane(final Pane p, final String query) {
		ObservableList<Node> children = p.getChildren();
		for(Node n : children) {
			if(n.getId() != null) {
				if(n.getId().equals(query)) {
					return (T) n;
				}
			}
		}
		return null;
	}

}
