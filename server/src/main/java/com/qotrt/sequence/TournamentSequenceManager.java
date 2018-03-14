package com.qotrt.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.TournamentCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;
import com.qotrt.model.BoardModelMediator;
import com.qotrt.model.TournamentModel;


public class TournamentSequenceManager extends SequenceManager {

	final static Logger logger = LogManager.getLogger(SequenceManager.class);

	private TournamentCard card;

	public TournamentSequenceManager(TournamentCard card) {
		this.card = card;
	}
	
	@Override
	public void start(PlayerManager pm, BoardModelMediator bmm) {
		logger.info("Starting tournament sequence manager: " + this.card.getName());
		// Finding all players who want to join tournament
		Iterator<Player> players = pm.round();
		TournamentModel tm = bmm.getTournamentModel();
		tm.questionJoinPlayers(players);
		
		// Wait for responses
		try {
			tm.cdl.await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<Player> participants = tm.playersWhoJoined();
		

		// determining if anyone joined
		pm.drawCards(participants, 1);
		if(participants.size() == 0) {
			tm.setMessage("No Players join the tournament");
			tm.setWinners(new ArrayList<Player>());
			logger.info("No Players joined the tournament");
			return;
		} else if(participants.size() == 1) {
			pm.changeShields(participants, card.getShields() + 1);
			tm.setMessage("Only one participant joined");
			tm.setWinners(participants);
			logger.info("Only one participant joined");
			return;
		} 

		// more than one player (x > 1) joined tournament ask them to play cards
		tm.questionCards();
		
		// all players have decided on what cards to play
		// calculate highest bp and decide winner
		players = participants.iterator();
		pm.flipCards(players);
		logger.info("Flipping cards");

		BattlePointCalculator bpc = new BattlePointCalculator(pm);
		List<Player> winners = bpc.calculateHighest(participants, null);
		if(winners.size() != 1) {
			logger.info("Tie going again");
			// tie do tournament again
			pm.discardWeapons(participants);
			tm.setMessage("Tournament win");
			tm.setWinners(winners);
			
			// question players for cards to play
			tm.questionCards();
			// Wait for responses
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			players = winners.iterator();
			pm.flipCards(players);

			winners = bpc.calculateHighest(winners, null);
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			tm.setMessage("Player won");
			tm.setWinners(winners);
			logger.info("Winners: " + winners);
		} else {
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			tm.setMessage("Player won");
			tm.setWinners(winners);
			logger.info("Winners: " + winners);
		}
		
		logger.info("ending tournament sequence manager");
	}

}
