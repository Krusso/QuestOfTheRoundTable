package src.game_client;

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
import javafx.stage.Stage;

public class TitleScreenController implements Initializable{

	@FXML
	private Button startButton;

	@FXML
	private void handleButtonAction(ActionEvent event) throws Exception {
//		System.out.println("Button clicked");
//		Parent gameBoard = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
//		Scene gameBoardScene = new Scene(gameBoard);
//		
//		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
//		stage.setScene(gameBoardScene);
//		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
