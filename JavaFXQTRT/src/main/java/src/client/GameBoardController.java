package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import src.game_logic.AdventureCard;
import src.game_logic.Card;
import src.game_logic.Rank;
import src.game_logic.StoryCard;
import src.messages.game.ContinueGameClient;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.messages.tournament.TournamentPickCardsClient;

public class GameBoardController implements Initializable{

	private static final int NUM_POSITIONS = 4;

	private Client c;
	private UIPlayerManager playerManager;
	private File resDir = new File("src/main/resources/");
	
	@FXML private Pane playField;
	@FXML private VBox storyContainer;
	@FXML private Pane storyCardContainer;
	@FXML private Button endTurn;
	@FXML private Button accept;
	@FXML private Button decline;
	@FXML private Text playerNumber;
	@FXML private Button nextTurn;
	@FXML private Pane background;
	//The pane that holds the other players' hand


	//These panes are for hold each player's respective items, e.g hand, face up card, face down cards etc
	//When rotating, we only rotate these panes.
	@FXML private Pane playerPane0;
	@FXML private Pane playerPane1;
	@FXML private Pane playerPane2;
	@FXML private Pane playerPane3;
	private Pane playerPanes[] = new Pane[4];

	@FXML private Pane playerhand0;
	@FXML private Pane playerHand1;
	@FXML private Pane playerHand2;
	@FXML private Pane playerHand3;
	@FXML private Pane[] handPanes = new Pane[4];

	//TODO: make the face down field in FXML
	//The panes that govern the player's facedown cards
	@FXML private Pane playerFaceDown0;
	@FXML private Pane playerFaceDown1;
	@FXML private Pane playerFaceDown2;
	@FXML private Pane playerFaceDown3;
	private Pane[] faceDownPanes = new Pane[4];

	//TODO: make the faceup field in FXML
	//The panes that govern the player's faceup cards
	@FXML private Pane playerFaceUp0;
	@FXML private Pane playerFaceUp1;
	@FXML private Pane playerFaceUp2;
	@FXML private Pane playerFaceUp3;
	//TODO: initialize these in the init function
	private Pane[] faceUpPanes = new Pane[4];

	@FXML private ImageView playerRank0;
	@FXML private ImageView playerRank1;
	@FXML private ImageView playerRank2;
	@FXML private ImageView playerRank3;
	private ImageView[] playerRanks = new ImageView[4];

