package src.socket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OutputController extends Thread {
	private LinkedBlockingQueue<String> queue;
	private LinkedBlockingQueue<String> internalQueue;

	public OutputController(LinkedBlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void run() {
		Supplier<String> socketOutput = () -> {
			try {
				return internalQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		};
		Stream<String> stream = Stream.generate(socketOutput);
		stream.map(s -> {
			queue.add(s);
			return s;
		});
	}
	
	
	public synchronized void sendMessage(String message) {
		queue.add(message);
	}
}
