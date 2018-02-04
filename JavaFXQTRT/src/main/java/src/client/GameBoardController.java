package src.client;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import src.game_logic.AdventureCard;
import src.game_logic.Card;
import src.game_logic.StoryCard;

public class GameBoardController implements Initializable{

	private Client c;
	private UIPlayerManager playerManager;
	private File resDir = new File("src/main/resources/");

	@FXML private Pane handWindow;
	@FXML private Pane playField;
	@FXML private VBox storyContainer;
	@FXML private Pane storyCardContainer;
	@FXML private Button endTurn;
	@FXML private Button accept;
	@FXML private Button decline;
	@FXML private Text playerNumber;

	public void initPlayerManager(int numPlayers) {
		//		players = new UIPlayer[numPlayers];
		//		for(int i = 0 ; i < players.length ; i++) {
		//			players[i] = new UIPlayer(i);
		//		}
		playerManager = new UIPlayerManager(numPlayers);
	}

	public void addCardToHand(AdventureCard c, int playerNum) {
		c.gbc = this;
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
		this.playerNumber.setText("#" + p);
	}

	public void setStoryCard(StoryCard sc) {
		storyCardContainer.getChildren().add(sc.getImageView());
	}


	public void showPlayerHand(int playerNum) {
		playerManager.showPlayerHand(playerNum);
		repositionCardsInHand(playerNum);
	}

	//When player drags a card over the playfield Pane, we add the card into cards selected
	public void playCard(Card card, double d, double e) {
		Bounds boundsHand = handWindow.localToScene(handWindow.getBoundsInLocal());
		Bounds boundsPlay = playField.localToScene(playField.getBoundsInLocal());

		// Cards topleft corner = boundsHand.getMinY(), boundsHand.getMinX + d
		// give +10 on each side of the boundsPlay box to be nice to user
		if(boundsHand.getMinY() >= boundsPlay.getMinY() - 10 &&
				boundsHand.getMinY() <= boundsPlay.getMaxY() + 10 &&
				// plus 200 because we want to let the user play it on the story card too just so that we dont create
				// a gap where if the card is dropped user cant move it
				boundsHand.getMinX() + d >= boundsPlay.getMinX() - 210 &&
				boundsHand.getMinX() + d <= boundsPlay.getMaxX() + 10 && !card.inPlay) {
			System.out.println("in the box");
			handWindow.getChildren().remove(card.getImageView());
			playField.getChildren().add(card.getImageView());
			card.getImageView().setX(100 * playerManager.getFaceDownLength(playerManager.getCurrentPlayer()) + 10);
			card.getImageView().setY(0);
			card.inPlay = true;
			playerManager.playCard((AdventureCard) card, playerManager.getCurrentPlayer());
			repositionCardsInHand(playerManager.getCurrentPlayer());
		} 
		// TODO: let user put back to his hand
		else {
			System.out.println("out of the box");
			card.returnOriginalPosition();
		}

	}


	public void setClient(Client c) {
		this.c = c;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//		storyContainer.getChildren().add(storyCard);
	}

	public void setUp() {
		this.endTurn.setOnAction(e -> {
			System.out.println("clicked end turn");
			this.setButtonsInvisible();
			c.send("game tournament picked: player " + playerManager.getCurrentPlayer() + " " + playerManager.getFaceDownCards(playerManager.getCurrentPlayer()));
		});
		this.accept.setOnAction(e -> {
			System.out.println("accepted tournament");
			this.setButtonsInvisible();
			c.send("game tournament accept: player " + playerManager.getCurrentPlayer());
		});
		this.decline.setOnAction(e -> {
			System.out.println("declined tournament");
			this.setButtonsInvisible();
			c.send("game tournament decline: player " + playerManager.getCurrentPlayer());
		});
		this.setButtonsInvisible();
	}
	public void clearPlayField() {
		this.playField.getChildren().clear();
	}
	
	public void setButtonsInvisible() {
		this.endTurn.setVisible(false);
		this.accept.setVisible(false);
		this.decline.setVisible(false);
	}
	
	public void showAcceptDecline() {
		this.accept.setVisible(true);
		this.decline.setVisible(true);
	}

	public void showEndTurn() {
		this.endTurn.setVisible(true);
	}
	
}
