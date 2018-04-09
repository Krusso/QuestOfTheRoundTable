package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;
import com.qotrt.model.BoardModelMediator;

public class A3 extends AbstractAI implements AICommonImplementation {
	final static Logger logger = LogManager.getLogger(A3.class);

	private BoardModelMediator bmm;

	public A3(Player player, PlayerManager pm, BoardModelMediator bmm) {
		super(player, pm);
		this.bmm = bmm;
	}

	@Override
	public boolean doIParticipateInTournament() {
		logger.info("Participate in tournament");
		for(Player p: pm.players) {
			if(p.hand.size() > player.hand.size() && p != player) {
				logger.info("Someone has a bigger hand size dont participate");
				return false;
			}
		}
		logger.info("No one has a bigger hand size participate");
		return true;
	}

	@Override
	public List<AdventureCard> playCardsForTournament() {
		int maxCards = Integer.MIN_VALUE;
		for(Player p: bmm.getTournamentModel().playersWhoJoined()) {
			logger.info("Comparing: " + p.hand.size());
			if(p.hand.size() > maxCards && p != player) {
				maxCards = p.hand.size();
			}
		}

		int cardsToPlay = player.hand.size() - maxCards;
		logger.info("Cards to play: " + cardsToPlay);
		List<AdventureCard> cardsToUse = new ArrayList<AdventureCard>();
		if(cardsToPlay >= 0) {
			List<AdventureCard> alliesOrWeapons = bpc.uniqueListOfTypeDecreasingBp(player, null, pm.iseultExists(), 
					TYPE.ALLIES, 
					TYPE.WEAPONS, 
					TYPE.AMOUR);

			logger.info("Cards can use: " + alliesOrWeapons);
			for(int i = 0; i < cardsToPlay && i < alliesOrWeapons.size(); i++) {
				cardsToUse.add(alliesOrWeapons.get(i));
			}
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
		logger.info("Asking A3 if wants to join quest");
		rounds = 0;
		stages = 0;

		int numStages = bmm.getQuestModel().getQuest().getNumStages();
		for(int i = 0; i < numStages; i++) {
			if(bmm.getQuestModel().getQuest().getStage(i).getStageCards().size() >= 3) {
				return false;
			}
		}

		return true;
	}

	@Override
	public List<AdventureCard> playCardsForFoeQuest(QuestCard questCard) {
		logger.info("Asking A3 to play cards for foe quest");
		int numCardsInStage = bmm.getQuestModel().getQuest().getCurrentStageCards().size();

		List<AdventureCard> cardsToUse = new ArrayList<AdventureCard>();
		List<AdventureCard> alliesOrWeapons = bpc.uniqueListOfTypeDecreasingBp(player, null, pm.iseultExists(), 
				TYPE.ALLIES, 
				TYPE.WEAPONS, 
				TYPE.AMOUR);

		logger.info("Cards can use: " + alliesOrWeapons);
		for(int i = 0; i < numCardsInStage && i < alliesOrWeapons.size(); i++) {
			cardsToUse.add(alliesOrWeapons.get(i));
		}

		logger.info("Played cards: " + Arrays.toString(cardsToUse.stream().map(i -> i.getName()).toArray(String[]::new)));
		return cardsToUse;


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
