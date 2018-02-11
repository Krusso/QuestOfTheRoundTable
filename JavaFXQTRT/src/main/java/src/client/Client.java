package src.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import org.junit.experimental.theories.internal.SpecificDataPointsSupplier;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import src.client.GameBoardController.STATE;
import src.game_logic.AdventureCard;
import src.game_logic.Rank;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;
import src.game_logic.WeaponCard;
import src.game_logic.FoeCard;
import src.game_logic.TestCard;
import src.messages.Message;
import src.messages.Message.MESSAGETYPES;
import src.messages.game.MiddleCardServer;
import src.messages.game.ShieldCountServer;
import src.messages.game.TurnNextServer;
import src.messages.hand.AddCardsServer;
import src.messages.hand.FaceDownServer;
import src.messages.hand.FaceUpDiscardServer;
import src.messages.quest.QuestUpServer;
import src.messages.rank.RankServer;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.messages.tournament.TournamentAcceptDeclineServer;
import src.messages.tournament.TournamentPickCardsServer;
import src.messages.tournament.TournamentWinServer;
import src.messages.quest.*;

class AddCardsTask extends Task{
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
			//find file associated to name
			for(File f : list) {
				if (f.getName().contains(card+".jpg")) {
					switch (f.getName().charAt(0)) {
					case 'A':{
						AdventureCard c = new AdventureCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.jpg");
						c.faceDown();
						gbc.addCardToHand(c, player);
						break;
					}
					case 'F' : {
						FoeCard c = new FoeCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.jpg");
						gbc.addCardToHand(c, player);
						c.faceDown();
						break;
					}
					case 'T' : {
						TestCard c = new TestCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.jpg");
						gbc.addCardToHand(c, player);
						c.faceDown();
						break;
					}
					case 'W':{
						WeaponCard weapon = new WeaponCard(card, f.getPath());
						weapon.setCardBack(cardDir.getPath() + "/Adventure Back.jpg");
						gbc.addCardToHand(weapon, player);
						weapon.faceDown();
						break;
					}
					default:{
						break;
					}
					}
				}
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
		gbc.showAcceptDecline();
	}
}

class TournamentWonTask extends Task{
	private int player;
	public TournamentWonTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}

	@Override
	public void run() {
		winners[player] = true;

		String display = "Player(s) ";
		for(int i = 0 ; i < winners.length; i++) {

			System.out.println("pnum " + i + " winners: " + winners[i]) ;
			if(winners[i] == true) {
				display = display + i + ", ";
			}
			System.out.println(display);
		}
		display = display.substring(0, display.length()-2);
		display = display + " won the tournmanet!";
		gbc.toast.setText(display);
		gbc.toast.setVisible(true);

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

class ShowAcceptDeclineTask extends Task{
	private int player;
	public ShowAcceptDeclineTask(GameBoardController gbc, int player) {
		super(gbc);
		this.player = player;
	}

	// no msg expected
	@Override
	public void run() {
		System.out.println("Processing msg: accept/decline tournament");
		gbc.CURRENT_STATE = STATE.JOIN_TOURNAMENT;
		gbc.showAcceptDecline();
	}
}

class ShowTurnFaceDownFieldUp extends Task{
	private int player;
	private String[][] cards;
	public ShowTurnFaceDownFieldUp(GameBoardController gbc, String[][] cards, int player) {
		super(gbc);
		this.player = player;
		this.cards = cards;
	}

	@Override
	public void run() {
		gbc.flipFaceDownPane(player, true);

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
		gbc.showEndTurn();
		gbc.addStagePaneListener();
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
		gbc.addDraggable();
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
		gbc.addFaceDownPaneDragOver();
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
		gbc.flipStageCards(this.stage, true);
	}
}
class DiscardFaceDownTask extends Task {

	private String[] cardsToDiscard;
	private int player;
	public DiscardFaceDownTask(GameBoardController gbc, int player, String[] cardsDiscarded) {
		super(gbc);
		this.player = player;
		this.cardsToDiscard = cardsDiscarded;
		
	}
	@Override
	public void run() {
		gbc.CURRENT_STATE = STATE.DISCARDING_CARDS;
		gbc.discardFaceDownCards(player,cardsToDiscard);
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
//		gbc.CURRENT_STATE = STATE.DISCARDING_CARDS; //may add a statae for shields
		gbc.addShields(player, shields);
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
						Platform.runLater(new TurnNextTask(gbc, request.player));
					}
					if(message.equals(MESSAGETYPES.SHOWMIDDLECARD.name())) {
						MiddleCardServer request = gson.fromJson(obj, MiddleCardServer.class);
						Platform.runLater(new MiddleCardTask(gbc, request.card));
					}
					if(message.equals(MESSAGETYPES.JOINTOURNAMENT.name())) {
						TournamentAcceptDeclineServer request = gson.fromJson(obj, TournamentAcceptDeclineServer.class);
						Platform.runLater(new ShowAcceptDeclineTask(gbc, request.player));
					}
					if(message.equals(MESSAGETYPES.PICKTOURNAMENT.name())) {
						TournamentPickCardsServer request = gson.fromJson(obj, TournamentPickCardsServer.class);
						Platform.runLater(new ShowEndTurn(gbc, request.player));
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
						Platform.runLater(new TournamentWonTask(gbc, request.player));
					}
					/*
					 * Dealing with Quest state
					 */
					if(message.equals(MESSAGETYPES.SPONSERQUEST.name())) {
						QuestSponsorServer request = gson.fromJson(obj, QuestSponsorServer.class);
						Platform.runLater(new QuestSponsorTask(gbc, request.player));
					}
					if(message.equals(MESSAGETYPES.PICKSTAGES.name())) {
						QuestPickStagesServer request = gson.fromJson(obj, QuestPickStagesServer.class);
						Platform.runLater(new QuestPickStagesTask(gbc, request.player, request.numStages));
					}
					if(message.equals(MESSAGETYPES.JOINQUEST.name())) {
						QuestJoinServer request = gson.fromJson(obj, QuestJoinServer.class);
						Platform.runLater(new QuestJoinTask(gbc, request.player));
					}					
					if(message.equals(MESSAGETYPES.PICKQUEST.name())) {
						QuestPickCardsServer request = gson.fromJson(obj, QuestPickCardsServer.class);
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
						Platform.runLater(new DiscardFaceDownTask(gbc, request.player, request.cardsDiscarded));
					}
					if(message.equals(MESSAGETYPES.SHIELDCOUNT.name())) {
						ShieldCountServer request = gson.fromJson(obj, ShieldCountServer.class);
						
						//Discard "Face Down" cards because that is where players play their cards.
						Platform.runLater(new ShieldCountTask(gbc, request.player, request.shields));
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
