package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.cards.StoryCard;
import com.qotrt.cards.AdventureCard.TYPE;

public interface AICommonImplementation  {
	public default List<List<AdventureCard>> doISponsorAQuest(QuestCard questCard, Logger logger, PlayerManager pm, Player player, Boolean canWinOrEvolve, BattlePointCalculator bpc) {
		logger.info("Asking if want to sponsor quest");
		if(questCard != null) {
			logger.info("Quest card: " + questCard.getName());
		}
		if(canWinOrEvolve) {
			logger.info("Someone can win dont sponsor tournament");
			return null;
		}

		List<List<AdventureCard>> cards = new ArrayList<List<AdventureCard>>();
		List<AdventureCard> sortedfoes = bpc.listOfTypeDecreasingBp(player, TYPE.FOES, questCard, pm.iseultExists());
		List<AdventureCard> sortedweapons = bpc.uniqueListOfTypeDecreasingBp(player, TYPE.WEAPONS, questCard, pm.iseultExists());
		List<AdventureCard> test = bpc.listOfTypeDecreasingBp(player, TYPE.TESTS, questCard, pm.iseultExists());

		int stages = questCard.getNumStages();

		IntStream.range(0, stages + 1).forEach(i -> cards.add(new ArrayList<AdventureCard>()));

		//set up last stage to be atleast 40
		if(sortedfoes.size() <= 0) {
			logger.info("Not enough foes to sponsor quest");
			return null;
		}

		AdventureCard biggestFoe = sortedfoes.remove(0);
		int bp = biggestFoe.getBattlePoints();
		cards.get(stages).add(biggestFoe);
		while(sortedweapons.size() != 0 && bp < 40) {
				AdventureCard biggestWeapon = sortedweapons.remove(0);
				bp += biggestWeapon.getBattlePoints();
				cards.get(stages).add(biggestWeapon);
		}

		if(bp < 40) {
			logger.info("Cant make last stage atleast 40 dont sponsor");
			return null;
		}

		// check if second last stage can be a test
		if(test.size() != 0) cards.get(stages - 1).add(test.get(0));


		Collections.reverse(sortedfoes);
		int currentBp = Integer.MIN_VALUE;
		int stage = 1;
		if(cards.get(stages - 1).size() != 0) {
			for(AdventureCard foecard: sortedfoes) {
				if(stage == stages - 1) break;
				if(foecard.getBattlePoints() > currentBp) {
					cards.get(stage).add(foecard);
					stage++;
					currentBp = foecard.getBattlePoints();
				}
			}
		} else {
			for(AdventureCard foecard: sortedfoes) {
				if(stage == stages) break;
				if(foecard.getBattlePoints() > currentBp) {
					cards.get(stage).add(foecard);
					stage++;
					currentBp = foecard.getBattlePoints();
				}
			}
		}

		cards.remove(0);

		BattlePointCalculator bc = new BattlePointCalculator(null);
		int min = Integer.MIN_VALUE;
		for(List<AdventureCard> i: cards) {
			if(i.size() == 0) {
				logger.info("A3 doesnt want to sponsor tournament not enough different bp foes/tests");
				return null;	
			}
			int pointsInStage = bc.calculateStage(player.hand, 
					i.stream().map(j -> j.getName()).toArray(String[]::new),
					(StoryCard) questCard);
			if(pointsInStage < min) {
				logger.info("Cant make stages follow increasing bp order");
				return null;
			}
			min = pointsInStage;
		}
		
		logger.info("Cards picked: " + cards);
		return cards;
	}

	public default List<AdventureCard> discardAfterWinningTest(Logger logger, int rounds, BattlePointCalculator bpc, Player player, PlayerManager pm){
		logger.info("Discarding cards after round: " + rounds);
		if(rounds == 1) {
			logger.info("First round finding all foes under 25");
			return bpc.listOfTypeDecreasingBp(player, TYPE.FOES, null, pm.iseultExists()).stream().
					filter(i -> i.getBattlePoints() < 25).collect(Collectors.toList());
		} else if(rounds == 2) {
			List<AdventureCard> cards = new ArrayList<AdventureCard>();
			Set<String> uniqueCards = new HashSet<String>();
			for(AdventureCard card: player.hand.getDeck()) {
				if(card.getType() == TYPE.FOES && card.getBattlePoints() < 25) {
					cards.add(card);
				} else if(uniqueCards.contains(card.getName())) {
					cards.add(card);
				} else {
					uniqueCards.add(card.getName());
				}
			}

			logger.info("Round 2 finding foes under 25 and duplicates");
			logger.info("Cards: " + cards);
			return cards;
		} else {
			logger.info("Past round 2 not willing to bid");
			return null;
		}
	}
}
