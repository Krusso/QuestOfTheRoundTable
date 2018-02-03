package src.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javafx.application.Platform;
import src.client.GameBoardController;
import src.game_logic.AdventureCard;
import src.game_logic.EventCard;
import src.game_logic.TournamentCard;
import src.game_logic.WeaponCard;
//class OneShotTask implements Runnable {
//    String str;
//    OneShotTask(String s) { str = s; }
//    public void run() {
//        someFunc(str);
//    }
//}
//Thread t = new Thread(new OneShotTask(str));
//t.start();

class PerformTask implements Runnable{
	private File cardDir;
	private GameBoardController gbc;
	private String typeOfTask;
	private String msg;
	PerformTask(String typeOfTask, String msg, GameBoardController gbc) {
		this.msg = msg;
		this.typeOfTask = typeOfTask;
		this.gbc = gbc;
		cardDir = new File("src/main/resources/"); 
	}
	@Override
	public void run() {
		switch(typeOfTask) {
		
		}
	}
	
	private void displayHand(String msg) {
		System.out.println("Processing msg: "+msg);
		File[] list = cardDir.listFiles();
		String[] hand = msg.substring("player # [".length(), msg.length()-1).split(", ");
		for(String card: hand) {
			//find file associated to name
			for(File f : list) {
				if (f.getName().contains(card)) {
					System.out.println("Adding image: ("+ f.getName()+") to hand found in " + f.getPath());
					switch (f.getName().charAt(0)) {
						case 'A':{
							gbc.addCardToHand(new AdventureCard(card, f.getPath()));
							break;
						}
						case 'E':{
							gbc.addCardToHand(new EventCard(card, f.getPath()));
							break;
						}
						case 'T':{
							gbc.addCardToHand(new TournamentCard(card, f.getPath()));
							break;
						}
						case 'W':{
							AdventureCard weapon = new WeaponCard(card, f.getPath());
							gbc.addCardToHand(weapon);
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
                		
                		Platform.runLater(new Runnable() {
                			@Override
                			public void run() {
                				
                        		displayHand(currentMessage.substring(currentMessage.indexOf("player")));
                			}
                		});
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
		writeStream.println(message);
	}
}
