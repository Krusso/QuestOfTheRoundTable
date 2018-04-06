package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;

public class A2 extends AbstractAI implements AICommonImplementation {
	final static Logger logger = LogManager.getLogger(A2.class);

	public A2(Player player, PlayerManager pm) {
		super(player, pm);
	}
	
	@Override
	public boolean doIParticipateInTournament() {
		logger.info("Participate in tournament");
		return true;
	}


	@Override
	public List<AdventureCard> playCardsForTournament() {
		int currentBp = bpc.calculatePlayer(player, null);
		if(currentBp >= 50) {
			logger.info("BP over 50 not playing any more cards");
			return null;
		}
		
		List<AdventureCard> alliesOrWeapons = bpc.uniqueListOfTypeDecreasingBp(player, null, pm.iseultExists(), TYPE.ALLIES, TYPE.WEAPONS, TYPE.AMOUR);
		logger.info("Cards can use: " + alliesOrWeapons);
		List<AdventureCard> cardsToUse = new ArrayList<AdventureCard>();
		for(AdventureCard card: alliesOrWeapons) {
			if(currentBp >= 50) {
				break;
			}
			cardsToUse.add(card);
			currentBp += card.getBattlePoints();
		}
		logger.info("Played cards: " + Arrays.toString(cardsToUse.stream().map(i -> i.getName()).toArray(String[]::new)));
		return cardsToUse;

	}

	@Override
	public List<List<AdventureCard>> doISponsorAQuest(QuestCard questCard) {
		return this.doISponsorAQuest(questCard, logger, pm, player, playerCanWinOrEvolve(pm), bpc, 40);
	}

	@Override
	public boolean doIParticipateInQuest(QuestCard questCard) {
		logger.info("Asking A2 if wants to join quest");
		rounds = 0;
		stages = 0;
		List<AdventureCard> sortedfoes = bpc.listOfTypeDecreasingBp(player, TYPE.FOES, questCard, pm.iseultExists());

		int tenIncrements = 0;
		for(AdventureCard card: player.hand.getDeck()) {
			if((card.getType() == TYPE.WEAPONS || card.getType() == TYPE.ALLIES
					|| card.getType() == TYPE.AMOUR) && card.getBattlePoints() == 10) {
				logger.info("A2 found increment of 10");
				tenIncrements++;
			}
		}

		int foes = 0;
		for(AdventureCard foe: sortedfoes) {
			if(foe.getBattlePoints() < 25) {
				foes++;
			}
		}

		logger.info((foes >= 2) && (tenIncrements >= questCard.getNumStages()));
		return (foes >= 2) && (tenIncrements >= questCard.getNumStages());
	}

	@Override
	public List<AdventureCard> playCardsForFoeQuest(QuestCard questCard) {
		List<AdventureCard> cards = new ArrayList<AdventureCard>();
		stages++;
		List<AdventureCard> sortedweapons = bpc.uniqueListOfTypeDecreasingBp(player, TYPE.WEAPONS, questCard, pm.iseultExists());
		List<AdventureCard> sortedallies = bpc.listOfTypeDecreasingBp(player, TYPE.ALLIES, questCard, pm.iseultExists());
		sortedallies.addAll(bpc.listOfTypeDecreasingBp(player, TYPE.AMOUR, questCard, pm.iseultExists()));
		if(stages >= questCard.getNumStages()) {
			// if last stage add everything you can
			cards.addAll(sortedweapons);
			cards.addAll(sortedallies);
		} else {
			// play an incremement of +10 using amour, then ally, then weapons
			for(AdventureCard card: sortedallies) {
				if(card.getType().equals(TYPE.AMOUR)) {
					cards.add(card);
					logger.info("Playing cards for foe: " + cards);
					return cards;
				}
			}
			for(AdventureCard card: sortedallies) {
				if(card.getBattlePoints() == 10) {
					cards.add(card);
					logger.info("Playing cards for foe: " + cards);
					return cards;
				}
			}
			for(AdventureCard card: sortedweapons) {
				if(card.getBattlePoints() == 10) {
					cards.add(card);
					logger.info("Playing cards for foe: " + cards);
					return cards;
				}
			}
		}
		logger.info("Playing cards for foe: " + cards);
		return cards;
	}

	@Override
	public int nextBid(int prevBid) {
		rounds++;
		stages = 0;
		List<AdventureCard> cards = discardAfterWinningTest();
		logger.info("Willing to bid: " + cards);
		logger.info("Prev bid: " + prevBid);
		if(cards == null || cards.size() < prevBid) {
			return -1;
		} else {
			return cards.size();
		}
	}
	
	@Override
	public List<AdventureCard> discardAfterWinningTest() {
		return this.discardAfterWinningTest(logger, rounds, bpc, player, pm);
	}
}
