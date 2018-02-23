package src.socket;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.StoryCard;
import src.messages.QOTRTQueue;
import src.messages.game.ContinueGameClient;
import src.player.PlayerManager;
import src.sequence.DiscardSequenceManager;
import src.sequence.GameSequenceManager;
import src.sequence.SequenceManager;
import src.views.PlayerView;
import src.views.PlayersView;

public class Game extends Thread{

	final static Logger logger = LogManager.getLogger(Game.class);
	
	private OutputController output;
	private GameModel gm;
	private QOTRTQueue actions;
	private boolean rigged;
	private BoardModel bm;

	public Game(OutputController output, GameModel gm) {
		this.output = output;
		this.gm = gm;
	}

	public void run() {
		bm = new BoardModel();
		DeckManager dm = new DeckManager();
		PlayerManager pm = new PlayerManager(gm.getNumPlayers(), dm, rigged);
		PlayersView pvs = new PlayersView(output);
		pm.subscribe(pvs);
		PlayerView pv = new PlayerView(output);
		pm.subscribe(pv);
		bm.subscribe(pv);
		pm.start();
		actions.setPlayerManager(pm);
		actions.setBoardModel(bm);
		actions.setOutputController(output);
		DiscardSequenceManager dsm = new DiscardSequenceManager(actions, pm, bm);
		pm.setDiscardSequenceManager(dsm);

		GameSequenceManager gsm = new GameSequenceManager();
		while(true) {
			logger.info("Next Turn");
			pm.nextTurn();
			StoryCard s = dm.getStoryCard(1).get(0);
			logger.info("Next card being played: " + s.getName());
			bm.setCard(dm.getStoryCard(1).get(0));

			SequenceManager sm = gsm.createStoryManager(bm.getCard());
			sm.start(actions, pm, bm);
			
			boolean winners = pm.rankUp();
			if(winners) {
				sm = gsm.createStoryManager(StoryCard.GAMEOVER);
				sm.start(actions, pm, bm);
				break;
			}

			logger.info("Waiting for player to continue to next turn");
			// wait until client is ready for the next turn
			actions.take(ContinueGameClient.class);
		}

	}

	public BoardModel getBoardModel() {
		return this.bm;
	}
	
	public void setActionQueue(QOTRTQueue actionQueue) {
		this.actions = actionQueue;
	}

	public void setRigged(boolean rigged) {
		this.rigged = rigged;
	}

}
