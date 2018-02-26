package src.socket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import src.messages.Message;

public class OutputController extends Thread {
	final static Logger logger = LogManager.getLogger(OutputController.class);
	private LinkedBlockingQueue<String> queue;
	public LinkedBlockingQueue<Message> internalQueue;

	public OutputController(LinkedBlockingQueue<String> queue) {
		this.queue = queue;
		this.internalQueue = new LinkedBlockingQueue<Message>();
	}

	public void run() {
		Gson gson = new Gson();
		Supplier<Message> socketOutput = () -> {
			try {
				Message received = internalQueue.take();
				return received;
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				return null;
			}
		};
		Stream<Message> stream = Stream.generate(socketOutput);
		stream.map(s -> {
			queue.add(gson.toJson(s));
			return s;
		}).allMatch(s -> s != null);
	}
	
	
	public void sendMessage(Message message) {
		internalQueue.add(message);
	}
}
