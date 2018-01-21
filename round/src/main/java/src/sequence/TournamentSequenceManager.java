package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import src.game_logic.TournamentCard;
import src.player.Player;
import src.player.PlayerManager;

public class TournamentSequenceManager extends SequenceManager {

	TournamentCard card;

	public TournamentSequenceManager(TournamentCard card) {
		this.card = card;
	}

	@Override
	public void start(LinkedBlockingQueue<String> actions, PlayerManager pm) {
		
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionTournament();
			String string;
			try {
				string = actions.take();
				System.out.println("Action recieved: " + string);
				if("game tournament accept: player 0".equals(string) ||
						"game tournament accept: player 1".equals(string)) {
					pm.currentAcceptTournament();
				} else if("game tournament decline: player 0".equals(string)) {
					pm.currentDeclineTournament();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		
		if(participants.size() == 0) {
			return;
		} else if(participants.size() == 1) {
			pm.tournamentWin(participants.get(0), card.getShields() + 1);
		} else {
			players = participants.iterator();
			while(players.hasNext()) {
				pm.setPlayer(players.next());
				pm.currentQuestionTournCards();
				String string;
				try {
					string = actions.take();
					System.out.println("Action recieved: " + string);
					if("game tournament accept: player 0".equals(string)) {
						pm.currentAcceptTournament();
					} else if("game tournament decline: player 0".equals(string)) {
						pm.currentDeclineTournament();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}
		
	}
}
