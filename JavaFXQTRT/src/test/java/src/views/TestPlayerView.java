package src.views;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import src.messages.Message;
import src.messages.QOTRTQueue;
import src.messages.Message.MESSAGETYPES;
import src.messages.game.MiddleCardServer;
import src.socket.Game;
import src.socket.GameModel;
import src.socket.OutputController;

public class TestPlayerView {
	final static Logger logger = LogManager.getLogger(TestPlayerView.class);
	
	@Test
	public void testMiddleCardMessage() throws InterruptedException {
		LinkedBlockingQueue<String> outputQueue = new LinkedBlockingQueue<String>();
		OutputController output = new OutputController(outputQueue);
		GameModel gm = new GameModel();
		gm.setNumPlayers(2);
		Game game = new Game(output, gm);
		game.setActionQueue(new QOTRTQueue());
		game.setRigged(true);
		Runnable task2 = () -> { game.start(); };
		new Thread(task2).start();
		
		while(true) {
			Message message = output.internalQueue.take();
			if(message.message == MESSAGETYPES.SHOWMIDDLECARD) {
				logger.info("Middle card received: " + ((MiddleCardServer) (message)).card);
				game.getBoardModel().getCard().getName().equals(((MiddleCardServer) (message)).card);
				break;
			}
		}
	}
	
}
