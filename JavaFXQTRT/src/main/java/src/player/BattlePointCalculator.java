package src.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureDeck;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.Rank.RANKS;

public class BattlePointCalculator {

	private PlayerManager pm;

	public BattlePointCalculator(PlayerManager pm) {
		this.pm = pm;
	}

	public ArrayList<Integer> calculatePoints(List<Player> participants){
		ArrayList<Integer> scores = new ArrayList<Integer>();
		participants.forEach(player -> {
			scores.add(scoreOfPlayer(player));
		});

		return scores;
	}

	public void getFoeWinners(List<Player> participants, int foePoints){
		ListIterator<Player> players = participants.listIterator();
		while(players.hasNext()) {
			if (scoreOfPlayer(players.next()) < foePoints) {
				players.remove();
			}
		}
	}

	public List<Player> calculateHighest(List<Player> participants) {
		int max = Integer.MIN_VALUE;
		List<Player> winning = new ArrayList<Player>();
		ArrayList<Integer> scores = calculatePoints(participants);
		for(int i = 0; i < scores.size(); i++) {
			if(scores.get(i) == max) {
				winning.add(participants.get(i));
			} else if (scores.get(i) > max) {
				winning.clear();
				winning.add(participants.get(i));
				max = scores.get(i);
			}
		}
		return winning;
	}

	public int scoreOfPlayer(Player player) {
		int score = 0;
		RANKS rank = player.getRank();
		if(rank == RANKS.SQUIRE) {
			score += 5;
		} else if(rank == RANKS.KNIGHT) {
			score += 10;
		} else {
			score += 20;
		}

		boolean foundIseult = false;
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			if(players.next().iseult == true) foundIseult = true;
		}

		if(player.tristan && foundIseult) {
			score += 10;
		}

		AdventureDeck cards = player.getFaceUp();
		score += cards.getBP();
		return score;
	}

	public List<AdventureCard> listOfTypeDecreasingBp(Player player, TYPE type, String questOrTournamentCard){
		// p1 and p2 being flipped is not a typo :) 
		return player.hand.getDeck().stream().
				sorted((p2,p1) -> Integer.compare(p1.getBattlePoints(), p2.getBattlePoints())).
				filter(i -> i.getType() == type).
				collect(Collectors.toList());
	}

	public List<AdventureCard> uniqueListOfTypeDecreasingBp(Player player, TYPE type, String questOrTournamentCard){
		return this.listOfTypeDecreasingBp(player, type, questOrTournamentCard).stream().
				map(i -> i.getName()).distinct().
				map(i -> player.hand.findCardByName(i)).
				collect(Collectors.toList());
	}
}