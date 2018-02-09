package src.socket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.game.GameStartClient;

public class InputController extends Thread {
	
	private LinkedBlockingQueue<String> inputQueue;
	private QOTRTQueue actionQueue;
	private Game game;
	private GameModel gm;
	private Gson gson = new Gson();
	private JsonParser json = new JsonParser();

	public InputController(LinkedBlockingQueue<String> queue, Game game, GameModel gm) {
		this.inputQueue = queue;
		this.game = game;
		this.gm = gm;
		this.actionQueue = new QOTRTQueue();
	}

	public void run() {
		System.out.println("Waiting");
		Supplier<String> socketOutput = () -> {
			try {
				return inputQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		};
		Stream<String> stream = Stream.generate(socketOutput);
		stream.map(s -> {
			handle(s);
			return s;
		}) .allMatch(s -> s != null);
		
		System.out.println("Done waiting");
	}
	
	public void handle(String message) {
		System.out.println("Server Received: " + message);
		JsonObject obj = json.parse(message).getAsJsonObject();
		if(obj.get("TYPE") == null || !obj.get("TYPE").getAsString().equals("GAME")){
			return;
		}
		
		if(obj.get("message") != null && obj.get("message").getAsString().equals(MESSAGETYPES.STARTGAME.name())) {
			GameStartClient gsc = gson.fromJson(obj, GameStartClient.class);
			gm.setNumPlayers(gsc.player);
			game.setActionQueue(actionQueue);
			game.start();
		} else {
			actionQueue.add(message);
		}
	}
}
