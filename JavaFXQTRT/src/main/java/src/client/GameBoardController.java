package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
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
import src.messages.quest.*;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.messages.tournament.TournamentPickCardsClient;

public class GameBoardController implements Initializable{
	enum STATE {SPONSOR_QUEST,JOIN_QUEST,PICK_STAGES, QUEST_PICK_CARDS, 
		JOIN_TOURNAMENT, 
		FACE_DOWN_CARDS, UP_QUEST,
		NONE}

	public STATE CURRENT_STATE = STATE.NONE;
	public int numStages = 0;
	public int currentStage = 0;

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

	//The panes that govern the player's facedown cards
	@FXML private Pane playerFaceDown0;
	@FXML private Pane playerFaceDown1;
	@FXML private Pane playerFaceDown2;
	@FXML private Pane playerFaceDown3;
	private Pane[] faceDownPanes = new Pane[4];

	//The panes that govern the player's faceup cards
	@FXML private Pane playerFaceUp0;
	@FXML private Pane playerFaceUp1;
	@FXML private Pane playerFaceUp2;
	@FXML private Pane playerFaceUp3;
	private Pane[] faceUpPanes = new Pane[4];

	@FXML private ImageView playerRank0;
	@FXML private ImageView playerRank1;
	@FXML private ImageView playerRank2;
	@FXML private ImageView playerRank3;
	private ImageView[] playerRanks = new ImageView[4];

	@FXML public Text toast;

	/*Panes for picking stages (maximum number of stages is 5)*/
	@FXML private Pane pickStage0;
	@FXML private Pane pickStage1;
	@FXML private Pane pickStage2;
	@FXML private Pane pickStage3;
	@FXML private Pane pickStage4;
	private Pane[] stages = new Pane[5];
	private ArrayList<ArrayList<AdventureCard>> stageCards = new ArrayList<>();

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

		stages[0] = pickStage0;
		stages[1] = pickStage1;
		stages[2] = pickStage2;
		stages[3] = pickStage3;
		stages[4] = pickStage4;
		for(int i = 0 ; i < 5 ; i++) {
			stageCards.add(new ArrayList<AdventureCard>());
		}

