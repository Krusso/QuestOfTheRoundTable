package src.game_client;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameBoardScene {
	Client client;
	Scene scene;
	public GameBoardScene(Client client) {
    	Pane canvas = new Pane();
    	scene = new Scene(canvas, 1280, 800, Color.WHITE);
	}

}
