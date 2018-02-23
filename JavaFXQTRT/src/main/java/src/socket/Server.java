package src.socket;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Server {
	final static Logger logger = LogManager.getLogger(Server.class);


	public Server(LinkedBlockingQueue<String> clientInput, LinkedBlockingQueue<String> serverOutput) {
		/*
		 * Create a client socket for each connection and pass it to a new client
		 * thread.
		 */

		LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue<String>();
		LinkedBlockingQueue<String> outputQueue = new LinkedBlockingQueue<String>();

		try {
			ClientRead readThread = new ClientRead(clientInput, inputQueue);
			ClientWrite writeThread = new ClientWrite(serverOutput, outputQueue);
			readThread.start();
			writeThread.start();

			GameModel gm = new GameModel();

			OutputController output = new OutputController(outputQueue);
			Game game = new Game(output, gm);
			InputController input = new InputController(inputQueue, game, gm);
			input.start();
			output.start();

			game.join();
			logger.info("Game over :)");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}
}

class ClientWrite extends Thread {
	private LinkedBlockingQueue<String> serverOutput;
	private LinkedBlockingQueue<String> queue = null;

	public ClientWrite(LinkedBlockingQueue<String> serverOutput, LinkedBlockingQueue<String> queue) {
		this.serverOutput = serverOutput;
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
			try {
				serverOutput.put(s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return s;
		})
		.allMatch(s -> s != null);
	}
}


class ClientRead extends Thread {
	private LinkedBlockingQueue<String> clientInput;
	private LinkedBlockingQueue<String> queue = null;

	public ClientRead(LinkedBlockingQueue<String> clientInput, LinkedBlockingQueue<String> queue) {
		this.clientInput = clientInput;
		this.queue = queue;
	}

	public void run() {
		Supplier<String> socketInput = () -> {
			try {
				return clientInput.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		};

		Stream<String> stream = Stream.generate(socketInput);
		stream.map(s -> {
			queue.add(s);
			return s;
		})
		.allMatch(s -> s != null);
	}
}