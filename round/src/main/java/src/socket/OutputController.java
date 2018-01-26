package src.socket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OutputController extends Thread {
	private LinkedBlockingQueue<String> queue;
	private LinkedBlockingQueue<String> internalQueue;

	public OutputController(LinkedBlockingQueue<String> queue) {
		this.queue = queue;
		this.internalQueue = new LinkedBlockingQueue<String>();
	}

	public void run() {
		Supplier<String> socketOutput = () -> {
			try {
				String received = internalQueue.take();
				return received;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		};
		Stream<String> stream = Stream.generate(socketOutput);
		stream.map(s -> {
			queue.add(s);
			return s;
		}).allMatch(s -> s != null);
		System.out.println("here");
	}
	
	
	public void sendMessage(String message) {
		internalQueue.add(message);
	}
}
