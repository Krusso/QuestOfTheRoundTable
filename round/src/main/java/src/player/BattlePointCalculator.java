package src.player;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureDeck;
import src.game_logic.Rank.RANKS;

public class BattlePointCalculator {
	
	/**
	 * 
	 * NOTE: not sure I like that the players cards are flipped over in here probably better
	 * in the sequencemanager class
	 */
	
	public List<Player> calculatePoints(List<Player> participants) {
		int max = Integer.MIN_VALUE;
		List<Player> winning = new ArrayList<Player>();
		for(Player player: participants) {
			int score = scoreOfPlayer(player);
			if(score == max) {
				winning.add(player);
			} else if (score > max) {
				winning.clear();
				winning.add(player);
				max = score;
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
		
		AdventureDeck cards = player.getFaceUp();
		score += cards.getBP();
		return score;
	}

}
