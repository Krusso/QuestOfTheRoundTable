package src.client;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import src.client.GameBoardController.GAME_STATE;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.AllyCard;
import src.game_logic.AmourCard;
import src.game_logic.FoeCard;
import src.game_logic.QuestCard;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;
import src.game_logic.TestCard;
import src.game_logic.WeaponCard;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.events.EventDiscardCardsServer;
import src.messages.game.CalculatePlayerClient;
import src.messages.game.CalculatePlayerServer;
import src.messages.game.CalculateStageClient;
import src.messages.game.CalculateStageServer;
import src.messages.game.ContinueGameClient;
import src.messages.game.ContinueGameServer;
import src.messages.game.MiddleCardServer;
import src.messages.game.ShieldCountServer;
import src.messages.game.TurnNextServer;
import src.messages.gameend.FinalTournamentNotifyServer;
import src.messages.gameend.GameOverServer;
import src.messages.hand.AddCardsServer;
import src.messages.hand.FaceDownServer;
import src.messages.hand.FaceUpDiscardServer;
import src.messages.hand.HandFullServer;
import src.messages.quest.QuestBidServer;
import src.messages.quest.QuestDiscardCardsServer;
import src.messages.quest.QuestJoinServer;
import src.messages.quest.QuestPassAllServer;
import src.messages.quest.QuestPassStageServer;
import src.messages.quest.QuestPickCardsServer;
import src.messages.quest.QuestPickStagesServer;
import src.messages.quest.QuestSponsorServer;
import src.messages.quest.QuestSponsorServerCant;
import src.messages.quest.QuestUpServer;
import src.messages.rank.RankServer;
import src.messages.tournament.TournamentAcceptDeclineServer;
import src.messages.tournament.TournamentPickCardsServer;
import src.messages.tournament.TournamentWinServer;

class AddCardsTask extends Task{
	final static Logger logger = LogManager.getLogger(AddCardsTask.class);
	private int player;
	private String[] cards;
	public AddCardsTask(GameBoardController gbc, int player, String[] cards) {
		super(gbc);
		this.player = player;
		this.cards = cards;
	}

	@Override
	public void run() {
		File[] list = cardDir.listFiles();
		for(String card: cards) {
			boolean didAddCard = false;
			//find file associated to name
			for(File f : list) {
				if ((f.getName().contains(card+".png") || f.getName().contains(card+".jpg")) && 
						((f.getName().length()-6) == card.length() || (f.getName().length()-4) == card.length())) {
					AdventureDeck ad = new AdventureDeck();
					ad.populate();
					switch (f.getName().charAt(0)) {
					case 'A':{
						if(card.equals("Amour")) {
							AdventureCard c = ad.getCardByName(card);
							c.setImgView(f.getPath());
							c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
							c.faceDown();
							gbc.addCardToHand(c, player);
						} else {
							AdventureCard c = ad.getCardByName(card);
							c.setImgView(f.getPath());
							c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
							c.faceDown();
							gbc.addCardToHand(c, player);
						}
						didAddCard = true;
						break;
					}
					case 'F' : {
						AdventureCard c = ad.getCardByName(card);
						c.setImgView(f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						gbc.addCardToHand(c, player);
						c.faceDown();
						didAddCard = true;
						break;
					}
					case 'T' : {
						AdventureCard c = ad.getCardByName(card);
						c.setImgView(f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						gbc.addCardToHand(c, player);
						c.faceDown();
						didAddCard = true;
						break;
					}
					case 'W':{
						AdventureCard c = ad.getCardByName(card);
						c.setImgView(f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						gbc.addCardToHand(c, player);
						c.faceDown();
						didAddCard = true;
						break;
					}
					default:{
						break;
					}
					}
				}
			}
			if(!didAddCard) {
				logger.warn("Could not add " + card + " to hand");
			}
		}
	}
}
class TurnNextTask extends Task{
	private int player;
	public TurnNextTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}

	//Msg should be a string with a number which indicates which players has the turn
	@Override
	public void run() {
		gbc.removeDraggable();
		gbc.removeDraggableFaceDown();
		gbc.setPlayerTurn(player);
		gbc.showPlayerHand(player);
	}
}

class MiddleCardTask extends Task{
	final static Logger logger = LogManager.getLogger(MiddleCardTask.class);

	private String card;
	public MiddleCardTask(GameBoardController gbc, String card) {
		super(gbc);
		this.card = card;
	}

	//Msg should be the name of the card
	@Override
	public void run() {
		logger.info("Processing msg: middle card:" + card);
		//find story card
		File[] list = cardDir.listFiles();
		for(File c : list) {
			if(c.getName().contains(card)) {
				StoryCard sc= new StoryCard(card, c.getPath());
				gbc.setStoryCard(sc);
				logger.info("Set story card to:" + sc.getName());
				// hell yaaaaaaaaaaaa!!!
				switch(sc.getName()) {
				case "Search for the Holy Grail":
					gbc.questCard = new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"});
					break;
				case "Test of the Green Knight":
					gbc.questCard = new QuestCard("Test of the Green Knight",4,new String[] {"Green Knight", "Sir Gawain"});
					break;
				case "Search for the Questing Beast":
					gbc.questCard = new QuestCard("Search for the Questing Beast",4,new String[] {});
					break;
				case "Defend the Queen's Honor":
					gbc.questCard = new QuestCard("Defend the Queen's Honor",4,new String[] {"All", "Sir Lancelot"});
				case "Rescue the Fair Maiden":
					gbc.questCard = new QuestCard("Rescue the Fair Maiden",3,new String[] {"Black Knight"});
					break;
				case "Journey Through the Enchanted Forest":
					gbc.questCard = new QuestCard("Journey Through the Enchanted Forest",3,new String[] {"Evil Knight"});
					break;
				case "Vanquish King Arthur's Enemies":
					gbc.questCard = new QuestCard("Vanquish King Arthur's Enemies",3,new String[] {});
					break;
				case "Slay the Dragon":
					gbc.questCard = new QuestCard("Slay the Dragon",3,new String[] {"Dragon"});
					break;
				case "Boar Hunt":
					gbc.questCard = new QuestCard("Boar Hunt",2,new String[] {"Boar"});
					break;
				case "Repel the Saxon Raiders":
					gbc.questCard = new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"});
					break;
				}
				return;
			}
		}
		logger.warn("Could not set story card to " + card);
		
	}
}

class QuestSponsorTaskCant extends Task {
	private int player;
	public QuestSponsorTaskCant(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}

	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.SPONSOR_QUEST;
		gbc.setButtonsInvisible();
		gbc.setPlayerPerspectiveTo(player);
		gbc.showDecline();
		gbc.showToast("Cant Sponsor Tournament");
		gbc.setMerlinMordredVisibility();
		if(gbc.playerManager.getAI(player) != null) {
			gbc.decline.fire();
		}
	}
}

class QuestSponsorTask extends Task {
	private int player;
	public QuestSponsorTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}

	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.SPONSOR_QUEST;
		gbc.setButtonsInvisible();
		gbc.setPlayerPerspectiveTo(player);
		gbc.showAcceptDecline();
		gbc.showToast("Sponsor Quest?");
		gbc.setMerlinMordredVisibility();
		if(gbc.playerManager.getAI(player) != null) {
			if(gbc.playerManager.getAI(player).doISponsorAQuest(gbc.questCard) != null) {
				gbc.accept.fire();
			} else {
				gbc.decline.fire();
			}
		}
	}
}

