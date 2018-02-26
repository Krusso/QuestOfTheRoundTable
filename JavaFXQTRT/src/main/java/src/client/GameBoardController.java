package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.effect.Glow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.QuestCard;
import src.game_logic.Rank;
import src.game_logic.StoryCard;
import src.messages.events.EventDiscardCardsClient;
import src.messages.game.CalculatePlayerClient;
import src.messages.game.CalculateStageClient;
import src.messages.game.ContinueGameClient;
import src.messages.game.MordredClient;
import src.messages.hand.HandFullClient;
import src.messages.quest.QuestBidClient;
import src.messages.quest.QuestDiscardCardsClient;
import src.messages.quest.QuestJoinClient;
import src.messages.quest.QuestPickCardsClient;
import src.messages.quest.QuestPickStagesClient;
import src.messages.quest.QuestSponsorClient;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.messages.tournament.TournamentPickCardsClient;

public class GameBoardController implements Initializable{
	final static Logger logger = LogManager.getLogger(GameBoardController.class);
	public enum GAME_STATE {SPONSOR_QUEST,JOIN_QUEST,PICK_STAGES, QUEST_PICK_CARDS, QUEST_BID,
		JOIN_TOURNAMENT, PICK_TOURNAMENT,
		FACE_DOWN_CARDS, UP_QUEST, DISCARDING_CARDS, BID_DISCARD, CHILLING,
		GAMEOVER,
		NONE, EVENT_DISCARD}

	public GAME_STATE CURRENT_STATE = GAME_STATE.NONE;
	public int numStages = 0;
	public int currentStage = 0;

	public Client c;
	public UIPlayerManager playerManager;
	private File resDir = new File("src/main/resources/");

	public ChoiceDialog<String> merlinDialog; 
	public ChoiceDialog<String> mordredDialog; 
	
	@FXML private Pane playField;
	@FXML private VBox storyContainer;
	@FXML private Pane storyCardContainer;
	@FXML public Button endTurn;
	@FXML public Button accept;
	@FXML public Button decline;
	@FXML public Button nextTurn;
	@FXML public Button discard;
	
	@FXML public Button useMerlin;
	@FXML public Button useMordred;
	
	@FXML private Text playerNumber;
	@FXML private Pane background;
	@FXML private Pane questBoard;
	@FXML public Slider bidSlider;
	@FXML public Button startTurn;
	//The pane that holds the other players' hand

	@FXML public Text p1Shields;
	@FXML public Text p2Shields;
	@FXML public Text p3Shields;
	@FXML public Text p4Shields;

	@FXML public ImageView shield1View;
	@FXML public ImageView shield2View;
	@FXML public ImageView shield3View;
	@FXML public ImageView shield4View;
	public ImageView[] shieldViews= new ImageView[4];

	//These panes are for hold each player's respective items, e.g hand, face up card, face down cards etc
	//When rotating, we only rotate these panes.
	@FXML private Pane playerPane0;
	@FXML private Pane playerPane1;
	@FXML private Pane playerPane2;
	@FXML private Pane playerPane3;
	private Pane playerPanes[] = new Pane[4];

	@FXML public Pane playerhand0;
	@FXML public Pane playerHand1;
	@FXML public Pane playerHand2;
	@FXML public Pane playerHand3;
	@FXML public Pane[] handPanes = new Pane[4];

	@FXML private Rectangle pRec0, pRec1, pRec2, pRec3;

	//The panes that govern the player's facedown cards
	@FXML public Pane playerFaceDown0;
	@FXML public Pane playerFaceDown1;
	@FXML public Pane playerFaceDown2;
	@FXML public Pane playerFaceDown3;
	public Pane[] faceDownPanes = new Pane[4];

	//The panes that govern the player's faceup cards
	@FXML public Pane playerFaceUp0;
	@FXML public Pane playerFaceUp1;
	@FXML public Pane playerFaceUp2;
	@FXML public Pane playerFaceUp3;
	public Pane[] faceUpPanes = new Pane[4];

	@FXML private ImageView playerRank0;
	@FXML private ImageView playerRank1;
	@FXML private ImageView playerRank2;
	@FXML private ImageView playerRank3;
	public ImageView[] playerRanks = new ImageView[4];

	@FXML public Text toast;

	@FXML private ImageView stage0View;
	@FXML private ImageView stage1View;
	@FXML private ImageView stage2View;
	@FXML private ImageView stage3View;
	@FXML private ImageView stage4View;
	public ImageView[] stageViews = new ImageView[5];

	/*Panes for picking stages (maximum number of stages is 5)*/
	@FXML private Pane pickStage0;
	@FXML private Pane pickStage1;
	@FXML private Pane pickStage2;
	@FXML private Pane pickStage3;
	@FXML private Pane pickStage4;
	public Pane[] stages = new Pane[5];
	public ArrayList<ArrayList<AdventureCard>> stageCards = new ArrayList<>();


	@FXML public Text bpTextStage0;
	@FXML public Text bpTextStage1;
	@FXML public Text bpTextStage2;
	@FXML public Text bpTextStage3;
	@FXML public Text bpTextStage4;
	public Text[] bpTexts = new Text[5];

