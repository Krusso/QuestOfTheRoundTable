package src.socket;

import src.socket.Server;

public class Test {
	
	public static void main(String args[]) throws InterruptedException {
		Runnable task2 = () -> { Server.main(null); };
		 
		// start the thread
		new Thread(task2).start();
		
		Thread.sleep(1000);
		Client.main(null);
	}

}