class TournamentWonTask extends Task{

	final static Logger logger = LogManager.getLogger(TournamentWonTask.class);
	private int[] players;
	public TournamentWonTask(GameBoardController gbc, int[] players) {
		super(gbc);
		this.players = players;
	}

	@Override
	public void run() {
		for(int i = 0; i < winners.length; i++) {
			winners[i] = false;
		}
		
		for(int i = 0 ; i < players.length;i++) {
			winners[players[i]] = true;
		}
		String display = "";
		for(int i = 0 ; i < winners.length; i++) {
			if(winners[i] == true) {
				display = display + (i + 1) + ", ";
			}
		}
		display = display.substring(0, display.length()-2);
		// sorry this was triggering me
		if(display.length()>1) {
			display = "Players " + display + " won the tournament!";
		} else { 
			display = "Player " + display + " won the tournament!";
		}
		logger.info(display);
		gbc.clearToast();
		gbc.showToast(display);
		//reset merlin power
		gbc.resetMerlinUse();

	}
}

class SetRankTask extends Task{
	private RANKS rank;
	private int player;
	public SetRankTask(GameBoardController gbc, RANKS newrank, int player) {
		super(gbc);
		this.rank = newrank;
		this.player = player;
	}

	@Override
	public void run() {
		Rank.RANKS r = Rank.RANKS.SQUIRE;
		if(rank.name().equals("KNIGHT")) r = Rank.RANKS.KNIGHT;
		if(rank.name().equals("CHAMPION")) r = Rank.RANKS.CHAMPION;
		if(rank.name().equals("KNIGHTOFTHEROUNDTABLE")) r = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
		gbc.setPlayerRank(player, r);

	}
}
class ShowEndTurn extends Task {

	final static Logger logger = LogManager.getLogger(ShowEndTurn.class);
	public ShowEndTurn(GameBoardController gbc, int player) {
		super(gbc);
	}

	// no msg expected
	@Override
	public void run() {
		logger.info("Processing msg: ShowEndTurn");
		gbc.showEndTurn();
		gbc.addDraggable();
	}
}

class QuestPickStagesTask extends Task {

