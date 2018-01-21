package src.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try {

            Socket echoSocket = new Socket("localhost", 2223);
            PrintStream out = new PrintStream(echoSocket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            
            out.println("game start:2");
            while(true) {
            	String x = br.readLine();
            	System.out.println("Client Received: " + x);
            	if("quit".equals(x)) {
            		break;
            	}
            	if("tournament accept: player 0".equals(x)) {
            		out.println("game tournament accept: player 0");
            	}
            	if("tournament accept: player 1".equals(x)) {
            		out.println("game tournament accept: player 1");
            	}
            }

            echoSocket.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }
}