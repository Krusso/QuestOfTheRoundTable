package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.Card.I_AM_IN;
import src.game_logic.Card;
import src.game_logic.Rank;
import src.game_logic.StoryCard;
import src.messages.game.ContinueGameClient;
import src.messages.quest.*;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.messages.tournament.TournamentPickCardsClient;

public class GameBoardController implements Initializable{
	enum STATE {SPONSOR_QUEST,JOIN_QUEST,PICK_STAGES, QUEST_PICK_CARDS, QUEST_BID,
		JOIN_TOURNAMENT, PICK_TOURNAMENT,
		FACE_DOWN_CARDS, UP_QUEST, DISCARDING_CARDS, BID_DISCARD,
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
	@FXML public Slider bidSlider;
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

	private Map<Pane, ArrayList<AdventureCard>> paneDeckMap;
	
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

		bidSlider.setVisible(false);
		//setBackground
		setBackground();
		//map the connection between panes and hands


	}
	////Must call this when you click start game in title screen!
	public void initPlayerManager(int numPlayers) {
		playerManager = new UIPlayerManager(numPlayers);
		for(int i = 0 ; i < numPlayers ; i++) {
			setPlayerRank(i, Rank.RANKS.SQUIRE);
		}
	    paneDeckMap = new HashMap<Pane,ArrayList<AdventureCard>>();
	    for(int i = 0 ; i < playerManager.getNumPlayers(); i++) {
	    	paneDeckMap.put(handPanes[i], playerManager.getPlayerHand(i));
	    	paneDeckMap.put(faceDownPanes[i], playerManager.getFaceDownCardsAsList(i));
	    	paneDeckMap.put(stages[i], stageCards.get(i));
	    }
	}



