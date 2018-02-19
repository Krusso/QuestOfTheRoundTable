package src.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Card;
import src.game_logic.QuestCard;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;

public class BattlePointCalculator {

	private PlayerManager pm;

	public BattlePointCalculator(PlayerManager pm) {
		this.pm = pm;
	}

	public int calculatePoints(Player player, boolean iseultExists) {
		int score = 0;
		RANKS rank = player.getRank();
		if(rank == RANKS.SQUIRE) {
			score += 5;
		} else if(rank == RANKS.KNIGHT) {
			score += 10;
		} else {
			score += 20;
		}

		if(player.tristan && iseultExists) {
			score += 10;
		}

		AdventureDeck cards = player.getFaceUp();
		score += cards.getBP();
		return score;
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

	public List<AdventureCard> listOfTypeDecreasingBp(Player player, TYPE type, QuestCard card, Boolean othersHaveIseult){
		
		for(AdventureCard c: player.hand.getDeck()) {
			if(c.getName().equals("Queen Iseult")) othersHaveIseult = true;
		}
		
		// p1 and p2 being flipped is not a typo :)
		final boolean foundIseult1 = othersHaveIseult;
		return player.hand.getDeck().stream().
				sorted((p2,p1) -> Integer.compare(getPoints(p1, foundIseult1, card), getPoints(p2, foundIseult1, card))).
				filter(i -> i.getType() == type).
				collect(Collectors.toList());
	}
	
	public int getPoints(AdventureCard c, Boolean foundIseult, QuestCard card) {
		if(card != null && c.checkIfNamed(card.getFoe())) {
			return c.getNamedBattlePoints();
		} else if(c.getName().equals("Sir Tristan") && foundIseult) {
			return c.getNamedBattlePoints();
		}
		
		return c.getBattlePoints();
	}

	public List<AdventureCard> uniqueListOfTypeDecreasingBp(Player player, TYPE type, QuestCard questCard, Boolean othersHaveIseult){
		return this.listOfTypeDecreasingBp(player, type, questCard, othersHaveIseult).stream().
				map(i -> i.getName()).distinct().
				map(i -> player.hand.findCardByName(i)).
				collect(Collectors.toList());
	}

	public int calculatePlayer(int player, String[] cards, StoryCard storyCard) {
		Player p = pm.players[player];
		int score = 0;
		RANKS rank = p.getRank();
		if(rank == RANKS.SQUIRE) {
			score += 5;
		} else if(rank == RANKS.KNIGHT) {
			score += 10;
		} else {
			score += 20;
		}
		if(storyCard.getType() != src.game_logic.StoryCard.TYPE.QUEST) {
			return 0;
		}

		boolean foundIseult = false;
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			if(players.next().iseult == true) foundIseult = true;
		}
		
		for(String c: cards) {
			AdventureCard card = p.getFaceUp().findCardByName(c);
			if(card == null) {
				card = p.hand.findCardByName(c);
			}

			if(card.checkIfNamed(((QuestCard) storyCard).getFoe())) {
				score += card.getNamedBattlePoints();
			} else {
				score += card.getBattlePoints();
			}
			
			if(c.equals("Queen Iseult")) {
				foundIseult = true;
			}
		}
		
		for(String c: cards) {
			if(c.equals("Sir Tristan") && foundIseult) {
				score += 10;
			}
		}
		return score;
	}

}