	@FXML public Text toast;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		playerPanes[0] = playerPane0;
		playerPanes[1] = playerPane1;
		playerPanes[2] = playerPane2;
		playerPanes[3] = playerPane3;
		handPanes[0] = playerhand0;
		handPanes[1] = playerHand1;
		handPanes[2] = playerHand2; 
		handPanes[3] = playerHand3;
		faceDownPanes[0] = playerFaceDown0;
		faceDownPanes[1] = playerFaceDown1;
		faceDownPanes[2] = playerFaceDown2;
		faceDownPanes[3] = playerFaceDown3;
		faceUpPanes[0] = playerFaceUp0;
		faceUpPanes[1] = playerFaceUp1;
		faceUpPanes[2] = playerFaceUp2;
		faceUpPanes[3] = playerFaceUp3;
		playerRanks[0] = playerRank0;
		playerRanks[1] = playerRank1;
		playerRanks[2] = playerRank2;
		playerRanks[3] = playerRank3;

	}
	////Must call this when you click start game in title screen!
	public void initPlayerManager(int numPlayers) {
		playerManager = new UIPlayerManager(numPlayers);
		for(int i = 0 ; i < numPlayers ; i++) {
			setPlayerRank(i, Rank.RANKS.SQUIRE);;
		}
	}




	//Put's Cards into the Pane and repositions it to fit the width
	private void putCardIntoPane(Pane p, Card... c) {
		for(Card card : c) {
			p.getChildren().add(card.getImageView());
		}
		//TODO reposition the the cards in this pane
		//		for(int i = 0 ; i < p.getChildren().size() ; i++) {
		//			
		//		}
	}


	public void addCardToHand(AdventureCard c, int playerNum) {
		c.gbc = this;
		c.faceDownPane = faceDownPanes[playerNum];
		playerManager.addCardToHand(c, playerNum);
		handPanes[playerNum].getChildren().add(c.getImageView());
		repositionCardsInHand(playerNum);
	}


	//Some raunchy way of setting player perspective to playerNum
	public void setPlayerPerspectiveTo(int playerNum) {
		int currentPlayer = playerManager.getCurrentPlayer();

		while(currentPlayer != playerNum) {
			if(currentPlayer == 3) {
				currentPlayer = 0;
			}else {
				currentPlayer++;
			}
			rotatePlayerPosition();
		}

	}

	//This rotates the player's pane clockwise 90 degrees
	private void rotatePlayerPosition() {
		double posX3 = playerPanes[3].getLayoutX();
		double posY3 = playerPanes[3].getLayoutY();	
		for(int i = playerPanes.length-1 ; i >= 0 ; i--) {
			int pos = i-1 < 0 ? 3 : i-1;
			if(i == 0) {
				playerPanes[i].relocate(posX3, posY3);
			}else {
				playerPanes[i].relocate(playerPanes[pos].getLayoutX(), playerPanes[pos].getLayoutY());
			}
			playerPanes[i].setRotate(playerPanes[i].getRotate() + 90);
		}
	}
	//Repositions the cards in the hand of this player
	private void repositionCardsInHand(int pNum) {
		ArrayList<AdventureCard> currHand = playerManager.getPlayerHand(pNum);
		double windowWidth = playerhand0.getWidth();
		for(int i = 0 ; i < currHand.size(); i++) {
			currHand.get(i).getImageView().setX(windowWidth/currHand.size() * i);
			currHand.get(i).getImageView().setY(0);
		}
	}

	public void setPlayerTurn(int p) {
		System.out.println("Set Player Turn to :" + p);
		setPlayerPerspectiveTo(p);
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

	//Puts currently selected card into the specified pane with precision of d and e
	public void playCard(Card card, Pane p, double d, double e) {
		int currPlayer = playerManager.getCurrentPlayer();

		//Gets the bounds for the current player's perspective
		Bounds boundsHand = handPanes[currPlayer].localToScene(handPanes[currPlayer].getBoundsInLocal());// playerhand0.localToScene(playerhand0.getBoundsInLocal());

		//the bounds to play will be outside of the player's hand
		Bounds boundsPlay = p.localToScene(p.getBoundsInLocal());//playField.localToScene(playField.getBoundsInLocal());

		// Cards topleft corner = boundsHand.getMinY(), boundsHand.getMinX + d
		// give +10 on each side of the boundsPlay box to be nice to user
		if(boundsHand.getMinY() >= boundsPlay.getMinY() - 10 &&
				boundsHand.getMinY() <= boundsPlay.getMaxY() + 10 &&
				// plus 200 because we want to let the user play it on the story card too just so that we dont create
				// a gap where if the card is dropped user cant move it
				boundsHand.getMinX() + d >= boundsPlay.getMinX() - 210 &&
				boundsHand.getMinX() + d <= boundsPlay.getMaxX() + 10 && !card.inPlay) {
			System.out.println("in the box");
			handPanes[currPlayer].getChildren().remove(card.getImageView());
			p.getChildren().add(card.getImageView());
			card.inPlay = true;
			playerManager.playCard((AdventureCard) card, playerManager.getCurrentPlayer());
			repositionCardsInHand(playerManager.getCurrentPlayer());
			//			System.out.println("Length : " + playerManager.getFaceDownLength(playerManager.getCurrentPlayer()));
			card.getImageView().setX(100 * (playerManager.getFaceDownLength(playerManager.getCurrentPlayer()) - 1) + 10);
			card.getImageView().setY(0);

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

	public void setUp() {
		this.endTurn.setOnAction(e -> {
			System.out.println("clicked end turn");
			this.setButtonsInvisible();
			this.removeDraggable();
			playerManager.faceDownFaceDownCards(playerManager.getCurrentPlayer());


			c.send(new TournamentPickCardsClient(playerManager.getCurrentPlayer(), 
					playerManager.getFaceDownCardsAsList(playerManager.getCurrentPlayer()).stream().map(i -> i.getName()).toArray(size -> new String[size])));
			//			playerManager.getPlayerHand(playerManager.getCurrentPlayer()).forEach(g -> {
			//				SequentialTransition x = g.flipDown();
			//				x.play();
			//				System.out.println("rotated: " + g.getName());
			//			});
		});
		this.accept.setOnAction(e -> {
			System.out.println("accepted tournament");
			this.setButtonsInvisible();
			c.send(new TournamentAcceptDeclineClient(playerManager.getCurrentPlayer(), true));
		});
		this.decline.setOnAction(e -> {
			System.out.println("declined tournament");
			this.setButtonsInvisible();
			c.send(new TournamentAcceptDeclineClient(playerManager.getCurrentPlayer(), false));
		});
		this.nextTurn.setOnAction(e -> {
			System.out.println("Clicked next turn");
			c.send(new ContinueGameClient());
		});
		this.setButtonsInvisible();
	}

	public void clearPlayField() {
		this.playField.getChildren().clear();
	}
	
	public void setBackground() {
		//
	}

	public void setButtonsInvisible() {
		this.endTurn.setVisible(false);
		this.accept.setVisible(false);
		this.decline.setVisible(false);
		this.nextTurn.setVisible(false);
	}

	public void showAcceptDecline() {
		this.accept.setVisible(true);
		this.decline.setVisible(true);
	}

	public void showNextTurn() {
		this.nextTurn.setVisible(true);
	}

	public void showEndTurn() {
		this.endTurn.setVisible(true);
	}

	public void removeDraggable() {
		ArrayList<AdventureCard> currHand = playerManager.getPlayerHand(playerManager.getCurrentPlayer());
		for(int i = 0 ; i < currHand.size(); i++) {
			currHand.get(i).setDraggableOff();
		}
	}

	public void addDraggable() {
		ArrayList<AdventureCard> currHand = playerManager.getPlayerHand(playerManager.getCurrentPlayer());
		for(int i = 0 ; i < currHand.size(); i++) {
			currHand.get(i).setDraggableOn();
		}
	}


	//Allow pane to take image views
	public void setOnDragOverOn(Pane p) {
		p.setOnDragOver(e->{
			if(e.getGestureSource() != p && e.getDragboard().hasString()) {
				e.acceptTransferModes(TransferMode.ANY);
			}
		});
	}

	public void showFaceDownFieldCards(int p) {
		playerManager.showFaceDownFieldCards(p);
	}

	public void setPlayerRank(int p, Rank.RANKS r) {
		playerManager.setPlayerRank(p, r);
		String rank = "";
		if( r == Rank.RANKS.SQUIRE) rank = "/R Squire.jpg";
		if( r == Rank.RANKS.KNIGHT) rank = "/R Knight.jpg";
		if( r == Rank.RANKS.CHAMPION) rank = "/R Champion Knight.jpg";
		//		if(rank.equals("KNIGHTOFTHEROUNDTABLE")) r = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
		if(!rank.isEmpty()) {
			try {
				File file = new File(resDir.getPath() + rank);
				Image img = new Image (new FileInputStream(file));
				playerRanks[p].setImage(img);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

