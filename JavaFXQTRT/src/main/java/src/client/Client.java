package src.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import src.client.GameBoardController;
import src.game_logic.AdventureCard;
import src.game_logic.EventCard;
import src.game_logic.Rank;
import src.game_logic.StoryCard;
import src.game_logic.TournamentCard;
import src.game_logic.WeaponCard;


class Task implements Runnable{
	private File cardDir;
	private GameBoardController gbc;
	private String typeOfTask;
	private String msg;
	Task(String typeOfTask, String msg, GameBoardController gbc) {
		this.msg = msg;
		this.typeOfTask = typeOfTask;
		this.gbc = gbc;
		cardDir = new File("src/main/resources/"); 
	}
	@Override
	public void run() {
		switch(typeOfTask) {
		case "add cards" :{
			displayHand(msg);
			break;
		}
		case "turn next" :{
			nextTurn(msg);
			break;
		}
		case "middle card" :{
			middleCard(msg);
			break;
		}
		case "tournament accept":{
			showAcceptDecline();
			break;
		}
		case "tournament picking":{
			showEndTurn();
			break;
		}
		case "quest up": {
			turnFaceDownFieldUp();
		}
		case "rank set": {
			setRank();
		}
		}
	}
	private void setRank() {
		int playerNum = msg.charAt("rank set: player ".length()) - '0';
		String[] splits = msg.split(" ");
		String rank = splits[splits.length-1];
		Rank.RANKS r = Rank.RANKS.SQUIRE;
//		if(rank.equals("SQUIRE")) r = Rank.RANKS.SQUIRE;
		if(rank.equals("KNIGHT")) r = Rank.RANKS.KNIGHT;
		if(rank.equals("CHAMPION")) r = Rank.RANKS.CHAMPION;
		if(rank.equals("KNIGHTOFTHEROUNDTABLE")) r = Rank.RANKS.KNIGHTOFTHEROUNDTABLE;
		gbc.setPlayerRank(playerNum, r);
		
	}

	// no msg expected
	private void showEndTurn() {
		System.out.println("Processing msg: pick card tournament");
		gbc.showEndTurn();
		gbc.addDraggable();
	}
	
	// no msg expected
	private void showAcceptDecline() {
		System.out.println("Processing msg: accept/decline tournament");
		gbc.showAcceptDecline();
	}
	
	private void displayHand(String msg) {
		System.out.println("Processing msg: "+msg);
		File[] list = cardDir.listFiles();
		int playerNumber = Integer.parseInt(msg.charAt(7) + "");
		String[] hand = msg.substring("player # [".length(), msg.length()-1).split(", ");
		for(String card: hand) {
			//find file associated to name
			for(File f : list) {
				if (f.getName().contains(card+".jpg")) {
					//					System.out.println("Adding image: ("+ f.getName()+") to hand found in " + f.getPath());
					switch (f.getName().charAt(0)) {
					case 'A':{
						AdventureCard c = new AdventureCard(card, f.getPath());
						c.setCardBack(cardDir.getPath() + "/Adventure Back.jpg");
						gbc.addCardToHand(c, playerNumber);
						break;
					}
					//						case 'E':{
					//							gbc.addCardToHand(new EventCard(card, f.getPath()),playerNumber);
					//							break;
					//						}
					//						case 'T':{
					//							gbc.addCardToHand(new TournamentCard(card, f.getPath()),playerNumber);
					//							break;
					//						}
					case 'W':{
						AdventureCard weapon = new WeaponCard(card, f.getPath());
						weapon.setCardBack(cardDir.getPath() + "/Adventure Back.jpg");
						gbc.addCardToHand(weapon, playerNumber);
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
	
	private void turnFaceDownFieldUp() {
		int p = msg.charAt(msg.indexOf("player") + "player ".length()) - '0';
		gbc.showFaceDownFieldCards(p);
		
	}

	//Msg should be a string with a number which indicates which players has the turn
	private void nextTurn(String msg) {
		int playerTurn = msg.charAt(msg.length()-1) - '0';
		System.out.println("Processing msg: " + msg );
		gbc.clearPlayField();
		gbc.setPlayerTurn(playerTurn);
		gbc.showPlayerHand(playerTurn);
		
	}

	//Msg should be the name of the card
	private void middleCard(String msg) {
		System.out.println("Processing msg: middle card:" + msg);
		//find story card
		File[] list = cardDir.listFiles();
		for(File c : list) {
			if(c.getName().contains(msg)) {
				StoryCard sc= new StoryCard(msg, c.getPath());
				gbc.setStoryCard(sc);
				System.out.println("Set story card to:" + sc.getName());
			}
		}
	}
}

public class Client implements Runnable {

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
			String message;

			while(client.isConnected()) {
				if(readStream.ready()) {
					currentMessage = readStream.readLine();
					System.out.println("Messsage received: " + currentMessage);

					if(currentMessage.startsWith("add cards: ")) {
						Platform.runLater(new Task("add cards", currentMessage.substring(currentMessage.indexOf("player")), gbc));
					}
					if(currentMessage.startsWith("turn next:")) {
						Platform.runLater(new Task("turn next", currentMessage.substring(currentMessage.indexOf("turn next:")), gbc));
					}
					if(currentMessage.startsWith("middle card:")) {
						String cardName = currentMessage.substring("middle card: ".length());
						Platform.runLater(new Task("middle card", cardName, gbc));
					}
					if(currentMessage.startsWith("tournament accept: player")) {
						Platform.runLater(new Task("tournament accept","",gbc));
					}
					if(currentMessage.startsWith("tournament picking: player")) {
						Platform.runLater(new Task("tournament picking","",gbc));
					}
					if(currentMessage.startsWith("quest up:")) {
						Platform.runLater(new Task("quest up", currentMessage, gbc));
					}
					if(currentMessage.startsWith("rank set:")) {
						Platform.runLater(new Task("rank set", currentMessage, gbc));
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

	public void send(String message) {
		System.out.println("Sending message: " + message);
		writeStream.println(message);
	}
}
