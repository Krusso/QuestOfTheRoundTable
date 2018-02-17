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
	@FXML private Pane questBoard;
	@FXML public Slider bidSlider;
	//The pane that holds the other players' hand

	@FXML public Text p1Shields;
	@FXML public Text p2Shields;
	@FXML public Text p3Shields;
	@FXML public Text p4Shields;
	
	@FXML public ImageView shield1View;
	@FXML public ImageView shield2View;
	@FXML public ImageView shield3View;
	@FXML public ImageView shield4View;

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
	
	@FXML private Rectangle pRec0, pRec1, pRec2, pRec3;

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

	@FXML private ImageView stage0View;
	@FXML private ImageView stage1View;
	@FXML private ImageView stage2View;
	@FXML private ImageView stage3View;
	@FXML private ImageView stage4View;
	private ImageView[] stageViews = new ImageView[5];
	
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

		pRec0.setVisible(false);
		pRec1.setVisible(false);
		pRec2.setVisible(false);
		pRec3.setVisible(false);
		
		stageViews[0] = stage0View;
		stageViews[1] = stage1View;
		stageViews[2] = stage2View;
		stageViews[3] = stage3View;
		stageViews[4] = stage4View;
	}
	
	public void clearHighlight() {
		pRec0.setVisible(false);
		pRec1.setVisible(false);
		pRec2.setVisible(false);
		pRec3.setVisible(false);
	}
	
	public void highlightFaceUp(int p) {
		if(p==0) { pRec0.setVisible(true); }
		if(p==1) { pRec1.setVisible(true); }
		if(p==2) { pRec2.setVisible(true); }
		if(p==3) { pRec3.setVisible(true); }
	}
	
	public void showToast(String text) { toast.setText(text); }
	public void clearToast() { toast.setText(""); }
	
	public void setShields(String[] players, Image shield1, Image shield2, Image shield3, Image shield4) {
		if(!players[0].equals("")) { shield1View.setImage(shield1); p1Shields.setText("0"); }
		if(!players[1].equals("")) { shield2View.setImage(shield2); p2Shields.setText("0"); }
		if(!players[2].equals("")) { shield3View.setImage(shield3); p3Shields.setText("0"); }
		if(!players[3].equals("")) { shield4View.setImage(shield4); p4Shields.setText("0"); }
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
		}
		for(int i = 0 ; i < stages.length ; i ++) {
			paneDeckMap.put(stages[i], stageCards.get(i));
		}
	}



	//TODO::
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
		}
	}

	public void removeStagePaneDragOver() {
		for(Pane p : stages) {
			p.setOnDragOver(null);
			p.setOnDragDropped(null);
		}
	}

	/* ********************************
	 *	REPOSITIONING FUNCTIONS 
	 *******************************/
	public void repositionStageCards() {
		//reposition all the cards in the stages
		for(int i = 0 ; i < stages.length; i++) {
			ObservableList<Node> cards = stages[i].getChildren();
			double handPaneHeight = stages[i].getHeight();
			for(int j = 0 ; j < cards.size(); j++) {
				if(cards.get(j) instanceof ImageView) {
					ImageView img = (ImageView) cards.get(j);
					img.setX(0);
					img.setY(handPaneHeight/cards.size() * j);
				}
			}
		}
	}

	public void repositionFaceDownCards(int p) {
		ObservableList<Node> cards = faceDownPanes[p].getChildren();
		double handPaneWidth = faceDownPanes[p].getWidth();
		for(int j = 0 ; j < cards.size(); j++) {
			if(cards.get(j) instanceof ImageView) {
				ImageView img = (ImageView) cards.get(j);
				System.out.println("Before X: " + img.getX() +"Y: " + img.getY());
				img.setX(handPaneWidth/cards.size() * j);
				img.setY(0);
				System.out.println("After X: " + img.getX() +"Y: " + img.getY());
			}
		}

	}

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


	/* ***************************************************
	 * HELPER FUNCTION FOR MOVING IMAGE VIEWS TO ANOTHER PANE
	 ****************************************************/
	/**
	 * Checks if the point is within the bounds of the pane p (helper function for putIntoPane()) and if the pane is visible
	 * @param p : the pane
	 * @param point : x, y coordinate
	 * @return
	 */
	private boolean isInPane(Pane p, Point2D point) {
		Bounds b = p.localToScene(p.getBoundsInLocal());

		System.out.println("bounds"+b);
		System.out.println("point" + p);
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
		//check handPanes
		Bounds b = handPanes[playerManager.getCurrentPlayer()].localToScene(handPanes[playerManager.getCurrentPlayer()].getBoundsInLocal());
		if(b.contains(point)) {
			return handPanes[playerManager.getCurrentPlayer()];
		}
		//check stage panes
		for(Pane p : stages) {
			if(p.localToScene(p.getBoundsInLocal()).contains(point)) {
				return p;
			}
		}

		//check faceDownPanes
		for(Pane p : faceDownPanes) {
			if(p.localToScene(p.getBoundsInLocal()).contains(point)) {
				System.out.println("mouse over Face Down Panes");
				return p;
			}
		}
		System.out.println("ERROR WE DIDN'T FIND ANY PANES OVER THE MOUSE!");
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
		ArrayList<AdventureCard> faceDownCards = playerManager.getFaceDownCardsAsList(cPlayer);
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
		//if it's still null, check the face down panes
		if(card == null) {
			card = playerManager.getCardByIDInFaceDown(cPlayer, id);
		}
		//Check if we are suppose to put cards into the stage
		if(CURRENT_STATE == STATE.PICK_STAGES) {
			//Find if the current point is within one of the stage panes.
			for(int i = 0 ; i < stages.length ; i++) {
				//check if the mouse is over a stage pane and check if it is valid to put it in there
				System.out.println(isInPane(stages[i], point)+ " " + isStageValid(stageCards.get(i), card) );
				if(isInPane(stages[i], point) && isStageValid(stageCards.get(i), card) || 
						isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer])) {
					doPutCardIntoPane(point, card);
					return;
				}
			}
		}
		//rules for puttings cards into facedown pane are same for picking quest/tournament cards
		if(CURRENT_STATE == STATE.QUEST_PICK_CARDS || CURRENT_STATE == STATE.PICK_TOURNAMENT) {
			System.out.println(isInPane(faceDownPanes[cPlayer], point) );
			System.out.println( isInPane(faceDownPanes[cPlayer], point)+" "+isPickQuestValid(faceDownCards, card));

			if(isInPane(faceDownPanes[cPlayer], point) && isPickQuestValid(faceDownCards, card) ||
					isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer])) {
				doPutCardIntoPane(point, card);
			}
		}
		//return to original position if we don't put it into the pane
		card.returnOriginalPosition();
	}
	private void doPutCardIntoPane(Point2D point, AdventureCard card ) {
		Pane from = card.childOf;
		ArrayList<AdventureCard> toRemove = paneDeckMap.get(from);
		from.getChildren().remove(card.getImageView());
		toRemove.remove(card);
		//find which pane we want to place the card into right now
		Pane to = mouseOverPane(point);
		System.out.println("to:" + to);

		System.out.println("");
		ArrayList<AdventureCard> toAdd = paneDeckMap.get(to);
		System.out.println("toAdd" + toAdd);
		to.getChildren().add(card.getImageView());
		toAdd.add(card);

		System.out.println("toAdd" + toAdd);
		card.childOf = to;
		
		repositionCardsInHand(playerManager.getCurrentPlayer());
		repositionStageCards();
		repositionFaceDownCards(playerManager.getCurrentPlayer());

		//reset the original position of this card cards
		card.setOriginalPosition(card.getImageView().getX(), card.getImageView().getY());
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
						//if they are the same card we can move it around
						if(c.id == toAdd.id) {
							return true;
						}
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
		if(CURRENT_STATE == STATE.QUEST_PICK_CARDS || CURRENT_STATE == STATE.PICK_TOURNAMENT) {
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

	/**
	 * Checks if the stages satisfy the current quest card (should be called before player ends turn)
	 * We only check if the stages are empty or contains either a test/foe not since when players play cards into the pane, a validation already occurs
	 * @return boolean
	 */
	private boolean areQuestStagesValid() {
		//check visible stages (visible stages must have cards in them)
		for(int i = 0 ; i < stages.length ; i++) {
			if(stages[i].isVisible()) {
				//must have cards in all visible panes
				if(stageCards.get(i).isEmpty()) {
					return false;
				}
				boolean hasTestOrFoe = false;
				for(AdventureCard c: stageCards.get(i)) {
					if(c.getType() == TYPE.TESTS || c.getType() == TYPE.FOES) {	
						hasTestOrFoe = true;
						break;
					}
				}
				if(hasTestOrFoe == false) {
					return false;
				}
			}
		}
		return true;
	}
	/* ************************************************
	 *  END OF VALIDATION METHODS FOR PUTTING CARDS INTO PANES
	 **************************************************/

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
		c.childOf = handPanes[playerNum];
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
		
		//readjust the player pane's scale
		for(int i = 0 ; i < handPanes.length; i++) {
			if(i == playerNum) {
				playerPanes[i].setScaleX(1);
				playerPanes[i].setScaleY(1);
			}else {
				playerPanes[i].setScaleX(0.6);
				playerPanes[i].setScaleY(0.6);
			}
			System.out.println("scaleX:" + handPanes[i].getScaleX());
			System.out.println("scaleY:" + handPanes[i].getScaleY());
		}
	}

	//This rotates the player's pane clockwise 90 degrees
	private void rotatePlayerPosition() {
		double posX3 = playerPanes[3].getLayoutX();
		double posY3 = playerPanes[3].getLayoutY();	
		double rotate3 = playerPanes[3].getRotate();
		
		for(int i = playerPanes.length-1 ; i >= 0 ; i--) {
			int pos = i-1 < 0 ? 3 : i-1;
			if(i == 0) {
				playerPanes[i].relocate(posX3, posY3);
				playerPanes[i].setRotate(rotate3);
			}else {
				playerPanes[i].relocate(playerPanes[pos].getLayoutX(), playerPanes[pos].getLayoutY());
				playerPanes[i].setRotate(playerPanes[pos].getRotate());;
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
	
	public void setQuestStageBanners(int num) {
		try {
			File f = new File(resDir + "/Red_Banner_Clipart_Picture.png");
			Image banner = new Image(new FileInputStream(f));
			for(int i=0; i<num; i++) { stageViews[i].setImage(banner); }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				System.out.println(p);
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
		if(p==1) p1Shields.setText(getShields(p)+"");
		if(p==2) p2Shields.setText(getShields(p)+"");
		if(p==3) p3Shields.setText(getShields(p)+"");
		if(p==4) p4Shields.setText(getShields(p)+"");
	}
	
	public int getShields(int p) {
		return playerManager.getShields(p);
	}

	public void setUp() {
		/*
		 * END TURN BUTTON LISTENER
		 */
		this.endTurn.setOnAction(e -> {
			int currentPlayer = playerManager.getCurrentPlayer();
			if(CURRENT_STATE == STATE.PICK_TOURNAMENT) {
				playerManager.flipFaceDownCards(currentPlayer, false);
				c.send(new TournamentPickCardsClient(currentPlayer, 
						playerManager.getFaceDownCardsAsList(currentPlayer).stream().map(i -> i.getName()).toArray(size -> new String[size])));
			}
			else if(CURRENT_STATE == STATE.PICK_STAGES) {
				//TODO::Handle PICK_STAGES make sure player plays a card
				if(areQuestStagesValid()) {
					for(int i = 0 ; i < stageCards.size(); i++) {
						if(!stageCards.get(i).isEmpty()) {
							String[] currentStageCards = new String[stageCards.get(i).size()];
							for(int j = 0 ; j < stageCards.get(i).size(); j++) {
								currentStageCards[j] = stageCards.get(i).get(j).getName();
							}
							c.send(new QuestPickStagesClient(currentPlayer, currentStageCards, i));
						}
					}
				}else {
					//TODO:: display some message saying quest stages are not valid
					System.out.println("Quest stages are not valid");
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

