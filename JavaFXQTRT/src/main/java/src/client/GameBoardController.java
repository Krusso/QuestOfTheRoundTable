package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import src.game_logic.AdventureCard;
import src.game_logic.Card;
import src.game_logic.StoryCard;
import src.player.Player;
import src.player.PlayerManager;

public class GameBoardController implements Initializable{

	private Client c;
	private UIPlayerManager playerManager;
	private File resDir = new File("src/main/resources/");

	@FXML private Pane handWindow;
	@FXML private Pane playField;
	@FXML private VBox storyContainer;
	@FXML private Pane storyCardContainer;
	
	public void initPlayerManager(int numPlayers) {
//		players = new UIPlayer[numPlayers];
//		for(int i = 0 ; i < players.length ; i++) {
//			players[i] = new UIPlayer(i);
//		}
		playerManager = new UIPlayerManager(numPlayers);
	}
	
	public void addCardToHand(AdventureCard c, int playerNum) {
		playerManager.addCardToHand(c, playerNum);
		handWindow.getChildren().add(c.getImageView());
	}
	
	private void repositionCardsInHand(int pNum) {
		ArrayList<AdventureCard> currHand = playerManager.getPlayerHand(pNum);
		for(int i = 0 ; i < currHand.size(); i++) {
			double windowWidth = handWindow.getWidth();
			currHand.get(i).getImageView().setX(windowWidth/currHand.size() * i);
			currHand.get(i).getImageView().setY(handWindow.getTranslateY());
		}
	}
	
	public void setPlayerTurn(int p) {
		playerManager.setCurrentPlayer(p);
	}
	
	public void setStoryCard(StoryCard sc) {
		storyCardContainer.getChildren().add(sc.getImageView());
	}
	
	
	
	public void showPlayerHand(int playerNum) {
		playerManager.showPlayerHand(playerNum);
		repositionCardsInHand(playerNum);
	}
	
	
	//When player drags a card over the playfield Pane, we add the card into cards selected
	@FXML 
	public void playCard() {
		
	}
	
	
	public void setClient(Client c) {
		this.c = c;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		storyContainer.getChildren().add(storyCard);
		
	}
	

}
