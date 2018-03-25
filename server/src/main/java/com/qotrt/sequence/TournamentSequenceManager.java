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
import com.qotrt.messages.tournament.TournamentWinServer.WINTYPES;
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
			tm.join().await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<Player> participants = tm.playersWhoJoined();
		logger.info("Players who joined: " + participants);

		// determining if anyone joined
		pm.drawCards(participants, 1);
		if(participants.size() == 0) {
			tm.setMessage(WINTYPES.NOJOIN);
			tm.setWinners(new ArrayList<Player>());
			logger.info("No Players joined the tournament");
			return;
		} else if(participants.size() == 1) {
			pm.changeShields(participants, card.getShields() + 1);
			tm.setMessage(WINTYPES.ONEJOIN);
			tm.setWinners(participants);
			logger.info("Only one participant joined");
			return;
		} 

		// more than one player (x > 1) joined tournament ask them to play cards
		tm.questionCards(participants);
		try {
			logger.info("Waiting for 60 seconds for users to pick their cards");
			tm.questionCards().await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		// dont let users pick anymore
		tm.finishPicking();
		logger.info("finished selecting cards");
		
		// all players have decided on what cards to play
		// calculate highest bp and decide winner
		players = participants.iterator();
		// TODO: add flip card message to let players see result of the tournament
		pm.flipCards(players);
		logger.info("Flipping cards");

		BattlePointCalculator bpc = new BattlePointCalculator(pm);
		List<Player> winners = bpc.calculateHighest(participants, null);
		if(winners.size() != 1) {
			logger.info("Tie going again");
			// tie do tournament again
			pm.discardWeapons(participants);
			tm.setMessage(WINTYPES.TIE);
			tm.setWinners(winners);
			
			// question players for cards to play
			tm.questionCards(winners);
			// Wait for responses
			try {
				tm.questionCards().await(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// dont let users pick anymore
			tm.finishPicking();

			players = winners.iterator();
			pm.flipCards(players);

			winners = bpc.calculateHighest(winners, null);
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			tm.setMessage(WINTYPES.WON);
			tm.setWinners(winners);
			logger.info("Winners: " + winners);
		} else {
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			tm.setMessage(WINTYPES.WON);
			tm.setWinners(winners);
			logger.info("Winners: " + winners);
		}
		
		logger.info("ending tournament sequence manager");
	}

}
