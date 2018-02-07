package src.socket;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

import src.messages.QOTRTQueue;

public class InputController extends Thread {
	
	private LinkedBlockingQueue<String> inputQueue;
	private QOTRTQueue actionQueue;
	private Game game;
	private GameModel gm;

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
		// TODO change to some regex
		if("game start:2".equals(message) || "game start:3".equals(message) || "game start:4".equals(message)) {
			gm.setNumPlayers(Integer.parseInt(message.split(":")[1]));
			game.setActionQueue(actionQueue);
			game.start();
		} else if(message.contains("game")) {
			actionQueue.add(message);
		}
	}
}