	private int player;
	private int numStages;
	public QuestPickStagesTask(GameBoardController gbc, int player, int numStages) {
		super(gbc);
		this.player = player;
		this.numStages = numStages;

	}
	@Override
	public void run() {
		gbc.setButtonsInvisible();
		gbc.CURRENT_STATE = GAME_STATE.PICK_STAGES;
		gbc.setPickStageOn(numStages);
		gbc.addDraggable();
		gbc.setPlayerPerspectiveTo(player);
		gbc.showEndTurn();
		gbc.setQuestStageBanners(numStages);
		gbc.clearToast();
		gbc.showToast("Select cards for each Stage");
		gbc.setMerlinMordredVisibility();
		if(gbc.playerManager.getAI(player) != null) {
			List<List<AdventureCard>> cards = gbc.playerManager.getAI(player).doISponsorAQuest(gbc.questCard);
			for(int i = 0; i < cards.size(); i++) {
				for(int j = 0; j < cards.get(i).size(); j++) {
					gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.stages[i], cards.get(i).get(j));	
				}
			}
			
			for(int i = 0; i < gbc.stages.length; i++) {
				if(gbc.stages[i].isVisible()) {
					gbc.c.send(new CalculateStageClient(gbc.playerManager.getCurrentPlayer(),gbc.stageCards.get(i).stream().map(j -> j.getName()).toArray(String[]::new), i));	
				}
			}
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gbc.endTurn.fire();
				}
			});
		}
	}
}

class QuestJoinTask extends Task {

	private int player;
	public QuestJoinTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;

	}
	@Override
	public void run() {
		gbc.setButtonsInvisible();
		gbc.CURRENT_STATE = GAME_STATE.JOIN_QUEST;
		gbc.showAcceptDecline();
		gbc.setPlayerPerspectiveTo(player);
		gbc.addDraggable();
		gbc.clearToast();
		gbc.showToast("Join Quest?");
		gbc.setMerlinMordredVisibility();
		if(gbc.playerManager.getAI(player) != null) {
			if(gbc.playerManager.getAI(player).doIParticipateInQuest(gbc.questCard)) {
				gbc.accept.fire();
			} else {
				gbc.decline.fire();
			}
		}
	}

}

class QuestPickCardsTask extends Task {

	private int player;
	public QuestPickCardsTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;

	}
	@Override
	public void run() {
		gbc.setButtonsInvisible();
		gbc.CURRENT_STATE = GAME_STATE.QUEST_PICK_CARDS;
		gbc.setPlayerPerspectiveTo(player);
		gbc.showEndTurn();
		gbc.addDraggable();
		gbc.removeStagePaneDragOver();
		gbc.clearHighlight();
		gbc.highlightFaceUp(player);
		gbc.clearToast();
		gbc.showToast("Select Cards for current stage");
		gbc.setMerlinMordredVisibility();
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cards = gbc.playerManager.getAI(player).playCardsForFoeQuest(gbc.questCard.getNumStages() == gbc.currentStage + 1,gbc.questCard);
			cards.forEach(i -> gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.faceDownPanes[player], i));
			gbc.endTurn.fire();
		}
	}
}
class FaceDownCardsTask extends Task {

	private int player;
	public FaceDownCardsTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;

	}
	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.FACE_DOWN_CARDS;
		gbc.flipFaceDownPane(player, false);
	}
}
class UpQuestTask extends Task {

	private int player;
	private int stage;
	public UpQuestTask(GameBoardController gbc, String[]cards, int player, int stage) {
		super(gbc);
		this.player = player;
		this.stage = stage;

	}
	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.UP_QUEST;
		//		gbc.flipStageCards(this.stage, true);
		gbc.setStageCardVisibility(true, stage);
		gbc.repositionStageCards(stage);
	}
}
class DiscardFaceUpTask extends Task {
	final static Logger logger = LogManager.getLogger(DiscardFaceUpTask.class);
	private String[] cardsToDiscard;
	private int player;
	public DiscardFaceUpTask(GameBoardController gbc, int player, String[] cardsDiscarded) {
		super(gbc);
		this.player = player;
		this.cardsToDiscard = cardsDiscarded;
	}
	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.DISCARDING_CARDS;
		logger.info("removing: " + Arrays.asList(cardsToDiscard) + " : " + player);
		gbc.discardFaceUpCards(player,cardsToDiscard);
	}
}

class ShieldCountTask extends Task {

	private int shields;
	private int player;
	public ShieldCountTask(GameBoardController gbc, int player, int shields) {
		super(gbc);
		this.player = player;
		this.shields = shields;

	}
	@Override
	public void run() {
		//		gbc.CURRENT_STATE = STATE.DISCARDING_CARDS; //may add a state for shields
		gbc.setShield(player, shields);
	}
}

