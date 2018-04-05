package com.qotrt.gameplayer;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.game.Game;
import com.qotrt.gameplayer.aicontrollers.AIController;
import com.qotrt.gameplayer.aicontrollers.AIDiscard;
import com.qotrt.gameplayer.aicontrollers.AIEvent;
import com.qotrt.gameplayer.aicontrollers.AIQuest;
import com.qotrt.gameplayer.aicontrollers.AITournament;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.views.Observer;

public class AIPlayer extends Observer {

	final static Logger logger = LogManager.getLogger(AIPlayer.class);
	
	private int strat;
	private AbstractAI ai;
	private Game game;
	private Player player;
	private ArrayList<AIController> controllers = new ArrayList<AIController>();

	public AIPlayer(int strat, Game game) {
		this.strat = strat;
		this.game = game;
	}

	public void startAIPlayer(Player player, PlayerManager pm, BoardModelMediator bmm) {
		logger.info("Player: " + player.getID() + " strat: " + strat);
		this.player = player;
		if(strat == 1) {
			ai = new A1(player, pm, bmm);
		} else if(strat == 2) {
			ai = new A2(player, pm);
		} else {
			ai = new A3(player, pm, bmm); 
		}
		
		controllers.add(new AITournament(game, player, ai));
		controllers.add(new AIQuest(game, player, ai));
		controllers.add(new AIDiscard(game, player, ai));
		controllers.add(new AIEvent(game, player, ai));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.info("Player: " + player.getID() + " strat: " + strat + " got event: " + evt.getPropertyName());
		
		controllers.forEach(i -> i.handleEvent(evt));
	}



}
