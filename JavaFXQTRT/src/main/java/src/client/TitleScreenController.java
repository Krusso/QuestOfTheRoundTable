package src.client;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TitleScreenController implements Initializable{

	private Client client;
	
	@FXML private Text	numPlayerslabel;
	@FXML private TextField numPlayers;
	@FXML private Button start;
	@FXML private Pane p;
	public void setClient(Client c) {
		client = c;
	}
	public void addImage(ImageView i) {
		p.getChildren().add(i);
	}
	
	@FXML
	private void handleButtonAction(ActionEvent e) throws IOException {
		
		String n = numPlayers.getText();
		int players = Integer.parseInt(n);
		if(players >= 2 && players <= 4) {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("GameBoard.fxml"));
			Scene gameScene = new Scene(fxmlLoader.load());
			
			//scale the game application to full screen
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			double scaleX = screenBounds.getMaxX()/1024; //FXML anchor pane width is 1024 and hieght is 768
			double scaleY = screenBounds.getMaxY()/768;
			System.out.println("scale X: " + scaleX);
			System.out.println("scale y: " + scaleY);
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
			gbc.initPlayerManager(players);
			
			//send gameStart message.
			client.send("game start:" + n);
		}
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