class QuestBidTask extends Task {
	private int min;
	private int max;
	private int player;
	public QuestBidTask(GameBoardController gbc, int player, int min, int max) {
		super(gbc);
		this.player = player;
		this.min = min;
		this.max = max;

	}
	@Override
	public void run() {

		gbc.setButtonsInvisible();
		//this means the player can't bid higher than the max
		if(min > max) {
			gbc.showDecline();
			if(gbc.playerManager.getAI(player) != null) {
				gbc.decline.fire();
			}
		}else {
			gbc.showEndTurn();
			gbc.showDecline();
			//			//players can only drag over facedown pane.
			gbc.removeStagePaneDragOver();
			gbc.CURRENT_STATE = GAME_STATE.QUEST_BID;
			gbc.bidSlider.setMin(min);
			gbc.bidSlider.setMax(max);
			gbc.bidSlider.setVisible(true);
			gbc.bidSlider.setMajorTickUnit(2);
			gbc.bidSlider.setShowTickLabels(true);
			gbc.bidSlider.setBlockIncrement(1);
			gbc.bidSlider.setSnapToTicks(true);
			gbc.clearToast();
			gbc.showToast("Use the slider to enter how many cards you want to bid.");
			gbc.setMerlinMordredVisibility();
			if(gbc.playerManager.getAI(player) != null) {
				int amount = gbc.playerManager.getAI(player).nextBid(min);
				if(amount == -1) {
					gbc.decline.fire();
				} else {
					gbc.bidSlider.setValue(amount);
					gbc.endTurn.fire();	
				}
			}
		}
	}
}

class EventDiscardTask extends Task {
	private int player;
	private TYPE type;
	private int amount;
	public EventDiscardTask(GameBoardController gbc, int player, TYPE type, int amount) {
		super(gbc);
		this.player = player;
		this.type = type;
		this.amount = amount;
	}

	@Override
	public void run() {
		gbc.type = type;
		gbc.toDiscard = amount;
		gbc.CURRENT_STATE = GAME_STATE.EVENT_DISCARD;
		gbc.setButtonsInvisible();
		gbc.showEndTurn();
		gbc.setPlayerPerspectiveTo(player);
		gbc.removeStagePaneDragOver();
		gbc.setMerlinMordredVisibility();
		gbc.clearToast();
		gbc.showDiscardPane();
		gbc.addDraggable();
		if(amount == 1) {
			gbc.showToast("Select: " + amount + " " + type.toString() + " card to discard");	
		} else {
			gbc.showToast("Select: " + amount + " " + type.toString() + " cards to discard");
		}
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cards = gbc.playerManager.getAI(player).discardKingsCalltoArms(amount, type);
			cards.forEach(i -> gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.faceDownPanes[player], i));
			gbc.endTurn.fire();
		}
	}
}

class DiscardQuestTask extends Task {
	private int player;
	private int toDiscard;
	public DiscardQuestTask(GameBoardController gbc, int player, int toDiscard) {
		super(gbc);
		this.player = player;
		this.toDiscard = toDiscard;

	}
	@Override
	public void run() {
		//the task that is run before this is the bidquest so we have to hide the bidslider now
		gbc.bidSlider.setVisible(false);
		gbc.CURRENT_STATE = GAME_STATE.BID_DISCARD;
		gbc.setButtonsInvisible();
		gbc.showEndTurn();
		gbc.setPlayerPerspectiveTo(player);
		gbc.addDraggable();
		gbc.removeStagePaneDragOver();
		gbc.showDiscardPane();
		gbc.setMerlinMordredVisibility();
		gbc.clearToast();
		gbc.showToast("Select " +  toDiscard + " cards to discard");
		gbc.toDiscard = toDiscard;
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cards = gbc.playerManager.getAI(player).discardAfterWinningTest();
			if(cards != null) {
				cards.forEach(i -> gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.faceDownPanes[player], i));
			}
			gbc.endTurn.fire();
		}
	}
}


class JoinTournamentTask extends Task {
	final static Logger logger = LogManager.getLogger(JoinTournamentTask.class);
	int player;
	public JoinTournamentTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}
	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.JOIN_TOURNAMENT;
		gbc.setButtonsInvisible();
		gbc.showAcceptDecline();
		gbc.setPlayerPerspectiveTo(player);
		gbc.removeStagePaneDragOver();
		gbc.clearToast();
		gbc.showToast("Join Tournament?");
		gbc.setMerlinMordredVisibility();
		logger.info("Processing join tournament message");
		if(gbc.playerManager.getAI(player) != null) {
			if(gbc.playerManager.getAI(player).doIParticipateInTournament()) {
				gbc.accept.fire();
			} else {
				gbc.decline.fire();
			}
		}
	}
}

class PickTournamentTask extends Task {
	final static Logger logger = LogManager.getLogger(PickTournamentTask.class);
	int player;
	public PickTournamentTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}
	@Override
	public void run() {
		gbc.CURRENT_STATE = GAME_STATE.PICK_TOURNAMENT;
		gbc.setButtonsInvisible();
		gbc.showEndTurn();
		gbc.setPlayerPerspectiveTo(player);
		gbc.addDraggable();
		gbc.removeStagePaneDragOver();
		gbc.clearHighlight();
		gbc.highlightFaceUp(player);
		gbc.clearToast();
		gbc.showToast("Select cards to use for the tournament");
		logger.info("Processing pick tournament cards message");
		gbc.setMerlinMordredVisibility();
	}
}


