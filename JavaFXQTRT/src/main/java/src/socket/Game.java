package src.socket;


import src.game_logic.BoardModel;
import src.game_logic.DeckManager;
import src.game_logic.StoryCard;
import src.messages.QOTRTQueue;
import src.messages.game.ContinueGameClient;
import src.player.PlayerManager;
import src.sequence.GameSequenceManager;
import src.sequence.SequenceManager;
import src.views.PlayerView;
import src.views.PlayersView;

public class Game extends Thread{

	private OutputController output;
	private GameModel gm;
	private QOTRTQueue actions;

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
		actions.setPlayerManager(pm);

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
			
			boolean winners = pm.rankUp();
			if(winners) {
				sm = gsm.createStoryManager(StoryCard.GAMEOVER);
				sm.start(actions, pm, bm);
				break;
			}

			// wait until client is ready for the next turn
			actions.take(ContinueGameClient.class);
		}

	}

	public void setActionQueue(QOTRTQueue actionQueue) {
		this.actions = actionQueue;
	}

}
