package src.client;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import src.client.GameBoardController.STATE;
import src.game_logic.AdventureCard;
import src.game_logic.AllyCard;
import src.game_logic.FoeCard;
import src.game_logic.QuestCard;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;
import src.game_logic.TestCard;
import src.game_logic.WeaponCard;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.game.CalculatePlayerClient;
import src.messages.game.CalculatePlayerServer;
import src.messages.game.CalculateStageClient;
import src.messages.game.CalculateStageServer;
import src.messages.game.MiddleCardServer;
import src.messages.game.ShieldCountServer;
import src.messages.game.TurnNextServer;
import src.messages.hand.AddCardsServer;
import src.messages.hand.FaceDownServer;
import src.messages.hand.FaceUpDiscardServer;
import src.messages.quest.QuestBidServer;
import src.messages.quest.QuestDiscardCardsServer;
import src.messages.quest.QuestJoinServer;
import src.messages.quest.QuestPassAllServer;
import src.messages.quest.QuestPassStageServer;
import src.messages.quest.QuestPickCardsServer;
import src.messages.quest.QuestPickStagesServer;
import src.messages.quest.QuestSponsorServer;
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
			boolean didAddCard = false;;
			//find file associated to name
			for(File f : list) {
				if ((f.getName().contains(card+".png") || f.getName().contains(card+".jpg")) && 
						((f.getName().length()-6) == card.length() || (f.getName().length()-4) == card.length())) {
					switch (f.getName().charAt(0)) {
					case 'A':{
						AllyCard c = new AllyCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						c.faceDown();
						gbc.addCardToHand(c, player);
						didAddCard = true;
						break;
					}
					case 'F' : {
						FoeCard c = new FoeCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						gbc.addCardToHand(c, player);
						c.faceDown();
						didAddCard = true;
						break;
					}
					case 'T' : {
						TestCard c = new TestCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						gbc.addCardToHand(c, player);
						c.faceDown();
						didAddCard = true;
						break;
					}
					case 'W':{
						WeaponCard weapon = new WeaponCard(card, f.getPath());
						weapon.setCardBack(cardDir.getPath() + "/Adventure Back.png");
						gbc.addCardToHand(weapon, player);
						weapon.faceDown();
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
	private String card;
	public MiddleCardTask(GameBoardController gbc, String card) {
		super(gbc);
		this.card = card;
	}

	//Msg should be the name of the card
	@Override
	public void run() {
		System.out.println("Processing msg: middle card:" + card);
		//find story card
		File[] list = cardDir.listFiles();
		//		System.out.println("Finding " + card + " card");
		for(File c : list) {
			if(c.getName().contains(card)) {
				StoryCard sc= new StoryCard(card, c.getPath());
				gbc.setStoryCard(sc);
				System.out.println("Set story card to:" + sc.getName());
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
		gbc.CURRENT_STATE = STATE.SPONSOR_QUEST;
		gbc.setButtonsInvisible();
		gbc.setPlayerPerspectiveTo(player);
		gbc.showAcceptDecline();
		gbc.showToast("Sponsor Quest?");
		if(gbc.playerManager.getAI(player) != null) {
			if(gbc.playerManager.getAI(player).doISponserAQuest(gbc.questCard) != null) {
				gbc.accept.fire();
			} else {
				gbc.decline.fire();
			}
		}
	}
}

class TournamentWonTask extends Task{
	private int[] players;
	public TournamentWonTask(GameBoardController gbc, int[] players) {
		super(gbc);
		this.players = players;
	}

	@Override
	public void run() {
		for(int i = 0 ; i < players.length;i++) {
			winners[players[i]] = true;
		}

		String display = "";
		for(int i = 0 ; i < winners.length; i++) {

			System.out.println("pnum " + i + " winners: " + winners[i]) ;
			if(winners[i] == true) {
				display = display + i + ", ";
			}
			System.out.println(display);
		}
		display = display.substring(0, display.length()-2);
		// sorry this was triggering me
		if(display.length()>1) {
			display = "Players " + display + " won the tournament!";
		} else { 
			display = "Player " + display + " won the tournament!";
		}
		gbc.clearToast();
		gbc.showToast(display);
		//		gbc.toast.setText(display);
		//		gbc.toast.setVisible(true);
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
	public ShowEndTurn(GameBoardController gbc, int player) {
		super(gbc);
	}

	// no msg expected
	@Override
	public void run() {
		System.out.println("Processing msg: pick card tournament");
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
		gbc.CURRENT_STATE = STATE.PICK_STAGES;
		gbc.setPickStageOn(numStages);
		gbc.addDraggable();
		gbc.setPlayerPerspectiveTo(player);
		gbc.showEndTurn();
		gbc.addStagePaneListener();
		gbc.setQuestStageBanners(numStages);
		gbc.clearToast();
		gbc.showToast("Select cards for each Stage");
		if(gbc.playerManager.getAI(player) != null) {
			List<List<AdventureCard>> cards = gbc.playerManager.getAI(player).doISponserAQuest(gbc.questCard);
			for(int i = 0; i < cards.size(); i++) {
				for(int j = 0; j < cards.get(i).size(); j++) {
					gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.stages[i], cards.get(i).get(j));	
				}
			}
			gbc.endTurn.fire();
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
		gbc.CURRENT_STATE = STATE.JOIN_QUEST;
		gbc.showAcceptDecline();
		gbc.setPlayerPerspectiveTo(player);
		gbc.addDraggable();
		gbc.clearToast();
		gbc.showToast("Join Quest?");
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
		gbc.CURRENT_STATE = STATE.QUEST_PICK_CARDS;
		gbc.setPlayerPerspectiveTo(player);
		gbc.showEndTurn();
		gbc.addDraggable();
		gbc.removeStagePaneDragOver();
		gbc.clearHighlight();
		gbc.highlightFaceUp(player);
		gbc.clearToast();
		gbc.showToast("Select Cards for current stage");
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cards = gbc.playerManager.getAI(player).playCardsForFoeQuest(gbc.questCard.getNumStages() == gbc.currentStage + 1, 
					gbc.questCard);
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
		gbc.CURRENT_STATE = STATE.FACE_DOWN_CARDS;
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
		gbc.CURRENT_STATE = STATE.UP_QUEST;
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
		gbc.CURRENT_STATE = STATE.DISCARDING_CARDS;
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
			gbc.CURRENT_STATE = STATE.QUEST_BID;
			gbc.bidSlider.setMin(min);
			gbc.bidSlider.setMax(max);
			gbc.bidSlider.setVisible(true);
			gbc.bidSlider.setMajorTickUnit(2);
			gbc.bidSlider.setShowTickLabels(true);
			gbc.bidSlider.setBlockIncrement(1);
			gbc.bidSlider.setSnapToTicks(true);
			gbc.clearToast();
			gbc.showToast("Use the slider to enter how many cards you want to bid.");
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

class DiscardQuestTask extends Task {
	private int player;
	public DiscardQuestTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;

	}
	@Override
	public void run() {
		//the task that is run before this is the bidquest so we have to hide the bidslider now
		gbc.bidSlider.setVisible(false);
		gbc.CURRENT_STATE = STATE.BID_DISCARD;
		gbc.setButtonsInvisible();
		gbc.showEndTurn();
		gbc.setPlayerPerspectiveTo(player);
		gbc.addDraggable();
		gbc.removeStagePaneDragOver();
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cards = gbc.playerManager.getAI(player).discardAfterWinningTest();
			cards.forEach(i -> gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.faceDownPanes[player], i));
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
		gbc.CURRENT_STATE = STATE.JOIN_TOURNAMENT;
		gbc.setButtonsInvisible();
		gbc.showAcceptDecline();
		gbc.setPlayerPerspectiveTo(player);
		gbc.removeStagePaneDragOver();
		gbc.clearToast();
		gbc.showToast("Join Tournament?");
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
		gbc.CURRENT_STATE = STATE.PICK_TOURNAMENT;
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
		if(gbc.playerManager.getAI(player) != null) {
			List<AdventureCard> cardsToPlay = gbc.playerManager.getAI(player).playCardsForTournament();
			cardsToPlay.forEach(i -> gbc.moveCardBetweenPanes(gbc.handPanes[player], gbc.faceDownPanes[player], i));
			gbc.endTurn.fire();
		}
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


abstract class Task implements Runnable{
	protected File cardDir;
	protected GameBoardController gbc;
	protected static boolean[] winners = {false,false,false,false};
	public Task() { };
	public Task(GameBoardController gbc) {
		this.gbc = gbc;
		cardDir = new File("src/main/resources/"); 
	}
}

public class Client implements Runnable {

	private Gson gson = new Gson();
	private JsonParser json = new JsonParser();
	private File cardDir;
	private String host;
	private int port;
	Socket client;
	PrintStream writeStream;
	BufferedReader readStream;
	private String currentMessage;

	GameBoardController gbc;
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void setGameBoardController(GameBoardController gbc) {
		System.out.println("referenced GBC");
		this.gbc = gbc;
	}

	@Override
	public void run() {
		try {
			cardDir = new File("src/main/resources/");

			client = new Socket(host, port);
			writeStream = new PrintStream(client.getOutputStream());
			readStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
			ArrayList<DiscardFaceUpTask> toDiscard = new ArrayList<DiscardFaceUpTask>();
			while(client.isConnected()) {
				if(readStream.ready()) {
					currentMessage = readStream.readLine();
					System.out.println("Messsage received: " + currentMessage);
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
										gbc.showToast("Player #: " + request.player + " turn");
										gbc.playerManager.faceDownPlayerHand(gbc.playerManager.getCurrentPlayer());
										gbc.setButtonsInvisible();
										gbc.startTurn.setVisible(true);
										gbc.startTurn.setText("Start Turn");
										gbc.CURRENT_STATE = STATE.CHILLING;
									}
								});
								this.wait();
								ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
								cards.addAll(gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getFaceUp().getDeck());
								cards.addAll(gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getFaceDownDeck().getDeck());
								this.send(new CalculatePlayerClient(this.gbc.playerManager.getCurrentPlayer(), cards.stream().map(i -> i.getName()).toArray(size -> new String[size])));
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
										gbc.CURRENT_STATE = STATE.CHILLING;
									}
								});
								this.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					/*
					 * Dealing with Quest state
					 */
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
										gbc.CURRENT_STATE = STATE.CHILLING;
									}
								});
								this.wait();
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
										gbc.CURRENT_STATE = STATE.CHILLING;
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
					}
					if(message.equals(MESSAGETYPES.FACEDOWNCARDS.name())) {
						//						QuestPickCardsServer request = gson.fromJson(obj, QuestPickCardsServer.class);
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
						Platform.runLater(new DiscardQuestTask(gbc, request.player));
					}
				}
			}
			if(!client.isConnected()) {
				System.out.println("Client disconnected!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(Message message) {
		System.out.println("Sending message: " + message);
		writeStream.println(gson.toJson(message));
	}
}