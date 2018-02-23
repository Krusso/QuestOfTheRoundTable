package client;

import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import src.client.Main;
import src.client.TitleScreenController;
import src.socket.Server;

public abstract class TestFXBase extends ApplicationTest{
	TitleScreenController tsc;
//	@Before
//	public void setupClass() throws Exception{
//		Runnable task2 = () -> { Server.main(null); };
//		// start the thread
//		new Thread(task2).start();
//		ApplicationTest.launch(Main.class);
//	}

	@Override 
	public void start(Stage stage) throws Exception{
		
		stage.show();
	}
	
	@After
	public void afterEachTest() throws Exception{
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
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
