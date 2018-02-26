package src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.QuestCard;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;

public class BattlePointCalculator {

	final static Logger logger = LogManager.getLogger(BattlePointCalculator.class);
	
	private PlayerManager pm;

	public BattlePointCalculator(PlayerManager pm) {
		this.pm = pm;
	}

	public int calculatePoints(Player player, boolean iseultExists) {
		logger.info("Calculating player: " + player.getID() + " score");
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
		logger.info("Player: " + player.getID() + " score: " + score);
		return score;
	}
	
	public ArrayList<Integer> calculatePoints(List<Player> participants, StoryCard card){
		ArrayList<Integer> scores = new ArrayList<Integer>();
		participants.forEach(player -> {
			scores.add(calculatePlayer(player.getID(), player.getFaceUp().getDeck().stream().map(i -> i.getName()).toArray(String[]::new), card));
		});

		logger.info("Scores for: " + participants + " card: " + card + " scores: " + scores);
		return scores;
	}

	public void getFoeWinners(List<Player> participants, StoryCard card, int foePoints){
		ListIterator<Player> players = participants.listIterator();
		while(players.hasNext()) {
			Player player = players.next();
			if (calculatePlayer(player.getID(), player.getFaceUp().getDeck().stream().map(i -> i.getName()).toArray(String[]::new), card) < foePoints) {
				players.remove();
			}
		}
		logger.info("Survivors: " + participants);
	}

	public List<Player> calculateHighest(List<Player> participants, StoryCard card) {
		int max = Integer.MIN_VALUE;
		List<Player> winning = new ArrayList<Player>();
		ArrayList<Integer> scores = calculatePoints(participants, card);
		for(int i = 0; i < scores.size(); i++) {
			if(scores.get(i) == max) {
				winning.add(participants.get(i));
			} else if (scores.get(i) > max) {
				winning.clear();
				winning.add(participants.get(i));
				max = scores.get(i);
			}
		}
		
		logger.info("Highest: " + winning);
		return winning;
	}


	public List<AdventureCard> listOfTypeDecreasingBp(Player player, TYPE type, QuestCard card, Boolean othersHaveIseult){
		
		for(AdventureCard c: player.hand.getDeck()) {
			if(c.getName().equals("Queen Iseult")) othersHaveIseult = true;
		}
		
		// p1 and p2 being flipped is not a typo :)
		final boolean foundIseult1 = othersHaveIseult;
		return player.hand.getDeck().stream().
				filter(i -> i.getType() == type).
				sorted((p1,p2) -> Integer.compare(getPoints(p2, foundIseult1, card), getPoints(p1, foundIseult1, card))).
				collect(Collectors.toList());
	}
	
	public int getPoints(AdventureCard c, Boolean foundIseult, QuestCard card) {
		if(card != null) {
			logger.info("Card name: " + card.getName());
		}
		if(card != null && c.checkIfNamed(card.getFoe())) {
			logger.info(c.getName() + " named " + c.getNamedBattlePoints());
			return c.getNamedBattlePoints();
		} else if(c.getName().equals("Sir Tristan") && foundIseult) {
			logger.info(c.getName() + " named " + c.getNamedBattlePoints());
			return c.getNamedBattlePoints();
		}
		
		logger.info(c.getName() + " not named " + c.getBattlePoints());
		return c.getBattlePoints();
	}

	public List<AdventureCard> uniqueListOfTypeDecreasingBp(Player player, TYPE type, QuestCard questCard, Boolean othersHaveIseult){
		return this.listOfTypeDecreasingBp(player, type, questCard, othersHaveIseult).stream().
				map(i -> i.getName()).distinct().
				map(i -> player.hand.findCardByName(i)).
				collect(Collectors.toList());
	}


	public List<AdventureCard> uniqueListOfTypeDecreasingBp(UIPlayer player, QuestCard card, boolean iseultExists,
			TYPE... types) {
		for(AdventureCard c: player.hand.getDeck()) {
			if(c.getName().equals("Queen Iseult")) iseultExists = true;
		}
		
		// p1 and p2 being flipped is not a typo :)
		final boolean foundIseult1 = iseultExists;
		return player.hand.getDeck().stream().
				filter(i -> {
					for(TYPE t: types) {
						if(t == i.getType()) {
							return true;
						}
					}
					return false;
				}).
				map(i -> i.getName()).distinct().
				map(i -> player.hand.findCardByName(i)).
				sorted((p1,p2) -> Integer.compare(getPoints(p2, foundIseult1, card), getPoints(p1, foundIseult1, card))).
				collect(Collectors.toList());
	}
	
	public int calculatePlayer(int player, String[] cards, StoryCard storyCard) {
		logger.info("Calculating score for player id: " + player);
		if(storyCard != null) {
			logger.info("Middle card: " + storyCard);
		}
		logger.info("Player cards: " + Arrays.toString(cards));
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


		boolean foundIseult = false;
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			if(players.next().iseult == true) foundIseult = true;
		}
		
		for(String c: cards) {
			logger.info("Card: " + c);
			AdventureCard card = p.getFaceUp().findCardByName(c);
			if(card == null) {
				card = p.hand.findCardByName(c);
			}
			if(card == null) {
				continue;
			}

			if(storyCard != null && storyCard.getType() == src.game_logic.StoryCard.TYPE.QUEST && 
					card.checkIfNamed(((QuestCard) storyCard).getFoe())) {
				logger.info("Plus named: " + card.getNamedBattlePoints());
				score += card.getNamedBattlePoints();
			} else {
				logger.info("Plus: " + card.getBattlePoints());
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
		
		logger.info("Player score is: " + score);
		return score;
	}

	public int calculateStage(AdventureDeck hand, String[] cards, StoryCard storyCard) {
		logger.info("Calculating score for stage with quest: " + storyCard.getName());
		if(storyCard.getType() != src.game_logic.StoryCard.TYPE.QUEST) {
			return 0;
		}
		
		int score = 0;
		logger.info("Cards in stage: " + Arrays.toString(cards));
		for(String c: cards) {
			AdventureCard card = hand.findCardByName(c);
			if(card.checkIfNamed(((QuestCard) storyCard).getFoe()) && card.getType() == TYPE.FOES){
				score += card.getNamedBattlePoints();
				logger.info("Card: " + card + " score-named: " + card.getNamedBattlePoints());
			} else {
				score += card.getBattlePoints();
				logger.info("Card: " + card + " score: " + card.getBattlePoints());
			}
		}
		
		logger.info("Score is: " + score);
		return score;
	}

	public boolean canSponsor(Player next, QuestCard card) {
		List<AdventureCard> foes = this.uniqueListOfTypeDecreasingBp(next, TYPE.FOES, card, false);
		List<AdventureCard> tests = this.uniqueListOfTypeDecreasingBp(next, TYPE.TESTS, card, false);
		int uniqueBpFoes = 0;
		int minBp = Integer.MIN_VALUE;
		Collections.reverse(foes);
		for(AdventureCard c: foes) {
			if(getPoints(c, false, card) > minBp) {
				uniqueBpFoes++;
				minBp = getPoints(c,false,card);
			}
		}
		
		if(tests.size() != 0) {
			uniqueBpFoes++;
		}
		
		logger.info("Player: " + next.getID() + " can sponsor: " + (uniqueBpFoes + 1 >= card.getNumStages()));
		return uniqueBpFoes >= card.getNumStages();
	}


}