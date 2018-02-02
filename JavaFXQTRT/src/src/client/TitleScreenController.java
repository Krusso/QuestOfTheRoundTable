package src.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
		System.out.println("print");
		Parent root = new AnchorPane();
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("GameBoard.fxml"));
		root = fxmlLoader.load();
		client.send("game start:" + numPlayers.getText());
		Scene gameBoardScene = new Scene(root);
		
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		stage.setScene(gameBoardScene);
		stage.show();
		
		//pass the client to the GameBoardController
//		GameBoardController gbc = fxmlLoader.getController();
//		client.setGameBoardController(gbc);
//		gbc.setClient(client);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
