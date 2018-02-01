package src.game_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client{
	
	Socket client;
	PrintStream writeStream;
	BufferedReader readStream;
//	private LinkedBlockingQueue<String> queue;
	public Client(String ip, int port) {
		try {

			client = new Socket(ip,port);
			writeStream = new PrintStream(client.getOutputStream());
			readStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
//			queue = new LinkedBlockingQueue<String>();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String message) {
		writeStream.println(message);
	}
	public String readData() {
		try {
			if(readStream.ready()) {
//				queue.add(readStream.readLine());
				return readStream.readLine();
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	



}