	private Map<Pane, ArrayList<AdventureCard>> paneDeckMap;

	@FXML public Text currBP;
	public int toDiscard = 0;

	@FXML public Pane discardPane;
	@FXML ImageView discardView;
	private ArrayList<AdventureCard> discardPile = new ArrayList<>();
	public QuestCard questCard;
	public TYPE type;
	
	public boolean[] joinTournament = {false, false, false, false};

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		bpTexts[0] = bpTextStage0;
		bpTexts[1] = bpTextStage1;
		bpTexts[2] = bpTextStage2;
		bpTexts[3] = bpTextStage3;
		bpTexts[4] = bpTextStage4;

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
		shieldViews[0] = shield1View;
		shieldViews[1] = shield2View;
		shieldViews[2] = shield3View;
		shieldViews[3] = shield4View;

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

		setDiscardImage();
		hideDiscardPane();
	}
	
	public void setDiscardImage() {
		try {
			File f = new File(resDir + "/discardTray.png");
			Image discardImage = new Image(new FileInputStream(f));
			discardView.setImage(discardImage); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void hideDiscardPane() {
		discardView.setVisible(false);
	}
	
	public void showDiscardPane() {
		discardView.setVisible(true);
	}

	public void setGlow(int p) {
		DropShadow noticeableGlow = new DropShadow();
		noticeableGlow.setColor(Color.web("#42f4f1"));
		noticeableGlow.setOffsetX(0f);
		noticeableGlow.setOffsetY(0f);
		noticeableGlow.setHeight(170);
		playerRanks[p].setEffect(noticeableGlow);
	}

	public void clearHighlight() {
		logger.info("Clearing highlight");
		pRec0.setVisible(false);
		pRec1.setVisible(false);
		pRec2.setVisible(false);
		pRec3.setVisible(false);
	}

	public void highlightFaceUp(int p) {
		logger.info("Highlight player: " + p);
		if(p==0) { pRec0.setVisible(true); }
		if(p==1) { pRec1.setVisible(true); }
		if(p==2) { pRec2.setVisible(true); }
		if(p==3) { pRec3.setVisible(true); }
	}

	public void showToast(String text) {
		toast.setText(text);
		logger.info("Setting toast to: " + text);
	}
	public void clearToast() { toast.setText(""); }

	public void setShields(String[] players, Image shield1, Image shield2, Image shield3, Image shield4) {
		if(!players[0].equals("")) { shield1View.setImage(shield1); p1Shields.setText("0"); }
		if(!players[1].equals("")) { shield2View.setImage(shield2); p2Shields.setText("0"); }
		if(!players[2].equals("")) { shield3View.setImage(shield3); p3Shields.setText("0"); }
		if(!players[3].equals("")) { shield4View.setImage(shield4); p4Shields.setText("0"); }
	}

	//Must call this when you click start game in title screen!
	public void initPlayerManager(int numPlayers, List<Integer> list, List<Integer> list2) {
		playerManager = new UIPlayerManager(numPlayers);
		playerManager.setAI(list, list2);
		for(int i = 0 ; i < numPlayers ; i++) {
			setPlayerRank(i, Rank.RANKS.SQUIRE);
		}
		paneDeckMap = new HashMap<Pane,ArrayList<AdventureCard>>();
		for(int i = 0 ; i < playerManager.getNumPlayers(); i++) {
			paneDeckMap.put(handPanes[i], playerManager.getPlayerHand(i));
			paneDeckMap.put(faceDownPanes[i], playerManager.getFaceDownCardsAsList(i));
			paneDeckMap.put(faceUpPanes[i], playerManager.getFaceUpCardsAsList(i));
		}
		for(int i = 0 ; i < stages.length ; i ++) {
			paneDeckMap.put(stages[i], stageCards.get(i));
		}
		paneDeckMap.put(discardPane, discardPile );
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
	public void repositionStageCards(int stageNum) {
		//reposition all the cards in the stages
		ObservableList<Node> cards = stages[stageNum].getChildren();
		double handPaneHeight = stages[stageNum].getHeight();
		for(int j = 0 ; j < cards.size(); j++) {
			if(cards.get(j) instanceof ImageView) {
				ImageView img = (ImageView) cards.get(j);
				img.setX(0);
				img.setY(handPaneHeight/cards.size() * j);
			}
		}
	}

	public void stackStageCards() {
		//reposition all the cards in the stages
		for(int i = 0 ; i < stages.length; i++) {
			ObservableList<Node> cards = stages[i].getChildren();
			for(int j = 0 ; j < cards.size(); j++) {
				if(cards.get(j) instanceof ImageView) {
					ImageView img = (ImageView) cards.get(j);
					img.setX(0);
					img.setY(0);
				}
			}
		}
	}


	public void repositionFaceDownCards(int p) {
		reposition(faceDownPanes[p].getChildren(), faceDownPanes[p].getWidth());
	}

	private void reposition(ObservableList<Node> cards, double handPaneWidth) {
		for(int i = 0 ; i < cards.size(); i++) {
			if(cards.get(i) instanceof ImageView) {
				ImageView img = (ImageView) cards.get(i);
				img.setX(handPaneWidth/cards.size() * i);
				img.setY(0);
			}
		}
	}

	private void repositionCardsInHand(int pNum) {
		reposition(handPanes[pNum].getChildren(), handPanes[pNum].getWidth());
	}

	private void repositionFaceUpCards(int pNum) {
		reposition(faceUpPanes[pNum].getChildren(),  faceUpPanes[pNum].getWidth());
	}

	private void repositionDiscardPile() {
		ObservableList<Node> cards = discardPane.getChildren();
		double height = discardPane.getHeight();

		for(int i = 0 ; i < cards.size(); i++) {
			if(cards.get(i) instanceof ImageView) {
				ImageView img = (ImageView) cards.get(i);
				img.setX(0);
				img.setY(height/cards.size() * i);
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
			System.out.println("moused over handpanes");
			return handPanes[playerManager.getCurrentPlayer()];
		}
		//check stage panes
		for(Pane p : stages) {
			if(p.localToScene(p.getBoundsInLocal()).contains(point)) {
				System.out.println("Moused over stage panes");
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
		//check discard pane
		if(discardPane.localToScene(discardPane.getBoundsInLocal()).contains(point)) {
			System.out.println("mouse over discard pane");
			return discardPane;
		}

		for(Pane p : faceUpPanes) {
			if(p.localToScene(p.getBoundsInLocal()).contains(point)) {
				System.out.println("mouse over Face up Pane");
				return p;
			}
		}

		System.out.println("ERROR WE DIDN'T FIND ANY PANES OVER THE MOUSE!");
		//if we wish to add drag and drop for new panes, make sure we add it below here:

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
		ArrayList<AdventureCard> faceDownCards = playerManager.getFaceDownCardsAsList(cPlayer);

		//Find where this card is on the game board (it must be either in player hand, face down pane, discard pile or stage)
		AdventureCard card = playerManager.getCardByIDInHand(cPlayer, id);
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
		//then check discard pile
		if(card == null) {
			for(AdventureCard c : discardPile) {
				if(c.id == id) {
					card = c;
				}
			}
		}
		//then check faceuppane
		if(card == null) {
			for(AdventureCard c : playerManager.getFaceUpCardsAsList(cPlayer)) {
				if(c.id == id) {
					card = c;
				}
			}
		}
		System.out.println("Current State: " + CURRENT_STATE);
		//Check if we are suppose to put cards into the stage
		if(CURRENT_STATE == GAME_STATE.PICK_STAGES) {
			//putting card from stage pane to hand
			if(isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer])) {
				doPutCardIntoPane(point, card);
				for(int i = 0; i < stages.length; i++) {
					if(stages[i].isVisible()) {
						c.send(new CalculateStageClient(this.playerManager.getCurrentPlayer(),stageCards.get(i).stream().map(j -> j.getName()).toArray(String[]::new), i));	
					}
				}
				return;
			}
			//Find if the current point is within one of the stage panes.
			for(int i = 0 ; i < stages.length ; i++) {
				//we allow player to put the cards into the stage panes, hand panes or if it is a merlin card, we allow the player to put
				//it into the face up pane if they choose to use its power
				if(isInPane(stages[i], point) && isStageValid(stageCards.get(i), card)) {
					doPutCardIntoPane(point, card);
					for(int j = 0; j < stages.length; j++) {
						if(stages[j].isVisible()) {
							c.send(new CalculateStageClient(this.playerManager.getCurrentPlayer(),stageCards.get(j).stream().map(k -> k.getName()).toArray(String[]::new), j));	
						}
					}
				}
			}
		}

		if(CURRENT_STATE == GAME_STATE.JOIN_QUEST) {
			if(	card.isMerlin() && isInPane(faceUpPanes[cPlayer], point)
					&& !card.childOf.equals(faceUpPanes[cPlayer])) {
				doPutCardIntoPane(point, card);
			}
		}

		//rules for puttings cards into facedown pane are same for picking quest/tournament cards

		if(CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS) {
			if((isInPane(faceDownPanes[cPlayer], point) && 
					isPickQuestValid(faceDownCards, card) && 
					(card.getType() != TYPE.AMOUR || isPlayingAmourValid(playerManager.getFaceDownCardsAsList(cPlayer), playerManager.getFaceUpCardsAsList(cPlayer), card)) ||
					isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer]) ||
					card.isMerlin() && isInPane(faceUpPanes[cPlayer], point) )
					&& !card.childOf.equals(faceUpPanes[cPlayer])) { //once card is in faceuppane, we do not allow player to move it to another pane
				doPutCardIntoPane(point, card);
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.addAll(playerManager.players[playerManager.getCurrentPlayer()].getFaceUp().getDeck());
				cards.addAll(playerManager.players[playerManager.getCurrentPlayer()].getFaceDownDeck().getDeck());
				c.send(new CalculatePlayerClient(this.playerManager.getCurrentPlayer(), cards.stream().map(i -> i.getName()).toArray(String[]::new)));
			}
		}


		if( CURRENT_STATE == GAME_STATE.PICK_TOURNAMENT) {
			if(isInPane(faceDownPanes[cPlayer], point) && isPickQuestValid(faceDownCards, card) 
					&& (card.getType() != TYPE.AMOUR || isPlayingAmourValid(playerManager.getFaceDownCardsAsList(cPlayer), playerManager.getFaceUpCardsAsList(cPlayer), card)) ||

					isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer])) {
				doPutCardIntoPane(point, card);
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.addAll(playerManager.players[playerManager.getCurrentPlayer()].getFaceUp().getDeck());
				cards.addAll(playerManager.players[playerManager.getCurrentPlayer()].getFaceDownDeck().getDeck());
				c.send(new CalculatePlayerClient(this.playerManager.getCurrentPlayer(), cards.stream().map(i -> i.getName()).toArray(String[]::new)));
			}
		}
		
		if(CURRENT_STATE == GAME_STATE.DISCARDING_CARDS) {
			//if the current player has too many cards, we can allow him to play cards into the discard pile
			//We can also allow players to play amour/ally cards into the face down pane
			if(isInPane(faceDownPanes[cPlayer], point) && 
					((card.getType() == TYPE.AMOUR && isPlayingAmourValid(playerManager.getFaceDownCardsAsList(cPlayer), playerManager.getFaceUpCardsAsList(cPlayer), card)) || 
					 (card.getType() == TYPE.ALLIES))) {
				doPutCardIntoPane(point, card);
			}else if(isInPane(discardPane, point)) {
				System.out.println("Discarding this card + " + card.getName());
				doPutCardIntoPane(point, card);
			} else if(isInPane(handPanes[cPlayer], point) && (card.childOf.equals(discardPane) || card.childOf.equals(faceDownPanes[cPlayer]))) {
				doPutCardIntoPane(point, card);
			}
		}
		
		if(CURRENT_STATE == GAME_STATE.BID_DISCARD) {
			if(isInPane(discardPane, point)) {
				doPutCardIntoPane(point, card);
			} else if(isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer])) {
				doPutCardIntoPane(point, card);
			}
		}
		
		if(CURRENT_STATE == GAME_STATE.EVENT_DISCARD) {
			if(isInPane(discardPane, point) && card.getType() == this.type) {
				doPutCardIntoPane(point, card);
			} else if(isInPane(handPanes[cPlayer], point) && !card.childOf.equals(handPanes[cPlayer])) {
				doPutCardIntoPane(point, card);
			}
		}
		
		//return to original position if we don't put it into the pane
		card.returnOriginalPosition();
	}

	private void putCardIntoPane(Pane to, AdventureCard card) {
		// adding card to the to pane
		ArrayList<AdventureCard> toAdd = paneDeckMap.get(to);
		to.getChildren().add(card.getImageView());
		toAdd.add(card);

		card.childOf = to;

		repositionCardsInHand(playerManager.getCurrentPlayer());
		repositionFaceDownCards(playerManager.getCurrentPlayer());

		if(CURRENT_STATE == GAME_STATE.PICK_STAGES) {
			repositionStageCards();
		}

		repositionFaceUpCards(playerManager.getCurrentPlayer());
		repositionDiscardPile();


		//reset the original position of this card cards
		card.setOriginalPosition(card.getImageView().getX(), card.getImageView().getY());
	}

	private void removeFromPane(Pane from, AdventureCard card) {
		// removing card from the from pane
		ArrayList<AdventureCard> toRemove = paneDeckMap.get(from);
		from.getChildren().remove(card.getImageView());
		toRemove.remove(card);
	}

	public void moveCardBetweenPanes(Pane from, Pane to, AdventureCard card) {
		logger.info("Putting card: " + card.getName() + " into: " + to + " from: " + from);
		removeFromPane(from, card);
		putCardIntoPane(to, card);
	}

	private void doPutCardIntoPane(Point2D point, AdventureCard card ) {
		Pane from = card.childOf;
		removeFromPane(from, card);
		//find which pane we want to place the card into right now
		Pane to = mouseOverPane(point);

		logger.info("Putting card: " + card.getName() + " into: " + to + " from: " + from);
		putCardIntoPane(to, card);
	}


	/* ************************************************
	 *  VALIDATION METHODS FOR PUTTING CARDS INTO PANES
	 **************************************************/
	/**
	 *  Checks if we can add the toAdd card into the stageCards pile during Pick stages
	 * @param stageCards
	 * @param toAdd
	 * @return
	 */
	public boolean isStageValid(ArrayList<AdventureCard> stageCards, AdventureCard toAdd) {
		//Make sure the current stage has either 1 foe or 1 test card	
		if(CURRENT_STATE == GAME_STATE.PICK_STAGES) {
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
		if(CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS || CURRENT_STATE == GAME_STATE.PICK_TOURNAMENT) {
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
			return cardType == TYPE.ALLIES;
		}
		return false;
	}


	private boolean stagesIncreasing() {
		int lastBp = Integer.MIN_VALUE;
		for(int i = 0; i < stages.length; i++) {
			if(stages[i].isVisible() && stageCards.get(i).get(0).getType() != TYPE.TESTS) {
				if(bpTexts[i].getText().equals("")) {
					return false;
				} else if(Integer.parseInt(bpTexts[i].getText()) <= lastBp){
					return false;
				}
				lastBp = Integer.parseInt(bpTexts[i].getText());
			}
		}

		return true;
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
	
	//Can only have 1 Amour card present at any time of the game for the player
	private boolean isPlayingAmourValid(ArrayList<AdventureCard> fuc, ArrayList<AdventureCard> fdc, AdventureCard toAdd) {
		for(AdventureCard c : fuc) {
			if(c.getType() == TYPE.AMOUR) {
				return false;
			}
		}
		for(AdventureCard c : fdc) {
			if(c.getType() == TYPE.AMOUR) {
				return false;
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

		//after we set the perspective to this player number, check if the player has over the card hand limit
		//if it's full, we must discard cards.
		if(playerManager.isHandFull(playerNum)) {
			setDiscardVisibility(true);
		}

		//readjust the player pane's scale as well as the orientation of the rank/shield cards
		for(int i = 0 ; i < handPanes.length; i++) {
			if(i == playerNum) {
				playerPanes[i].setScaleX(1);
				playerPanes[i].setScaleY(1);
				playerRanks[i].setRotate(0);
				shieldViews[i].setRotate(0);
			}else {
				playerPanes[i].setScaleX(0.6);
				playerPanes[i].setScaleY(0.6);
				playerRanks[i].setRotate(180);
				shieldViews[i].setRotate(180);
			}
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
			for(int i=0; i<num; i++) { stageViews[i].setImage(banner); stageViews[i].setVisible(true); }
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

	/* *****************
	 * BUTTON VISIBILITY
	 ******************/
	public void setButtonsInvisible() {
		this.endTurn.setVisible(false);
		this.accept.setVisible(false);
		this.decline.setVisible(false);
		this.nextTurn.setVisible(false);
		this.discard.setVisible(false);
		this.startTurn.setVisible(false);
		this.useMerlin.setVisible(false);
		this.useMordred.setVisible(false);
	}

	public void showAcceptDecline() {
		this.accept.setVisible(true);
		this.decline.setVisible(true);
	}
	public void showDecline() {
		this.decline.setVisible(true);
	}

	public void showStartTurn() {
		this.startTurn.setVisible(true);
	}


	public void showNextTurn() {
		this.nextTurn.setVisible(true);
	}

	public void showEndTurn() {
		this.endTurn.setVisible(true);
	}

	public void setDiscardVisibility(boolean b) {
		this.discard.setVisible(b);
	}
	
	public void setMerlinMordredVisibility() {
		this.playerManager.players[playerManager.getCurrentPlayer()].hand.getDeck().forEach(i -> {
			if(i.getName().equals("Mordred")) {
				this.useMordred.setVisible(true);
			} else if(i.getName().equals("Merlin")) {
				this.useMerlin.setVisible(true);
			}
		});
		
		this.playerManager.players[playerManager.getCurrentPlayer()].getFaceUp().getDeck().forEach(i -> {
			if(i.getName().equals("Merlin")) {
				this.useMerlin.setVisible(true);
			}
		});
	}

	public void removeDraggable() {
		ArrayList<AdventureCard> currHand = playerManager.getPlayerHand(playerManager.getCurrentPlayer());
		for(int i = 0 ; i < currHand.size(); i++) {
			currHand.get(i).setDraggableOff();
		}
	}

	public void removeDraggableFaceDown() {
		ArrayList<AdventureCard> currHand = playerManager.getFaceDownCardsAsList(playerManager.getCurrentPlayer());
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
	
	public void flipAllFaceDownPane(boolean isShow) {
		for(int i = 0 ; i < playerManager.getNumPlayers() ; i++) {
			playerManager.flipFaceDownCards(i, isShow);
		}
	}

	public void moveToFaceUpPane(int p) {
		logger.info("Moving Cards to Face up pane");
		playerManager.players[p].getFaceDownDeck().getDeck().forEach(i -> {
			i.faceDown();
			i.setDraggableOff();
		});
		playerManager.players[p].flipCards();
		while(faceDownPanes[p].getChildren().size() > 0) { faceUpPanes[p].getChildren().add(faceDownPanes[p].getChildren().remove(0));}
		repositionFaceUpCards(p);
		repositionFaceDownCards(p);
		playerManager.flipFaceUpCards(p);
		logger.info("Face up cards: " + Arrays.toString(playerManager.players[p].getFaceUp().getDeck().stream().map(i -> i.getName()).toArray()));
	}

	public void setStageCardVisibility( boolean isShow, int... stageNum) {
		for(int i: stageNum) {
			currentStage = i;
			ArrayList<AdventureCard> cards = stageCards.get(i);
			for(AdventureCard c : cards) {
				if(isShow) {
					c.faceUp();
				}else {
					c.faceDown();
				}
			}
		}
	}

	public void setPlayerRank(int p, Rank.RANKS r) {
		playerManager.setPlayerRank(p, r);
		String rank = "";
		if( r == Rank.RANKS.SQUIRE) rank = "/R Squire.png";
		if( r == Rank.RANKS.KNIGHT) rank = "/R Knight.png";
		if( r == Rank.RANKS.CHAMPION) rank = "/R Champion Knight.png";
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


	public void discardFaceUpCards(int p, String[] cardNames) {
		logger.info("Discarding: " + Arrays.toString(cardNames));
		ArrayList<AdventureCard> fdc = playerManager.getFaceUpCardsAsList(p);
		for(String n : cardNames) {
			logger.info("Discarding: " + n);
			for(int i = 0 ; i < fdc.size(); i++) {
				//find cards to discard in fdc  and the image view from the pane
				if(fdc.get(i).getName().equalsIgnoreCase(n)) {
					faceUpPanes[p].getChildren().remove(i);

					fdc.remove(i);
					System.out.println(fdc + " " + faceDownPanes[p].getChildren().size());
				}
			}
		}
		repositionFaceUpCards(p);
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

	public void setShield(int p, int s) {
		playerManager.setShields(p, s);
		if(p==0) {p1Shields.setText(getShields(p)+"");}
		if(p==1) {p2Shields.setText(getShields(p)+"");}
		if(p==2) {p3Shields.setText(getShields(p)+"");}
		if(p==3) {p4Shields.setText(getShields(p)+"");}
	}

	public int getShields(int p) {
		return playerManager.getShields(p);
	}

	public void setUp() {
		this.startTurn.setOnAction(e->{
			if(CURRENT_STATE == GAME_STATE.CHILLING) {
				synchronized (c) {
					c.notify();
					int viewAbleStage =  playerManager.viewableStage(playerManager.getCurrentPlayer());
					if(viewAbleStage != -1) {
						setStageCardVisibility(true, viewAbleStage);
						repositionStageCards(viewAbleStage);;
					}
				}
			}
		});
		/*
		 * END TURN BUTTON LISTENER
		 */
		this.endTurn.setOnAction(e -> {

			int currentPlayer = playerManager.getCurrentPlayer();
			if(CURRENT_STATE == GAME_STATE.PICK_TOURNAMENT) {
				playerManager.flipFaceDownCards(currentPlayer, false);
				c.send(new TournamentPickCardsClient(currentPlayer, 
						playerManager.getFaceDownCardsAsList(currentPlayer).stream().map(i -> i.getName()).toArray(size -> new String[size])));
			}

			else if(CURRENT_STATE == GAME_STATE.EVENT_DISCARD) {
				String[] discardCards = discardPile.stream().map(c -> c.getName()).toArray(String[]::new);
				if(discardCards.length != this.toDiscard) {
					clearToast();
					showToast("Select: " + this.toDiscard + " cards to discard");
					return;
				}
				
				discardPile.clear();
				discardPane.getChildren().clear();
				this.hideDiscardPane();
				c.send(new EventDiscardCardsClient(currentPlayer,discardCards));
			}
			else if(CURRENT_STATE == GAME_STATE.PICK_STAGES) {
				if(areQuestStagesValid() && stagesIncreasing()) {
					for(int i = 0 ; i < stageCards.size(); i++) {
						if(!stageCards.get(i).isEmpty()) {
							String[] currentStageCards = new String[stageCards.get(i).size()];
							for(int j = 0 ; j < stageCards.get(i).size(); j++) {
								currentStageCards[j] = stageCards.get(i).get(j).getName();
								stageCards.get(i).get(j).setDraggableOff();
							}
							c.send(new QuestPickStagesClient(currentPlayer, currentStageCards, i));
							setStageCardVisibility(false, i);
							stackStageCards();
						}
					}
				}else {
					if(!areQuestStagesValid()) {
						toast.setText("Quest stages are not valid. Each stage needs 1 foe or 1 test");
						System.out.println("Quest stages are not valid. Each stage needs 1 foe or 1 test");
					} else {
						toast.setText("Quest stages are not valid. Each stage needs an increasing amount of bp");
						System.out.println("Quest stages are not valid. Each stage needs an increasing amount of bp");
					}
				}

			}else if(CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS) {
				//send cards 
				ArrayList<AdventureCard> faceDownCards = playerManager.getFaceDownCardsAsList(currentPlayer);
				String[] cards = new String[faceDownCards.size()];
				for(int i = 0 ; i < cards.length ; i++) {
					cards[i] = faceDownCards.get(i).getName();
				}

				//if merlin hide the stage again
				int viewAbleStage = playerManager.viewableStage(currentPlayer);
				if(viewAbleStage != -1) {
					setStageCardVisibility(false, viewAbleStage);
					stackStageCards();
				}

				c.send(new QuestPickCardsClient(currentPlayer, cards));

			}
			//TODO::verify cards are minimum number of cards
			else if(CURRENT_STATE == GAME_STATE.QUEST_BID) {
				c.send(new QuestBidClient(currentPlayer, (int)bidSlider.getValue()));
				this.bidSlider.setVisible(false);
			}else if(CURRENT_STATE == GAME_STATE.BID_DISCARD) {
				String[] discardCards = discardPile.stream().map(c -> c.getName()).toArray(String[]::new);
				if(discardCards.length != this.toDiscard) {
					clearToast();
					showToast("Select: " + this.toDiscard + " cards to discard");
					return;
				}
				
				discardPile.clear();
				discardPane.getChildren().clear();
				this.hideDiscardPane();
				c.send(new QuestDiscardCardsClient(currentPlayer,discardCards));
			}

		});

		/*
		 * ACCEPT BUTTON LISTENER
		 */
		this.accept.setOnAction(e -> {
			System.out.println("accepted story");
			if(CURRENT_STATE == GAME_STATE.JOIN_TOURNAMENT) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " accepted tournament");
				joinTournament[playerManager.getCurrentPlayer()] = true;
				c.send(new TournamentAcceptDeclineClient(playerManager.getCurrentPlayer(), true));
				setGlow(playerManager.getCurrentPlayer());
			}else if(CURRENT_STATE == GAME_STATE.SPONSOR_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " accepted quest sponsoring");
				c.send(new QuestSponsorClient(playerManager.getCurrentPlayer(), true));
			}else if(CURRENT_STATE == GAME_STATE.JOIN_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " joined quest");
				//make sure we turn over the viewable stage if previous used merlin
				if(playerManager.viewableStage(playerManager.getCurrentPlayer()) != -1) {
					flipStageCards(playerManager.viewableStage(playerManager.getCurrentPlayer()), false);
				}

				//if merlin hide the stage again
				int viewAbleStage = playerManager.viewableStage(playerManager.getCurrentPlayer() );
				if(viewAbleStage != -1) {
					setStageCardVisibility(false, viewAbleStage);
					stackStageCards();
				}
				c.send(new QuestJoinClient(playerManager.getCurrentPlayer(), true));
				setGlow(playerManager.getCurrentPlayer());
			}
		});
		/*
		 * DECLINE BUTTON LISTENER
		 */
		this.decline.setOnAction(e -> {
			System.out.println("declined story");
			if(CURRENT_STATE == GAME_STATE.JOIN_TOURNAMENT) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined tournament");
				c.send(new TournamentAcceptDeclineClient(playerManager.getCurrentPlayer(), false));
			}else if(CURRENT_STATE == GAME_STATE.SPONSOR_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined quest sponsoring");
				c.send(new QuestSponsorClient(playerManager.getCurrentPlayer(), false));
			}else if(CURRENT_STATE == GAME_STATE.JOIN_QUEST) {
				System.out.println("Client: player" + playerManager.getCurrentPlayer()  + " declined quest");
				c.send(new QuestJoinClient(playerManager.getCurrentPlayer(), false));
			}else if(CURRENT_STATE == GAME_STATE.QUEST_BID) {
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
		/*
		 * button dedicated for handling discarding :>
		 */
		this.discard.setOnAction(e->{
			System.out.println("Clicked Discard");
			int cPlayer = playerManager.getCurrentPlayer();
			String[] discardCards = discardPile.stream().map(c -> c.getName()).toArray(String[]::new);
			String[] allyCards = playerManager.getFaceDownCardsAsList(cPlayer).stream().map(c -> c.getName()).toArray(String[]::new);
			if(playerManager.getPlayerHand(playerManager.getCurrentPlayer()).size() > playerManager.MAX_HAND_SIZE) {
				toast.setText("You must discard exactly " + 
						(playerManager.getPlayerHand(playerManager.getCurrentPlayer()).size() - playerManager.MAX_HAND_SIZE) + 
						" card(s)");
				logger.info("Player #"+ playerManager.getCurrentPlayer()+"'s hand is too full!");
				return;
			}else if(playerManager.getPlayerHand(playerManager.getCurrentPlayer()).size() < playerManager.MAX_HAND_SIZE && !discardPile.isEmpty()) {
				toast.setText("Can only discard cards if hand has more than 12 cards");
				return;
			}
			setDiscardVisibility(false);
			c.send(new HandFullClient(playerManager.getCurrentPlayer(), discardCards, allyCards));
			
			//add the facedown cards to faceup pane
			ArrayList<AdventureCard> fuc = playerManager.getFaceUpCardsAsList(cPlayer);
			ArrayList<AdventureCard> fdc = playerManager.getFaceDownCardsAsList(cPlayer);
			for(AdventureCard c : fdc) {
				fuc.add(c);
				faceUpPanes[cPlayer].getChildren().add(c.getImageView());
			}
			fdc.clear();
			discardPile.clear();
			discardPane.getChildren().clear();
			faceDownPanes[cPlayer].getChildren().clear();
			this.hideDiscardPane();
			repositionFaceUpCards(cPlayer);
			repositionCardsInHand(cPlayer);
		});

		/*
		 * Setup merlin button
		 */
		this.useMerlin.setOnAction(e->{
			//check if current player has merlin in play.
			int currentPlayer = playerManager.getCurrentPlayer();
			ArrayList<AdventureCard> currFUC = playerManager.getFaceUpCardsAsList(currentPlayer);
			for(AdventureCard c:currFUC) {
				if(c.isMerlin()) {
					List<String> dialogChoices = new ArrayList<String>();
					//get current active stages
					int numStages = 0;
					for(ArrayList<AdventureCard> stageList: stageCards) {
						final int stage = numStages;
						if(!stageList.isEmpty()) {
							dialogChoices.add((stage+1)+"");
							numStages++;
						}
					}
					merlinDialog = new ChoiceDialog<>(null, dialogChoices);
					merlinDialog.setTitle("Using Merlin Power");
					merlinDialog.setHeaderText("Select a stage to show");
					merlinDialog.setContentText("Stage #:");
					Optional<String> result = merlinDialog.showAndWait();
					if(result.isPresent() && c.tryUseMerlin()) {
						int s = Integer.parseInt(result.get()) - 1;
						useMerlinPower(s, c);
					}
					return;
				}
			}
			System.out.println("You do not have Merlin in play");
		});
		
		this.useMordred.setOnAction(e->{
			//check if current player has mordred
			int currentPlayer = playerManager.getCurrentPlayer();
			ArrayList<AdventureCard> hand = playerManager.getPlayerHand(currentPlayer);
			AdventureCard mordred = null;
			int mIndex = -1;
			for(int i = 0 ; i < hand.size(); i++) {
				if(hand.get(i).isMordred()) {
					mordred = hand.get(i);
					mIndex = i;
				}
			}
			//if it's not in either hand or facedown field then player does not have mordred
			if(mordred == null) {
				System.out.println("You do not have Mordred");
				return;
			}
			
			//setup dialog to choose which card to delete
			Map<String, Integer[]> dialogChoices = new HashMap<String, Integer[]>();
			ArrayList<String> choices = new ArrayList<String>();
			//get all the face up cards on the board
			for(int p = 0 ; p < playerManager.getNumPlayers() ; p++) {
				ArrayList<AdventureCard> fuc = playerManager.getFaceUpCardsAsList(p);
				for(int i = 0 ; i < fuc.size(); i++) {
					if(p != playerManager.getCurrentPlayer()) {
						Integer[] pNumAndCardID = new Integer[3];
						pNumAndCardID[0] = p;			//idx 0 = playerNum
						pNumAndCardID[1] = fuc.get(i).id;//idx 1= cardid
						pNumAndCardID[2] = i;			//idx 2 = idx of the card in list
						dialogChoices.put("Player #" + p + " " + fuc.get(i).getName(), pNumAndCardID);
						choices.add("Player #" + p + " " + fuc.get(i).getName());
					}
				}
			}
		    
			mordredDialog = new ChoiceDialog<>(null, choices);
			mordredDialog.setTitle("Using Mordred's Power");
			mordredDialog.setHeaderText("Select an opponent's ally card to destroy");
			mordredDialog.setContentText("Ally Card:");
			Optional<String> result = merlinDialog.showAndWait();
			if(result.isPresent()) {
				Integer[] pNumAndCard = dialogChoices.get(result.get());
				useMordred(mordred, mIndex, currentPlayer, pNumAndCard[2],pNumAndCard[0], result.get().substring(10));
			}
			
		});
	}
	
	public void useMordred(AdventureCard mordred, int mIndex, int currentPlayer, int oIndex, int oPlayer, String otherAlly) {
		//dicard the current mordred card
		Pane mordredContainer = mordred.childOf;
		mordredContainer.getChildren().remove(mIndex);
		playerManager.removeCardFromHand(mordred, currentPlayer);
		
		faceUpPanes[oPlayer].getChildren().remove(oIndex);
		ArrayList<AdventureCard> fuc = playerManager.getFaceUpCardsAsList(oPlayer);
		fuc.remove(oIndex);
		c.send(new MordredClient(this.playerManager.getCurrentPlayer(), oPlayer, otherAlly));
	}
	
	public void resetMerlinUse() {
		//find the merlins and reset their charge to 1
		for(int i = 0 ; i < playerManager.getNumPlayers() ; i++) {
			ArrayList<AdventureCard> currFUC = playerManager.getFaceUpCardsAsList(i);
			for(AdventureCard c : currFUC) {
				if(c.isMerlin()) {
					c.resetMerlinCharges();
					playerManager.resetMerlinViewableStage();
				}
			}
		}
	}
	
	public ImageView findCardInHand(String cardName) {
		ArrayList<AdventureCard> phand = playerManager.getPlayerHand(playerManager.getCurrentPlayer());
		for(AdventureCard c: phand) {
			if(c.getName().equals(cardName)) {
				return c.getImageView();
			}
		}
		return null;
	}
	public void useMerlinPower(int s, AdventureCard c) {
		if(c.tryUseMerlin()) {
			setStageCardVisibility(true,s);
			repositionStageCards(s);
			playerManager.rememberStage(playerManager.getCurrentPlayer(), s);
			logger.info("Player " + playerManager.getCurrentPlayer() + " has used the merlin card to reveal stage " + (s+1));
		}
	}
}