class UpdateStageBattlePointTask extends Task {
	int player;
	int points;
	int stage;
	public UpdateStageBattlePointTask(GameBoardController gbc, int player, int points, int stage) {
		super(gbc);
		this.player = player;
		this.points = points;
		this.stage = stage;
	}

	@Override
	public void run() {
		if(gbc.playerManager.getCurrentPlayer() == player) {
			gbc.bpTexts[stage].setText(points + "");
		}
	}
}

class UpdateBattlePointTask extends Task {
	int player;
	int points;
	public UpdateBattlePointTask(GameBoardController gbc, int player, int points) {
		super(gbc);
		this.player = player;
		this.points = points;
	}

	@Override
	public void run() {
		if(gbc.playerManager.getCurrentPlayer() == player) {
			gbc.currBP.setText(points + "");
		}
	}
}


class RevealAllCards extends Task {
	public RevealAllCards(GameBoardController gbc) {
		super(gbc);
	}

	@Override
	public void run() {
		this.gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
		this.gbc.removeDraggable();
		IntStream.range(0,this.gbc.playerManager.getNumPlayers()).forEach(i -> this.gbc.moveToFaceUpPane(i));
	}
}
class HandFullDiscardTask extends Task {

	private int player;

	public HandFullDiscardTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;

	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		gbc.CURRENT_STATE = GAME_STATE.DISCARDING_CARDS;
		gbc.showToast("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards");
		gbc.showPlayerHand(player);
		gbc.setButtonsInvisible();
		gbc.setDiscardVisibility(true);
		gbc.addDraggable();
		gbc.showDiscardPane();
		gbc.highlightFaceUp(player);
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cardsToPlay = gbc.playerManager.getAI(player).discardWhenHandFull(gbc.playerManager.players[player].hand.size() - 12);
			cardsToPlay.forEach(i -> gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.discardPane, i));
			gbc.discard.fire();
		}
	}

}

class GameOverTask extends Task {

	private int[] players;
	public GameOverTask(GameBoardController gbc, int[] players) {
		super(gbc);
		this.players = players;

	}
	@Override
	public void run() {
		for(int i = 0; i < players.length; i++) {
			players[i] = players[i] + 1;
		}
		gbc.CURRENT_STATE = GAME_STATE.GAMEOVER;
		gbc.showToast("Player #" + Arrays.toString(players) + " won the game!");
		gbc.flipAllFaceDownPane(true);
		gbc.setButtonsInvisible();

		String winningMessage = "Player #" + Arrays.toString(players) + " won the game!";
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("GameOver.fxml"));
			Scene gameOverScene = new Scene(fxmlLoader.load());
			GameOverController goc = fxmlLoader.getController();
			goc.text.setText(winningMessage);

			//attach image to bg pane
			Image img = new Image(new FileInputStream(new File(cardDir + "/gameover.png")));
			ImageView imgv = new ImageView(img);
			//keep aspect ratio and make imgView fit the whole screen
			double scaleV = 1920 / img.getWidth();
			double scaleH = 1080 / img.getHeight();
			double scaleFactor = scaleV > scaleH ? scaleV : scaleH;
			imgv.setFitHeight(img.getHeight() * scaleFactor);
			imgv.setFitWidth(img.getWidth() * scaleFactor);
			goc.bg.getChildren().add(imgv);
			Stage stage = (Stage) gbc.accept.getScene().getWindow();	
			if(stage!= null) {
				stage.setScene(gameOverScene);
				stage.show();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}

class JoinedFinalTournamentTask extends Task{

	private int[] players;
	public JoinedFinalTournamentTask(GameBoardController gbc, int[] players) {
		super(gbc);
		this.players = players;
	}
	@Override
	public void run() {
		gbc.clearToast();
		if(players.length == 1) {
			gbc.showToast("Player: " + Arrays.toString(players) + " has won the game!");	
		} else {
			gbc.showToast("Players: " + Arrays.toString(players) + " has joined the final tournament!");
		}
		gbc.setButtonsInvisible();
		gbc.showStartTurn();
	}
}

abstract class Task implements Runnable{
	protected File cardDir;
	protected GameBoardController gbc;
	protected GameOverController goc;
	protected static boolean[] winners = {false,false,false,false};
	public Task() { };
	public Task(GameBoardController gbc) {
		this.gbc = gbc;
		cardDir = new File("src/main/resources/"); 
	}
}

public class Client implements Runnable {

	final static Logger logger = LogManager.getLogger(Client.class);

	private Gson gson = new Gson();
	private JsonParser json = new JsonParser();
	private LinkedBlockingQueue<String> clientOutput;
	private LinkedBlockingQueue<String> serverOutput;

