package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.TournamentCard;
import src.player.BattlePointerCalculator;
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
			pm.winTournament(participants, card.getShields() + 1);
		} else {
			players = participants.iterator();
			questionPlayers(players, pm, actions);
		}
		
		players = participants.iterator();
		while(players.hasNext()) {
			pm.flipCards(players.next());	
		}
		
		BattlePointerCalculator bpc = new BattlePointerCalculator();
		List<Player> winners = bpc.calculatePoints(participants);
		if(winners.size() != 1) {
			pm.discardWeapons(participants);
			players = winners.iterator();
			questionPlayers(players, pm, actions);
			
			winners = bpc.calculatePoints(winners);
			pm.winTournament(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			
		} else {
			pm.winTournament(winners, card.getShields() + participants.size());
			pm.discardCards(participants);
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
		}
		//System.exit(0);
		
	}

	private void questionPlayers(Iterator<Player> players, PlayerManager pm, LinkedBlockingQueue<String> actions) {
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionTournCards();
			String string;
			try {
				string = actions.take();
				Pattern p = Pattern.compile("game tournament picked: player (\\d+) (.*)");
			    Matcher m = p.matcher(string);
			    m.find();
			    String cards = m.group(2);
				pm.currentFaceDown(cards);
				System.out.println("Action recieved: " + string);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
}