	public void addStagePaneListener() {
		//Add listeners for the stage panes
		System.out.println("Adding listener for stage");
		for(int i = 0 ; i < stages.length ; i++) {
			Pane p = stages[i];
			final int currentIndex = i;
			// Add mouse event handlers for the target
			p.setOnMouseDragEntered(new EventHandler <MouseDragEvent>()
			{
				public void handle(MouseDragEvent event)
				{
//					System.out.println("Event on Target: mouse dragged");
				}
			});

			p.setOnMouseDragOver(new EventHandler <MouseDragEvent>()
			{
				public void handle(MouseDragEvent event)
				{
//					System.out.println("Event on Target: mouse drag over");
				}
			});

			p.setOnMouseDragReleased(new EventHandler <MouseDragEvent>()
			{
				public void handle(MouseDragEvent event)
				{
					//	            	p.setText(sourceFld.getSelectedText());
//					System.out.println("Event on Target: mouse drag released");
				}
			});

			p.setOnMouseDragExited(new EventHandler <MouseDragEvent>()
			{
				public void handle(MouseDragEvent event)
				{
//					System.out.println("Event on Target: mouse drag exited");
				}
			});

			//			p.setOnDragOver(new EventHandler<DragEvent>() {
			//				@Override
			//				public void handle(DragEvent event) {
			//					System.out.println("hello");
			//					if(event.getGestureSource() != pickStage0 && event.getDragboard().hasString()) {
			//						event.acceptTransferModes(TransferMode.ANY);
			//					}
			//					event.consume();
			//				}
			//			});



			//TODO::
			p.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					//Dragboard should have a string that holds the card ID (integer) and the card image (could probably do without the card image tho);
					Dragboard db = event.getDragboard();
					//Find the id of the card and the index of the card in the player's hand
					int cPlayer = playerManager.getCurrentPlayer();
					int id = Integer.parseInt(db.getString());
					int idx = playerManager.getCardIndexByID(cPlayer, id);
					AdventureCard card = playerManager.getCardByIDInHand(cPlayer, id);
					if(db.hasImage() && isStageValid(stageCards.get(currentIndex), card)) {
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




	/**
	 * Checks if the point is within the bounds of the pane p (helper function for putIntoPane()) and if the pane is visible
	 * @param p : the pane
	 * @param point : x, y coordinate
	 * @return
	 */
	private boolean isInPane(Pane p, Point2D point) {
		Bounds b = p.localToScene(p.getBoundsInLocal());
		System.out.println("is in pane" + p);
		if(b.contains(point) && p.isVisible()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param point : x, y coordinate
	 * @return the pane the mouse is over
	 */
	private Pane mouseOverPane(Point2D point) {		
		//check stage panes
		for(Pane p : stages) {
			Bounds b = p.localToScene(p.getBoundsInLocal());
			if(b.contains(point)) {
				System.out.println("mouse over stages");
				return p;
			}
		}
		//check handPanes
		System.out.println("POINT: " + point);
		for(Pane p : handPanes) {
			Bounds b = p.localToScene(p.getBoundsInLocal());
			System.out.println(b);
			if(b.contains(point)) {
				System.out.println("mouse over Hand Pane");
				return p;
			}
		}
		//check faceDownPanes
//		for(Pane p : faceDownPanes) {
//			Bounds b = p.localToScene(p.getBoundsInLocal());
//			if(b.contains(point)) {
//				System.out.println("mouse over Face Down Panes");
//				return p;
//			}
//		}

		//otherwise return null
		return null;
	}

	/**
	 * Checks if the point is within the bounds of any possible pane
	 * then checks what state the game is and puts the card with the id into that pane
	 * if it valid, otherwise it returns to the original position
	 * @param point: position of the mouse pointer
	 * @param id:	 the id of the card we want to put into the pane
	 */
	public void putIntoPane(Point2D point, int id) {
		int cPlayer = playerManager.getCurrentPlayer();
		int idx = playerManager.getCardIndexByID(cPlayer, id);
		AdventureCard card = playerManager.getCardByIDInHand(cPlayer, id);
		//if it's not in the player hand or face down we look at the stage cards
		if(card == null) {
			for(ArrayList<AdventureCard> l : stageCards) {
				for(AdventureCard c : l) {
					if(c.id == id) {
						card = c;
					}
				}
			}
		}
		ArrayList<AdventureCard> faceDownCards = playerManager.getFaceDownCardsAsList(cPlayer);
		//Check if we are suppose to put cards into the stage
		if(CURRENT_STATE == STATE.PICK_STAGES) {
			//Find if the current point is within one of the stage panes.
			for(int i = 0 ; i < stages.length ; i++) {
				//check if the mouse is over a stage pane and check if it is valid to put it in there
				if(i == 0) System.out.println(isInPane(stages[i], point) + " " + isStageValid(stageCards.get(i), card));
				if(isInPane(stages[i], point) && isStageValid(stageCards.get(i), card)) {
					System.out.println("hello");
					//find which pane the card originated from
					Pane from = findPaneWithCard(card);
					//find which pane we want to place the card into right now
					Pane to = mouseOverPane(point); //TODO error here
					System.out.println("TO PANE: " + to);
					//get the arraylist the card should be removed from
					ArrayList<AdventureCard> toRemove = paneDeckMap.get(from);
					from.getChildren().remove(card.getImageView());
					toRemove.remove(card);

					
					ArrayList<AdventureCard> toAdd = paneDeckMap.get(to);
					to.getChildren().add(card.getImageView());
					toAdd.add(card);
					
					repositionCardsInHand(playerManager.getCurrentPlayer());

					//reposition cards in this pane
					ObservableList<Node> cards = stages[i].getChildren();
					double handPaneHeight = stages[i].getHeight();
					for(int j = 0 ; j < cards.size(); j++) {
						if(cards.get(j) instanceof ImageView) {
							ImageView img = (ImageView) cards.get(j);
							img.setX(0);
							img.setY(handPaneHeight/cards.size() * j);
							System.out.println("img y: " + img.getY());
						}
					}
					return;
				}
			}
		}
		if(CURRENT_STATE == STATE.QUEST_PICK_CARDS) {
			
			//Find if the current point is within the current player's face down pane
			if(isInPane(faceDownPanes[cPlayer], point) && isPickQuestValid(faceDownCards, card)) {
				handPanes[cPlayer].getChildren().remove(idx);
				//add the card image to the faceDownPane
				faceDownPanes[cPlayer].getChildren().add(card.getImageView());

				//add the card to the faceDown hand for the palyer
				faceDownCards.add(card);

				//remove card from the player's hand by matching the id of the card
				playerManager.removeCardFromPlayerHandByID(cPlayer, id);
				repositionCardsInHand(playerManager.getCurrentPlayer());
				

				ObservableList<Node> cards = faceDownPanes[cPlayer].getChildren();
				double paneWidth = faceDownPanes[cPlayer].getWidth();
				for(int j = 0 ; j < cards.size(); j++) {
					if(cards.get(j) instanceof ImageView) {
						ImageView img = (ImageView) cards.get(j);
						img.setX(paneWidth/cards.size() * j);
						img.setY(0);
					}
				}
				return;
			}
		}
		//return to original position if we don't put it into the pane
		card.returnOriginalPosition();
	}
	
	//Helper function
	private Pane findPaneWithCard(AdventureCard c) {
		if(c.childNodeOf == I_AM_IN.HAND_PANE) {return handPanes[playerManager.getCurrentPlayer()];}
		if(c.childNodeOf == I_AM_IN.FACE_DOWN_PANE) {return faceDownPanes[playerManager.getCurrentPlayer()];}
		if(c.childNodeOf == I_AM_IN.STAGE_PANE_0) {return stages[0];}
		if(c.childNodeOf == I_AM_IN.STAGE_PANE_1) {return stages[1];}
		if(c.childNodeOf == I_AM_IN.STAGE_PANE_2) {return stages[2];}
		if(c.childNodeOf == I_AM_IN.STAGE_PANE_3) {return stages[3];}
		if(c.childNodeOf == I_AM_IN.STAGE_PANE_4) {return stages[4];}
		if(c.childNodeOf == I_AM_IN.FACE_UP_PANE) {return faceUpPanes[playerManager.getCurrentPlayer()];}
		return null;
	}
	//Returns the arraylist that holds this card for the current player
	private ArrayList<AdventureCard> findCardList(AdventureCard c) {
		int currPlayer = playerManager.getCurrentPlayer();
//		check player hand
		AdventureCard inHand = playerManager.getCardByIDInHand(currPlayer, c.id);
		if(inHand != null) {
			return playerManager.getPlayerHand(currPlayer);
		}
//		check player facedown
		AdventureCard inFaceDown = playerManager.getCardByIDInFaceDown(currPlayer, c.id);
		if(inFaceDown != null) {
			return playerManager.getFaceDownCardsAsList(currPlayer);
		}
		//check stage cards
		for(ArrayList<AdventureCard> stage: stageCards) {
			for(AdventureCard sCard : stage) {
				if(sCard.id == c.id) {
					return stage;
				}
			}
		}
		return null;
	}
	//Helper Functions
	private void moveCardIntoPane(AdventureCard c, Pane from, Pane to) {
		ObservableList<Node> cards = from.getChildren();
		for(int j = 0 ; j < cards.size(); j++) {
			//check if pane has this card, if it does remove it
			if(cards.get(j) instanceof ImageView && c.getImageView().equals(cards.get(j))) {
				from.getChildren().remove(j);
				//now add the image to the other pane
				to.getChildren().add(c.getImageView());
				return;
			}
		}		
	}

	/* ************************************************
	 *  VALIDATION METHODS FOR PUTTING CARDS INTO PANES
	 **************************************************/
	/**
	 * Checks if we can add the toAdd card into the faceDownCards pile when it is a tournament
	 * @param faceDownCards
	 * @param toAdd
	 * @return
	 */
	private boolean isTournamentCardValid(ArrayList<AdventureCard> faceDownCards, AdventureCard toAdd) {
		if(CURRENT_STATE == STATE.PICK_TOURNAMENT) {
			TYPE cardType = toAdd.getType();
			//if the card we want to add is a weapon we make sure one doesn't exist already
			if(cardType == TYPE.WEAPONS) {
				for(AdventureCard c : faceDownCards) {
					if(c.getName().equals(toAdd.getName())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 *  Checks if we can add the toAdd card into the stageCards pile during Pick stages
	 * @param stageCards
	 * @param toAdd
	 * @return
	 */
	private boolean isStageValid(ArrayList<AdventureCard> stageCards, AdventureCard toAdd) {
		//Make sure the current stage has either 1 foe or 1 test card	
		if(CURRENT_STATE == STATE.PICK_STAGES) {
			TYPE cardType = toAdd.getType();
			//If we want to add a Test card, make sure the stageCards are empty
			if(cardType == TYPE.TESTS) {
				if(!stageCards.isEmpty()) {
					return false;
				}
				//check if there are tests cards in any other of the stages return false
				for(int i = 0 ; i < this.stageCards.size(); i++) {
					for(AdventureCard c : this.stageCards.get(i)) {
						if(c.getType() == TYPE.TESTS) {
							return false;
						}
					}
				}
				return true;
			}
			//If we want to add a Foe card, make sure that there is no other Foe card existing in the stageCards
			else if(cardType == TYPE.FOES) {
				for(AdventureCard c : stageCards) {
					if(c.getType() == cardType || c.getType() == TYPE.TESTS) {
						return false;
					}
				}
				return true;
			}
			//If we want to add a Weapon card, make sure a Test card or the same weapon doesn't exist in the stageCards
			else if(cardType == TYPE.WEAPONS) {
				for(AdventureCard c : stageCards) {
					if(c.getType() == TYPE.TESTS || c.getName().equals(toAdd.getName())) {
						return false;
					}
				}
				return true;
			}
			//For adding cards to stage, they must either be a test/foe/weapon card so return false if none of the above
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks if we can add the toAdd card into the faceDownCards pile when it is a pick quest state
	 * @param faceDownCards
	 * @param toAdd
	 * @return
	 */
	private boolean isPickQuestValid(ArrayList<AdventureCard> faceDownCards, AdventureCard toAdd) {
		if(CURRENT_STATE == STATE.QUEST_PICK_CARDS) {
			TYPE cardType = toAdd.getType();
			//A player cannot play 2 of the same weapon
			if(cardType == TYPE.WEAPONS) {
				for(AdventureCard c : faceDownCards) {
					if(c.getName().equals(toAdd.getName())) {
						return false;
					}
				}
				return true;
			}
			//Check if there's only 1 amour in your facedown deck
			if(cardType == TYPE.AMOUR) {
				for(AdventureCard c : faceDownCards) {
					if(c.getName().equals(toAdd.getName())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/* ************************************************
	 *  END OF VALIDATION METHODS FOR PUTTING CARDS INTO PANES
	 **************************************************/
	
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
			//			p.setOnDragDropped(new EventHandler<DragEvent>() {
			//				@Override
			//				public void handle(DragEvent event) {
			//					//Dragboard should have a string that holds the card ID (integer) and the card image (could probably do without the card image tho);
			//					Dragboard db = event.getDragboard();
			//					//Find the id of the card and the index of the card in the player's hand
			//					int cPlayer = playerManager.getCurrentPlayer();
			//					int id = Integer.parseInt(db.getString());
			//					int idx = playerManager.getCardIndexByID(cPlayer, id);
			//					AdventureCard card = playerManager.getCardByID(cPlayer, id);
			//					if(db.hasImage() && isTournamentCardValid(playerManager.getFaceDownCardsAsList(cPlayer), card)) {
			//						//remove the card from the player hand pane
			//						handPanes[cPlayer].getChildren().remove(idx);
			//						//add the card image to the stage pane
			//						p.getChildren().add(card.getImageView());
			//
			//
			//						AdventureCard c = playerManager.getCardByID(cPlayer, id);
			//						playerManager.playCard(c,cPlayer);
			//						//remove card from the player's hand by matching the id of the card
			//						playerManager.removeCardFromPlayerHandByID(cPlayer, id);
			//
			//						//lastly reposition the hand and reposition the card in this pane
			//						repositionCardsInHand(cPlayer);
			//						//reposition cards in this pane
			//						ObservableList<Node> cards = p.getChildren();
			//						double handPaneWidth = p.getHeight();
			//
			//						for(int i = 0 ; i < cards.size(); i++) {
			//							if(cards.get(i) instanceof ImageView) {
			//								ImageView img = (ImageView) cards.get(i);
			//								img.setX(handPaneWidth/cards.size() * i);
			//								img.setY(0);
			//							}
			//						}
			//					}
			//					event.consume();
			//				}
			//			});
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

	public void setClient(Client c) {
		this.c = c;
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
	public void showDecline() {
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


	public void discardFaceDownCards(int p, String[] cardNames) {
		ArrayList<AdventureCard> fdc = playerManager.getFaceDownCardsAsList(p);
		for(String n : cardNames) {
			for(int i = 0 ; i < fdc.size(); i++) {
				//find cards to discard in fdc  and the image view from the pane
				if(fdc.get(i).getName().equalsIgnoreCase(n)) {
					faceDownPanes[p].getChildren().remove(i);
					fdc.remove(i);
				}
			}
		}
	}
	//discards all cards and returns the string[] name of them
	public String[] discardAllFaceDownCards(int p) {
		ArrayList<AdventureCard> fdc = playerManager.getFaceDownCardsAsList(p);
		faceDownPanes[p].getChildren().clear();
		String[] cardNames = new String[fdc.size()];
		for(int i = 0 ; i < cardNames.length;i++) {
			cardNames[i] = fdc.get(i).getName();
		}
		fdc.clear();
		return cardNames;
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

	public void addShields(int p, int s) {
		playerManager.addShields(p, s);
	}

	public void setUp() {
		/*
		 * END TURN BUTTON LISTENER
		 */
		this.endTurn.setOnAction(e -> {
			System.out.println("clicked end turn");
			this.removeDraggable();
			int currentPlayer = playerManager.getCurrentPlayer();
			if(CURRENT_STATE == STATE.PICK_TOURNAMENT) {
				playerManager.flipFaceDownCards(currentPlayer, false);
				c.send(new TournamentPickCardsClient(currentPlayer, 
						playerManager.getFaceDownCardsAsList(currentPlayer).stream().map(i -> i.getName()).toArray(size -> new String[size])));
			}
			else if(CURRENT_STATE == STATE.PICK_STAGES) {
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
			//TODO::verify cards are minimum number of cards
			else if(CURRENT_STATE == STATE.QUEST_BID) {
				c.send(new QuestBidClient(currentPlayer, (int)bidSlider.getValue()));
			}else if(CURRENT_STATE == STATE.BID_DISCARD) {
				String[] cards = discardAllFaceDownCards(currentPlayer);
				c.send(new QuestDiscardCardsClient(currentPlayer,cards));
			}
		});

		/*
		 * ACCEPT BUTTON LISTENER
		 */
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
		/*
		 * DECLINE BUTTON LISTENER
		 */
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
			}else if(CURRENT_STATE == STATE.QUEST_BID) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined bidding");
				c.send(new QuestBidClient(playerManager.getCurrentPlayer(), -1));
			}
		});
		/*
		 * NEXT TURN BUTTON LISTENER
		 */
		this.nextTurn.setOnAction(e -> {
			System.out.println("Clicked next turn");
			c.send(new ContinueGameClient());
		});
	}
}

