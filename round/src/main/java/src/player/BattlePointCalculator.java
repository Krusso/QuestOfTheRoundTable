package src.player;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureDeck;
import src.game_logic.Rank.RANKS;

public class BattlePointCalculator {
	
	public ArrayList<Integer> calculatePoints(List<Player> participants){
		ArrayList<Integer> scores = new ArrayList<Integer>();
		participants.forEach(player -> {
			scores.add(scoreOfPlayer(player));
		});
		
		return scores;
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
		
		if(player.hasTristanIseultBoost()) {
			score += 10;
		}
		
		AdventureDeck cards = player.getFaceUp();
		score += cards.getBP();
		return score;
	}

}