		//setBackground
		setBackground();


	}


	public void addStagePaneListener() {
		//Add listeners for the stage panes
		for(int i = 0 ; i < stages.length ; i++) {
			Pane p = stages[i];
			final int currentIndex = i;
			p.setOnDragOver(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					if(event.getGestureSource() != pickStage0 && event.getDragboard().hasString()) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
					}
					event.consume();
				}
			});
			//TODO::
			p.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					//Dragboard should have a string that holds the card ID (integer) and the card image (could probably do without the card image tho);
					Dragboard db = event.getDragboard();
					if(db.hasImage()) {
						//Find the id of the card and the index of the card in the player's hand
						int cPlayer = playerManager.getCurrentPlayer();
						int id = Integer.parseInt(db.getString());
						int idx = playerManager.getCardIndexByID(cPlayer, id);
						AdventureCard card = playerManager.getCardByID(cPlayer, id);
						//remove the card from the player hand pane
						handPanes[cPlayer].getChildren().remove(idx);
						//add the card image to the stage pane
						p.getChildren().add(card.getImageView());


						//add the card name to the stage
						stageCards.get(currentIndex).add(card);
						//remove card from the player's hand by matching the id of the card
						playerManager.removeCardFromPlayerHandByID(cPlayer, id);

						//lastly reposition the hand and reposition the card in this pane
						repositionCardsInHand(cPlayer);
						//reposition cards in this pane
						ObservableList<Node> cards = p.getChildren();
						double handPaneHeight = p.getHeight();

						for(int i = 0 ; i < cards.size(); i++) {
							if(cards.get(i) instanceof ImageView) {
								ImageView img = (ImageView) cards.get(i);
								img.setX(0);
								img.setY(handPaneHeight/cards.size() * i);
							}
						}
					}
					event.consume();
				}
			});
		}
	}

	public void removeStagePaneDragOver() {
		for(Pane p : stages) {
			p.setOnDragOver(null);
			p.setOnDragDropped(null);
		}
	}

	public void addFaceDownPaneDragOver() {
		for(int i = 0 ; i < faceDownPanes.length ; i++) {
			Pane p = faceDownPanes[i];
			final int currentIndex = i;
			p.setOnDragOver(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					if(event.getGestureSource() != pickStage0 && event.getDragboard().hasString()) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
					}
					event.consume();
				}
			});
			//TODO::
			p.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					//Dragboard should have a string that holds the card ID (integer) and the card image (could probably do without the card image tho);
					Dragboard db = event.getDragboard();
					if(db.hasImage()) {
						//Find the id of the card and the index of the card in the player's hand
						int cPlayer = playerManager.getCurrentPlayer();
						int id = Integer.parseInt(db.getString());
						int idx = playerManager.getCardIndexByID(cPlayer, id);
						AdventureCard card = playerManager.getCardByID(cPlayer, id);
						//remove the card from the player hand pane
						handPanes[cPlayer].getChildren().remove(idx);
						//add the card image to the stage pane
						p.getChildren().add(card.getImageView());


						AdventureCard c = playerManager.getCardByID(cPlayer, id);
						playerManager.playCard(c,cPlayer);
						//remove card from the player's hand by matching the id of the card
						playerManager.removeCardFromPlayerHandByID(cPlayer, id);

						//lastly reposition the hand and reposition the card in this pane
						repositionCardsInHand(cPlayer);
						//reposition cards in this pane
						ObservableList<Node> cards = p.getChildren();
						double handPaneWidth = p.getHeight();

						for(int i = 0 ; i < cards.size(); i++) {
							if(cards.get(i) instanceof ImageView) {
								ImageView img = (ImageView) cards.get(i);
								img.setX(handPaneWidth/cards.size() * i);
								img.setY(0);
							}
						}
					}
					event.consume();
				}
			});
		}
	}
	public void removeFaceDownPaneDragOver() {
		for(Pane p: faceDownPanes) {
			//TODO:: remove panes listener
		}
	}
	//important for making sure players won't be dragging cards into the wrong stage panes
	public void setPickStageOn(int numStage) {
		for(int i = 0 ; i < stages.length ; i++) {
			if(i < numStage) {
				stages[i].setVisible(true);
			}else {
				stages[i].setVisible(false);
			}
		}
		//TODO::Provide visual feeed back by implementing setOnDragEntered
	}

	////Must call this when you click start game in title screen!
	public void initPlayerManager(int numPlayers) {
		playerManager = new UIPlayerManager(numPlayers);
		for(int i = 0 ; i < numPlayers ; i++) {
			setPlayerRank(i, Rank.RANKS.SQUIRE);;
		}
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

		for(int i = 0 ; i < playerManager.getNumPlayers();i++) {
			if(currentPlayer == i) {
				playerManager.showPlayerHand(currentPlayer);
			}else {
				playerManager.faceDownPlayerHand(i);
			}
		}
		playerManager.setCurrentPlayer(playerNum);

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
		ObservableList<Node> cards = handPanes[pNum].getChildren();
		double handPaneWidth = handPanes[pNum].getWidth();

		for(int i = 0 ; i < cards.size(); i++) {
			if(cards.get(i) instanceof ImageView) {
				ImageView img = (ImageView) cards.get(i);
				img.setX(handPaneWidth/cards.size() * i);
				img.setY(0);
			}
		}
	}

	public void setPlayerTurn(int p) {
		setPlayerPerspectiveTo(p);
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

		//TODO:: might make another state for choosing cards in tournament
		if(CURRENT_STATE == STATE.JOIN_TOURNAMENT) {
			//the bounds to play will be outside of the player's hand
			Bounds boundsPlay = p.localToScene(p.getBoundsInLocal());
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
		} 
		//If state is currently pick stages, we allow player to drag and drop cards into the 5 stage panes
		if(CURRENT_STATE == STATE.PICK_STAGES) {

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
			this.removeDraggable();
			int currentPlayer = playerManager.getCurrentPlayer();
			if(CURRENT_STATE == STATE.JOIN_TOURNAMENT) {
				playerManager.flipFaceDownCards(currentPlayer, false);
				c.send(new TournamentPickCardsClient(currentPlayer, 
						playerManager.getFaceDownCardsAsList(currentPlayer).stream().map(i -> i.getName()).toArray(size -> new String[size])));
			}else if(CURRENT_STATE == STATE.PICK_STAGES) {
				//TODO::Handle PICK_STAGES make sure player plays a card
				for(int i = 0 ; i < stageCards.size(); i++) {
					if(!stageCards.get(i).isEmpty()) {
						String[] currentStageCards = new String[stageCards.get(i).size()];
						for(int j = 0 ; j < stageCards.get(i).size(); j++) {
							currentStageCards[j] = stageCards.get(i).get(j).getName();
						}
						c.send(new QuestPickStagesClient(currentPlayer, currentStageCards, i));
					}
				}
			}else if(CURRENT_STATE == STATE.QUEST_PICK_CARDS) {
				//send cards 
				ArrayList<AdventureCard> faceDownCards = playerManager.getFaceDownCardsAsList(currentPlayer);
				String[] cards = new String[faceDownCards.size()];
				for(int i = 0 ; i < cards.length ; i++) {
					cards[i] = faceDownCards.get(i).getName();
				}
				c.send(new QuestPickCardsClient(currentPlayer, cards));
			}
		});
		this.accept.setOnAction(e -> {
			System.out.println("accepted story");
			if(CURRENT_STATE == STATE.JOIN_TOURNAMENT) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " accepted tournament");
				c.send(new TournamentAcceptDeclineClient(playerManager.getCurrentPlayer(), true));
			}else if(CURRENT_STATE == STATE.SPONSOR_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " accepted quest sponsoring");
				c.send(new QuestSponsorClient(playerManager.getCurrentPlayer(), true));
			}else if(CURRENT_STATE == STATE.JOIN_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " joined quest");
				c.send(new QuestJoinClient(playerManager.getCurrentPlayer(), true));
			}
		});
		this.decline.setOnAction(e -> {
			System.out.println("declined story");
			if(CURRENT_STATE == STATE.JOIN_TOURNAMENT) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined tournament");
				c.send(new TournamentAcceptDeclineClient(playerManager.getCurrentPlayer(), false));
			}else if(CURRENT_STATE == STATE.SPONSOR_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined quest sponsoring");
				c.send(new QuestSponsorClient(playerManager.getCurrentPlayer(), false));
			}else if(CURRENT_STATE == STATE.JOIN_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined quest");
				c.send(new QuestJoinClient(playerManager.getCurrentPlayer(), false));
			}
		});
		this.nextTurn.setOnAction(e -> {
			System.out.println("Clicked next turn");
			c.send(new ContinueGameClient());
		});
	}
	
	//TODO:: BG image isn't completely scaled correctly not sure why
	public void setBackground() {
		try {
			File f = new File(resDir + "/gameboardbg3.jpg");
			Image bg;
			bg = new Image(new FileInputStream(f));
			ImageView bgView = new ImageView();
			bgView.setImage(bg);
			bgView.setFitHeight(1920);
			bgView.setFitHeight(1080);
			background.getChildren().add(bgView);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setButtonsInvisible() {
		this.endTurn.setVisible(false);
		this.accept.setVisible(false);
		this.decline.setVisible(false);
		this.nextTurn.setVisible(false);
		System.out.println("Called");
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
	
	public void flipFaceDownPane(int p, boolean isShow) {
		playerManager.flipFaceDownCards(p, isShow);
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


	public void discardFaceDownCards() {
		playerManager.getFaceDownCardsAsList(playerManager.getCurrentPlayer()).clear();
	}
	
	public void flipStageCards(int stageNum, boolean isShow) {
		ArrayList<AdventureCard> cards = stageCards.get(stageNum);
		for(int i = 0 ; i < cards.size(); i++) {
			if(isShow == true) {
				cards.get(i).faceUp();
			}else {
				cards.get(i).faceDown();
			}
		}
	}
}

