package src.socket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class InputController extends Thread {
	
	private LinkedBlockingQueue<String> queue;

	public InputController(LinkedBlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void run() {
		Supplier<String> socketOutput = () -> {
			try {
				return queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		};
		Stream<String> stream = Stream.generate(socketOutput);
		stream.map(s -> {
			handle(s);
			return s;
		});
	}
	
	public void handle(String message) {
		// TODO :) 
	}

}
