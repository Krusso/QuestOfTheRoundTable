package src.game_client;

import java.io.FileInputStream;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application{

	private String cardPath = "resources/cards/";
	Client client;
    @Override
    public void start(Stage stage) throws Exception {

    	Client client = new Client("localhost", 2223);
    	
    	Pane canvas = new Pane();
    	Scene scene = new Scene(canvas, 1280, 800, Color.WHITE);

    	//Label
    	Text numPlayersLabel = new Text("Number of players");
    	
    	//Creating textfield
    	TextField numPlayers = new TextField();

    	//Creating buttons
    	Button startButton = new Button("Start");
    	startButton.setOnAction(e -> client.send("game start:" + numPlayers.getText()));
    	
    	//Creating gridpane
    	GridPane gridPane = new GridPane();
    	gridPane.setMinSize(500, 400);
    	gridPane.setPadding(new Insets(10,10,10,10));
    	
    	gridPane.add(numPlayersLabel, 0, 0);
    	gridPane.add(numPlayers, 0, 1);
    	gridPane.add(startButton, 0, 2);
    	
    	canvas.getChildren().add(gridPane);
    	
    	
    	stage.setTitle("Quest of the Round Table");
    	stage.setScene(scene);
    	stage.show();
    	
    	String message = null;
    	while(true) {
    		message = client.readData();
    		if(message != null) {
    			System.out.println("message received:" + message);
    		}
    	}
    	
    }

    public static void main(String[] args) {
        launch();
    }
}