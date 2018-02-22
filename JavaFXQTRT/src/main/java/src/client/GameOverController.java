package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.messages.game.GameStartClient;

public class GameOverController implements Initializable{
	@FXML public Pane bg;
	@FXML public Button mainScreen;
	@FXML public Text text;

	@FXML
	private void handleButtonAction(ActionEvent e) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("TitleScreen.fxml"));
		Scene title = new Scene(fxmlLoader.load());
		Stage stage = (Stage) mainScreen.getScene().getWindow();	
		stage.setScene(title);
		stage.show();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub


	}

}
