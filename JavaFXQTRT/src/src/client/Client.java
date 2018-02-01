package src.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client implements Runnable {

	private String host;
	private int port;
	Socket client;
	PrintStream writeStream;
	BufferedReader readStream;
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			client = new Socket(host, port);
			writeStream = new PrintStream(client.getOutputStream());
            readStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String message;
            
            while(client.isConnected()) {
            	if(readStream.ready()) {
                	message = readStream.readLine();
                	System.out.println("Messsage received: " + message);
            	}
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
