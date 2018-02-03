package src.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import src.game_logic.Card;

public class GameBoardController implements Initializable{

	private Client c;
	private ArrayList<Card> card;
	@FXML private Pane handWindow;
	
	public void addCardToHand(Card c) {
		card.add(c);
		handWindow.getChildren().add(c.getImageView());
	}
	
	public void setClient(Client c) {
		this.c = c;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		card = new ArrayList<Card>();
	}

}
