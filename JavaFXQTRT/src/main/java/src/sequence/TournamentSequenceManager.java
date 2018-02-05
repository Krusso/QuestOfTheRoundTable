package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.BoardModel;
import src.game_logic.TournamentCard;
import src.player.BattlePointCalculator;
import src.player.Player;
import src.player.PlayerManager;

public class TournamentSequenceManager extends SequenceManager {

	TournamentCard card;

	public TournamentSequenceManager(TournamentCard card) {
		this.card = card;
	}

	@Override
	public void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm) {
		// Finding all players who want to join tournament
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionTournament();
			String string;
			try {
				string = actions.take();
				System.out.println("Action recieved: " + string);
				Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			    Matcher m = p.matcher(string);
			    m.find();
			    //System.out.println("3: " + m.group(3));
			    //System.out.println("3: " + m.group(4));
				if(m.group(3).equals("accept")) {
					pm.currentAcceptTournament();
				} else {
					pm.currentDeclineTournament();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// cause tracing logs sucks
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		// determining if anyone joined
		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		if(participants.size() == 0) {
			return;
		} else if(participants.size() == 1) {
			pm.changeShields(participants, card.getShields() + 1);
			pm.setTournamentWinner(participants);
			return;
		} else {
			players = participants.iterator();
			questionPlayers(players, pm, actions, "game tournament picked: player (\\d+) (.*)");
		}
		
		// all players have decided on what cards to play
		// calculate highest bp and decide winner
		players = participants.iterator();
		while(players.hasNext()) {
			pm.flipCards(players.next());	
		}
		
		BattlePointCalculator bpc = new BattlePointCalculator();
		List<Player> winners = bpc.calculateHighest(participants);
		if(winners.size() != 1) {
			// tie do tournament again
			pm.discardWeapons(participants);
			players = winners.iterator();
			questionPlayers(players, pm, actions, "game tournament picked: player (\\d+) (.*)");
			
			players = winners.iterator();
			while(players.hasNext()) {
				pm.flipCards(players.next());	
			}
			
			winners = bpc.calculateHighest(winners);
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			pm.setTournamentWinner(winners);
		} else {
			pm.changeShields(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			pm.setTournamentWinner(winners);
		}
	}
}