	private String currentMessage;

	GameBoardController gbc;
	GameOverController goc;
	public Client(LinkedBlockingQueue<String> clientOutput, LinkedBlockingQueue<String> serverOutput) {
		this.clientOutput = clientOutput;
		this.serverOutput = serverOutput;
	}

	public void setGameBoardController(GameBoardController gbc) {
		logger.info("Set reference to GBC: " + gbc);
		this.gbc = gbc;
	}

	@Override
	public void run() {
		try {
			ArrayList<DiscardFaceUpTask> toDiscard = new ArrayList<DiscardFaceUpTask>();
			while(true) {
				currentMessage = serverOutput.take();
				logger.info("Message received: " + currentMessage);
				JsonObject obj = json.parse(currentMessage).getAsJsonObject();
				String message = obj.get("message").getAsString();
				if(message.equals(MESSAGETYPES.ADDCARDS.name())) {
					AddCardsServer request = gson.fromJson(obj, AddCardsServer.class);
					Platform.runLater(new AddCardsTask(gbc, request.player, request.cards));
				}
				if(message.equals(MESSAGETYPES.TURNNEXT.name())) {
					TurnNextServer request = gson.fromJson(obj, TurnNextServer.class);
					toDiscard.forEach(i -> Platform.runLater(i));
					toDiscard.clear();
					Platform.runLater(new TurnNextTask(gbc, request.player));
					synchronized (this) {
						try {
							Platform.runLater(new Runnable(){
								@Override
								public void run(){
									gbc.currBP.setText("");
									IntStream.range(0, gbc.bpTexts.length).forEach(i -> gbc.bpTexts[i].setText(""));
									gbc.clearToast();
									gbc.clearHighlight();
									gbc.showToast("Player #: " + (request.player + 1) + " turn");
									gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
									gbc.setButtonsInvisible();
									gbc.startTurn.setVisible(true);
									gbc.startTurn.setText("Start Turn");
									gbc.CURRENT_STATE = GAME_STATE.CHILLING;
								}
							});
							this.wait();
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
									cards.addAll(gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getFaceUp().getDeck());
									cards.addAll(gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getFaceDownDeck().getDeck());
									gbc.c.send(new CalculatePlayerClient(gbc.playerManager.getCurrentPlayer(), cards.stream().map(i -> i.getName()).toArray(size -> new String[size])));
								}
							});
							this.gbc.setMerlinMordredVisibility();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(message.equals(MESSAGETYPES.CALCULATEPLAYER.name())) {
					CalculatePlayerServer cps = gson.fromJson(obj, CalculatePlayerServer.class);
					Platform.runLater(new UpdateBattlePointTask(gbc, cps.player, cps.points));
				}
				if(message.equals(MESSAGETYPES.CALCULATESTAGE.name())) {
					CalculateStageServer csc = gson.fromJson(obj, CalculateStageServer.class);
					Platform.runLater(new UpdateStageBattlePointTask(gbc, csc.player, csc.points, csc.stage));
				}
				if(message.equals(MESSAGETYPES.SHOWMIDDLECARD.name())) {
					MiddleCardServer request = gson.fromJson(obj, MiddleCardServer.class);
					Platform.runLater(new MiddleCardTask(gbc, request.card));
				}
				if(message.equals(MESSAGETYPES.JOINTOURNAMENT.name())) {
					TournamentAcceptDeclineServer request = gson.fromJson(obj, TournamentAcceptDeclineServer.class);

					Platform.runLater(new JoinTournamentTask(gbc, request.player));
					//						Platform.runLater(new ShowAcceptDeclineTask(gbc, request.player));
				}
				if(message.equals(MESSAGETYPES.PICKTOURNAMENT.name())) {
					TournamentPickCardsServer request = gson.fromJson(obj, TournamentPickCardsServer.class);
					Platform.runLater(new PickTournamentTask(gbc, request.player));
					//						Platform.runLater(new ShowEndTurn(gbc, request.player));
				}
				if(message.equals(MESSAGETYPES.UPQUEST.name())) {
					QuestUpServer request = gson.fromJson(obj, QuestUpServer.class);
					Platform.runLater(new UpQuestTask(gbc, request.cards, request.player, request.stage));
				}
				if(message.equals(MESSAGETYPES.RANKUPDATE.name())) {
					RankServer request = gson.fromJson(obj, RankServer.class);
					Platform.runLater(new SetRankTask(gbc, request.newrank, request.player));
				}
				if(message.equals(MESSAGETYPES.WINTOURNAMENT.name())) {
					TournamentWinServer request = gson.fromJson(obj, TournamentWinServer.class);
					Platform.runLater(new TournamentWonTask(gbc, request.players));
					Platform.runLater(new RevealAllCards(gbc));
					synchronized (this) {
						try {
							Platform.runLater(new Runnable(){
								@Override
								public void run(){
									gbc.setButtonsInvisible();
									gbc.showStartTurn();
									gbc.startTurn.setText("Continue");
									gbc.CURRENT_STATE = GAME_STATE.CHILLING;
								}
							});
							this.wait();
							this.send(new ContinueGameClient());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(message.equals(MESSAGETYPES.TIETOURNAMENT.name())) {
					QuestPassStageServer qpss = gson.fromJson(obj, QuestPassStageServer.class);
					int[] players = qpss.players;
					Platform.runLater(new RevealAllCards(gbc));
					synchronized (this) {
						try {
							Platform.runLater(new Runnable(){
								@Override
								public void run(){
									gbc.clearToast();
									gbc.showToast("Players #: " + Arrays.stream(players).boxed().map(i -> (i + 1) + "").collect(Collectors.joining(",")) + " tied the tournament");
									gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
									gbc.setButtonsInvisible();
									gbc.startTurn.setVisible(true);
									gbc.startTurn.setText("Continue");
									gbc.CURRENT_STATE = GAME_STATE.CHILLING;
								}
							});
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(message.equals(MESSAGETYPES.CONTINUEGAME.name())) {
					synchronized (this) {
						ContinueGameServer cgs = gson.fromJson(obj, ContinueGameServer.class);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								gbc.stageCards.forEach(i -> i.clear());
								for(Pane p: gbc.stages) {
									p.getChildren().clear();
								}

								for(ImageView v: gbc.stageViews) {
									v.setVisible(false);
								}

								gbc.setButtonsInvisible();
								gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
								gbc.setButtonsInvisible();
								gbc.startTurn.setVisible(true);
								gbc.startTurn.setText("Continue");
								gbc.clearToast();
								gbc.showToast(cgs.messageText);
								gbc.CURRENT_STATE = GAME_STATE.CHILLING;
							}
						});
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.send(new ContinueGameClient());
					}
				}
				if(message.equals(MESSAGETYPES.EVENTDISCARD.name())) {
					EventDiscardCardsServer request = gson.fromJson(obj, EventDiscardCardsServer.class);

					//Discard "Face Down" cards because that is where players play their cards.
					Platform.runLater(new EventDiscardTask(gbc, request.player, request.type, request.amount));
				}
				/*
				 * Dealing with Quest state
				 */
				if(message.equals(MESSAGETYPES.SPONSERQUESTCANT.name())) {
					QuestSponsorServerCant request = gson.fromJson(obj, QuestSponsorServerCant.class);
					Platform.runLater(new QuestSponsorTaskCant(gbc, request.player));
				}
				if(message.equals(MESSAGETYPES.PASSALL.name())) {
					QuestPassAllServer qpss = gson.fromJson(obj, QuestPassAllServer.class);
					int[] players = qpss.players;
					synchronized (this) {
						try {
							Platform.runLater(new Runnable(){
								@Override
								public void run(){
									gbc.clearToast();
									if(players.length == 1) {
										gbc.showToast("Player #: " + players[0] + " passed the quest");
									} else if (players.length == 0) {
										gbc.showToast("No players passed the quest");
									} else {
										gbc.showToast("Players #: " + Arrays.stream(players).boxed().map(i -> i + "").collect(Collectors.joining(",")) + " passed the quest");
									}
									gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
									gbc.setButtonsInvisible();

									gbc.startTurn.setVisible(true);
									gbc.startTurn.setText("Continue");
									gbc.CURRENT_STATE = GAME_STATE.CHILLING;
								}
							});
							this.wait();
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									gbc.stageCards.forEach(i -> i.clear());
									for(Pane p: gbc.stages) {
										p.getChildren().clear();
									}
									
									for(ImageView v: gbc.stageViews) {
										v.setVisible(false);
									}
								}
							});
							this.send(new ContinueGameClient());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(message.equals(MESSAGETYPES.PASSSTAGE.name())) {
					QuestPassStageServer qpss = gson.fromJson(obj, QuestPassStageServer.class);
					int[] players = qpss.players;
					Platform.runLater(new RevealAllCards(gbc));
					synchronized (this) {
						try {
							Platform.runLater(new Runnable(){
								@Override
								public void run(){
									gbc.clearToast();
									if(players.length == 1) {
										gbc.showToast("Player #: " + players[0] + " passed the stage");
									} else if (players.length == 0) {
										gbc.showToast("No players passed the stage");
									} else {
										gbc.showToast("Players #: " + Arrays.stream(players).boxed().map(i -> i + "").collect(Collectors.joining(",")) + " passed");
									}
									gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
									gbc.setButtonsInvisible();
									gbc.startTurn.setVisible(true);
									gbc.startTurn.setText("Continue");
									gbc.CURRENT_STATE = GAME_STATE.CHILLING;
								}
							});
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(message.equals(MESSAGETYPES.SPONSERQUEST.name())) {
					QuestSponsorServer request = gson.fromJson(obj, QuestSponsorServer.class);
					Platform.runLater(new QuestSponsorTask(gbc, request.player));
				}
				if(message.equals(MESSAGETYPES.PICKSTAGES.name())) {
					QuestPickStagesServer request = gson.fromJson(obj, QuestPickStagesServer.class);
					Platform.runLater(new QuestPickStagesTask(gbc, request.player, request.numStages));
					for(int i = 0; i < gbc.stages.length; i++) {
						if(gbc.stages[i].isVisible()) {
							this.send(new CalculateStageClient(gbc.playerManager.getCurrentPlayer(),gbc.stageCards.get(i).stream().map(j -> j.getName()).toArray(String[]::new), i));	
						}
					}
				}
				if(message.equals(MESSAGETYPES.JOINQUEST.name())) {
					QuestJoinServer request = gson.fromJson(obj, QuestJoinServer.class);
					Platform.runLater(new QuestJoinTask(gbc, request.player));
				}					
				if(message.equals(MESSAGETYPES.PICKQUEST.name())) {
					QuestPickCardsServer request = gson.fromJson(obj, QuestPickCardsServer.class);
					toDiscard.forEach(i -> Platform.runLater(i));
					toDiscard.clear();
					Platform.runLater(new QuestPickCardsTask(gbc, request.player));
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
							cards.addAll(gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getFaceUp().getDeck());
							cards.addAll(gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getFaceDownDeck().getDeck());
							gbc.c.send(new CalculatePlayerClient(gbc.playerManager.getCurrentPlayer(), cards.stream().map(i -> i.getName()).toArray(size -> new String[size])));
						}
					});
				}
				if(message.equals(MESSAGETYPES.FACEDOWNCARDS.name())) {
					FaceDownServer request = gson.fromJson(obj, FaceDownServer.class);

					Platform.runLater(new FaceDownCardsTask(gbc, request.player));
				}
				if(message.equals(MESSAGETYPES.DISCARDFACEUP.name())) {
					FaceUpDiscardServer request = gson.fromJson(obj, FaceUpDiscardServer.class);

					//Discard "Face Down" cards because that is where players play their cards.
					//Platform.runLater(new DiscardFaceDownTask(gbc, request.player, request.cardsDiscarded));
					toDiscard.add(new DiscardFaceUpTask(gbc, request.player, request.cardsDiscarded));
				}
				if(message.equals(MESSAGETYPES.SHIELDCOUNT.name())) {
					ShieldCountServer request = gson.fromJson(obj, ShieldCountServer.class);

					//Discard "Face Down" cards because that is where players play their cards.
					Platform.runLater(new ShieldCountTask(gbc, request.player, request.shields));
				}
				if(message.equals(MESSAGETYPES.BIDQUEST.name())) {
					QuestBidServer request = gson.fromJson(obj, QuestBidServer.class);

					//Discard "Face Down" cards because that is where players play their cards.
					Platform.runLater(new QuestBidTask(gbc, request.player, request.minBidValue, request.maxBidValue));
				}
				if(message.equals(MESSAGETYPES.DISCARDQUEST.name())) {
					QuestDiscardCardsServer request = gson.fromJson(obj, QuestDiscardCardsServer.class);

					//Discard "Face Down" cards because that is where players play their cards.
					Platform.runLater(new DiscardQuestTask(gbc, request.player, request.cardsToDiscard));
				}
				if(message.equals(MESSAGETYPES.DISCARDHANDFULL.name())){
					HandFullServer request = gson.fromJson(obj, HandFullServer.class);

					Platform.runLater(new HandFullDiscardTask(gbc, request.player));
				}
				if(message.equals(MESSAGETYPES.GAMEOVER.name())){
					GameOverServer request = gson.fromJson(obj, GameOverServer.class);
					Platform.runLater(new RevealAllCards(gbc));
					if(request.players.length != 1) {
						synchronized (this) {
							try {
								Platform.runLater(new Runnable(){
									@Override
									public void run(){
										gbc.setButtonsInvisible();
										gbc.showStartTurn();
										gbc.clearToast();
										gbc.showToast("Results for final tournament");
										gbc.startTurn.setText("Continue");
										gbc.CURRENT_STATE = GAME_STATE.CHILLING;
									}
							});
								this.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					Platform.runLater(new GameOverTask(gbc, request.players));
				}
				if(message.equals(MESSAGETYPES.JOINEDFINALTOURNAMENT.name())){
					FinalTournamentNotifyServer request = gson.fromJson(obj, FinalTournamentNotifyServer.class);

					//						Platform.runLater(new JoinedFinalTournamentTask(gbc, request.player));
					synchronized (this) {
						try {
							Platform.runLater(new JoinedFinalTournamentTask(gbc, request.players));
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (InterruptedException e1) {
			logger.info("Closing game");
		}
	}

	public void send(Message message) {
		logger.info("Sending message: " + message);
		try {
			clientOutput.put(gson.toJson(message));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}