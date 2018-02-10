package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.messages.game.GameStartClient;

public class TitleScreenController implements Initializable{

	private Client client;
	
	@FXML private Text	numPlayerslabel;
	@FXML private TextField numPlayers;
	@FXML private Button start;
	@FXML private Pane p;
	@FXML public Pane background;
	private ArrayList<String> players;
	
	@FXML private MenuButton b1;
	@FXML private MenuButton b2;
	@FXML private MenuButton b3;
	@FXML private MenuButton b4;
	
	public TitleScreenController() { 
		this.players = new ArrayList<String>();
		// init with capacity not working for some reason
		players.add("");
		players.add("");
		players.add("");
		players.add("");
		System.out.println(this.players.size());
	}
	
	public void setClient(Client c) {
		client = c;
	}
	public void addImage(ImageView i) {
		p.getChildren().add(i);
	}

	// sorry guys
	@FXML private void setHuman1(ActionEvent e) throws IOException { this.players.set(0, "Human"); b1.setText("Human"); }
	@FXML private void setHuman2(ActionEvent e) throws IOException { this.players.set(1, "Human"); b2.setText("Human"); }
	@FXML private void setHuman3(ActionEvent e) throws IOException { this.players.set(2, "Human"); b3.setText("Human"); }
	@FXML private void setHuman4(ActionEvent e) throws IOException { this.players.set(3, "Human"); b4.setText("Human"); }
	@FXML private void setAI1(ActionEvent e) throws IOException { this.players.set(0, "AI"); b1.setText("AI"); }
	@FXML private void setAI2(ActionEvent e) throws IOException { this.players.set(1, "AI"); b2.setText("AI"); }
	@FXML private void setAI3(ActionEvent e) throws IOException { this.players.set(2, "AI"); b3.setText("AI"); }
	@FXML private void setAI4(ActionEvent e) throws IOException { this.players.set(3, "AI"); b4.setText("AI"); }
	
	@FXML
	private void handleButtonAction(ActionEvent e) throws IOException {
//		String n = numPlayers.getText();
//		int players = Integer.parseInt(n);
//		if(players >= 2 && players <= 4) {}
		
		for(int i=0;i<players.size();i++) { System.out.println(players.get(i)); }
	
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("GameBoard.fxml"));
		Scene gameScene = new Scene(fxmlLoader.load());
		
		//scale the game application to full screen
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double scaleX = screenBounds.getMaxX()/1920; //FXML anchor pane width is 1024 and height is 768
		double scaleY = screenBounds.getMaxY()/1080;
//			System.out.println("scale X: " + scaleX);
//			System.out.println("scale y: " + scaleY);
		scaleScene(gameScene,scaleX,scaleY);
		
		//give the GameBoardController the client if we got it
		//in this case the GBC should always have the client when we initialize it
		System.out.println("GameBoardController has reference to Client");
		GameBoardController gbc = fxmlLoader.getController();
		gbc.setClient(client);
		gbc.setUp();
		client.setGameBoardController(gbc);
		//Change the scene to the gameScene and show it.
		
		Stage stage = (Stage) start.getScene().getWindow();	
		stage.setScene(gameScene);
		stage.show();
		
		//Setup player manager
		gbc.initPlayerManager(players.size());
		
		//send gameStart message.
		client.send(new GameStartClient(players.size()));
	}
	
	//Gives the capability to scale the screen based on the scale factor (1.0 = 100%, 0.5 = 50% etc)
	private void scaleScene(Scene scene, double scaleX, double scaleY) {
		Scale scale = new Scale(scaleX,scaleY);
		scale.setPivotX(0);
		scale.setPivotY(0);
		scene.getRoot().getTransforms().setAll(scale);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
