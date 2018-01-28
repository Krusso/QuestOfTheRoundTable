package src.socket;


import java.util.concurrent.LinkedBlockingQueue;

import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.player.PlayerManager;
import src.sequence.GameSequenceManager;
import src.sequence.SequenceManager;
import src.views.PlayerView;
import src.views.PlayersView;

public class Game extends Thread{

	private OutputController output;
	private GameModel gm;
	private LinkedBlockingQueue<String> actions;

	public Game(OutputController output, GameModel gm) {
		this.output = output;
		this.gm = gm;
	}

	public void run() {
		
		BoardModel bm = new BoardModel();
		DeckManager dm = new DeckManager();
		PlayerManager pm = new PlayerManager(gm.getNumPlayers(), dm);
		PlayersView pvs = new PlayersView(output);
		pm.subscribe(pvs);
		PlayerView pv = new PlayerView(output);
		pm.subscribe(pv);
		bm.subscribe(pv);
		pm.start();

		GameSequenceManager gsm = new GameSequenceManager();
		while(true) {
			pm.nextTurn();
			//System.out.println("deck size: " + dm.storySize());
			bm.setCard(dm.getStoryCard(1).get(0));
			if(dm.storySize() == 0) {
				break;
			}
			SequenceManager sm = gsm.createStoryManager(bm.getCard());
			sm.start(actions, pm, bm);
			//System.exit(0);
			pm.nextTurn();
			System.exit(0);
		}

	}

	public void setActionQueue(LinkedBlockingQueue<String> actionQueue) {
		this.actions = actionQueue;
	}

}
