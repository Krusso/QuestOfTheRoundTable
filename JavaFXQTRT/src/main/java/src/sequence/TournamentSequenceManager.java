package src.sequence;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.BoardModel;
import src.game_logic.TournamentCard;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.player.BattlePointCalculator;
import src.player.Player;
import src.player.PlayerManager;

public class TournamentSequenceManager extends SequenceManager {

	final static Logger logger = LogManager.getLogger(SequenceManager.class);
	
	private TournamentCard card;

	public TournamentSequenceManager(TournamentCard card) {
		this.card = card;
	}

	@Override
	public void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
		logger.info("Starting tournament sequence manager");
		// Finding all players who want to join tournament
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.QUESTIONED);
			TournamentAcceptDeclineClient tadc = actions.take(TournamentAcceptDeclineClient.class, MESSAGETYPES.JOINTOURNAMENT);
			if(tadc.joined) {
				pm.setState(next, Player.STATE.YES);
			} else {
				pm.setState(next, Player.STATE.NO);
			}
		}

		// determining if anyone joined
		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		pm.drawCards(participants, 1);
		if(participants.size() == 0) {
			pm.sendContinue("No Players join the tournament");
			return;
		} else if(participants.size() == 1) {
			pm.changeShields(participants, card.getShields() + 1);
			pm.setStates(participants, Player.STATE.WIN);
			logger.info("Only one participant he wins");
			return;
		} 

		players = participants.iterator();
		questionPlayersTournament(players, pm, actions);

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
			pm.showTournamentTie(winners);
			
			players = winners.iterator();
			questionPlayersTournament(players, pm, actions);

			players = winners.iterator();
			pm.flipCards(players);

			winners = bpc.calculateHighest(winners, null);
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			pm.setStates(winners, Player.STATE.WIN);
			logger.info("Winners: " + winners);
		} else {
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			pm.setStates(winners, Player.STATE.WIN);
			logger.info("Winners: " + winners);
		}
		
		logger.info("ending tournament sequence manager");
	}
}
