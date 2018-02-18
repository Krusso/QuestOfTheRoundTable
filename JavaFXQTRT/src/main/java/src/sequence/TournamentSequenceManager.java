package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.BoardModel;
import src.game_logic.TournamentCard;
import src.messages.QOTRTQueue;
import src.messages.tournament.TournamentAcceptDeclineClient;
import src.player.BattlePointCalculator;
import src.player.Player;
import src.player.PlayerManager;

public class TournamentSequenceManager extends SequenceManager {

	TournamentCard card;

	public TournamentSequenceManager(TournamentCard card) {
		this.card = card;
	}

	@Override
	public void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
		// Finding all players who want to join tournament
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.QUESTIONED);
			TournamentAcceptDeclineClient tadc = actions.take(TournamentAcceptDeclineClient.class);
			if(tadc.joined) {
				pm.setState(next, Player.STATE.YES);
			} else {
				pm.setState(next, Player.STATE.NO);
			}
		}

		// determining if anyone joined
		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		if(participants.size() == 0) {
			return;
		} else if(participants.size() == 1) {
			pm.changeShields(participants, card.getShields() + 1);
			pm.setState(participants.get(0), Player.STATE.WIN);
			return;
		} else {
			players = participants.iterator();
			questionPlayersTournament(players, pm, actions);
		}

		// all players have decided on what cards to play
		// calculate highest bp and decide winner
		players = participants.iterator();
		pm.flipCards(players);	

		BattlePointCalculator bpc = new BattlePointCalculator();
		List<Player> winners = bpc.calculateHighest(participants);
		if(winners.size() != 1) {
			// tie do tournament again
			pm.discardWeapons(participants);
			players = winners.iterator();
			questionPlayersTournament(players, pm, actions);

			players = winners.iterator();
			pm.flipCards(players);

			winners = bpc.calculateHighest(winners);
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			pm.setState(winners, Player.STATE.WIN);
		} else {
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			pm.setState(winners, Player.STATE.WIN);
		}
	}
}
