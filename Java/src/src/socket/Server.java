package src.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
 * A chat server that delivers public and private messages.
 */
public class Server {

	// The server socket.
	private static ServerSocket serverSocket = null;
	// The client socket.
	private static Socket clientSocket = null;

	private static final ArrayList<ClientRead> threads = new ArrayList<ClientRead>(4);

	public static void main(String args[]) {

		// The default port number.
		int portNumber = 2222;

		/*
		 * Open a server socket on the portNumber (default 2222). Note that we can
		 * not choose a port less than 1023 if we are not privileged users (root).
		 */
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println(e);
		}

		/*
		 * Create a client socket for each connection and pass it to a new client
		 * thread.
		 */
		
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		InputController input = new InputController(queue);
		input.start();
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				ClientRead thread = new ClientRead(clientSocket, threads, queue);
				threads.add(thread);
				thread.start();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}

class ClientWrite extends Thread {
	private Socket clientSocket = null;
	private LinkedBlockingQueue<String> queue = null;
	
	public ClientWrite(Socket clientSocket, ArrayList<ClientRead> threads, LinkedBlockingQueue<String> queue) {
		this.clientSocket = clientSocket;
		this.queue = queue;
	}

	public void run() {
		try {
			PrintStream out = new PrintStream(clientSocket.getOutputStream());
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
				out.println(s);
				return s;
			})
			.allMatch(s -> s != null);
			
			
			out.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}


class ClientRead extends Thread {
	private Socket clientSocket = null;
	private LinkedBlockingQueue<String> queue = null;

	public ClientRead(Socket clientSocket, ArrayList<ClientRead> threads, LinkedBlockingQueue<String> queue) {
		this.clientSocket = clientSocket;
		this.queue = queue;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			Supplier<String> socketInput = () -> {
				try {
					return br.readLine();
				} catch (IOException ex) {
					return null;
				}
			};

			Stream<String> stream = Stream.generate(socketInput);
			stream.map(s -> {
				queue.add(s);
				return s;
			})
			.allMatch(s -> s != null);
			br.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}