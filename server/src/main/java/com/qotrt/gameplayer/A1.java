package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;
import com.qotrt.model.BoardModelMediator;

public class A1 extends AbstractAI implements AICommonImplementation{

	final static Logger logger = LogManager.getLogger(A1.class);

	private BoardModelMediator bmm;
	
	public A1(Player player, PlayerManager pm, BoardModelMediator bmm) {
		super(player, pm);
		this.bmm = bmm;
	}

	@Override
	public boolean doIParticipateInTournament() {
		logger.info("Asking A1 if want to join in tournament");
		if(playerCanWinOrEvolve(pm)) {
			logger.info("A1 joins tournament");
			return true;
		}

		logger.info("A1 doesnt join tournament");
		return false;
	}

	@Override
	public List<AdventureCard> playCardsForTournament() {
		logger.info("AI choosing cards for tournament");
		List<AdventureCard> cards = new ArrayList<AdventureCard>();
		if(playerCanWinOrEvolveJoined(pm)) {
			List<AdventureCard> sortedweapons = bpc.uniqueListOfTypeDecreasingBp(player, TYPE.WEAPONS, null, pm.iseultExists());
			List<AdventureCard> sortedallies = bpc.listOfTypeDecreasingBp(player, TYPE.ALLIES, null, pm.iseultExists());
			cards.addAll(bpc.uniqueListOfTypeDecreasingBp(player, TYPE.AMOUR, null, pm.iseultExists()));
			cards.addAll(sortedweapons);
			cards.addAll(sortedallies);
			logger.info("Someone can win playing cards: " + cards);
			return cards;
		}

		List<AdventureCard> weapons = bpc.listOfTypeDecreasingBp(player, TYPE.WEAPONS, null, pm.iseultExists());
		Set<String> uniqueCards = new HashSet<String>();
		Set<String> added = new HashSet<String>();
		for(AdventureCard card: weapons) {
			if(uniqueCards.contains(card.getName()) && !added.contains(card.getName())) {
				cards.add(card);
				added.add(card.getName());
			} else {
				uniqueCards.add(card.getName());
			}
		}

		logger.info("Playing cards: " + cards);
		return cards;
	}

	private boolean playerCanWinOrEvolveJoined(PlayerManager pm) {
		for(Player p: bmm.getTournamentModel().playersWhoJoined()) {
			if(winPlayer(p)) {
				logger.info("Player: " + p + " can evolve/win");
				return true;
			}
		}
		return false;
	}

	@Override
	public List<List<AdventureCard>> doISponsorAQuest(QuestCard questCard) {
		return this.doISponsorAQuest(questCard, logger, 
				pm, player, playerCanWinOrEvolve(pm), bpc, 50);
	}

	@Override
	public boolean doIParticipateInQuest(QuestCard questCard) {
		rounds = 0;
		stages = 0;
		logger.info("Asking A1 if wants to join quest");
		int stages = questCard.getNumStages();
		int weaponOrAllyCount = player.getTypeCount(TYPE.WEAPONS) + player.getTypeCount(TYPE.ALLIES);
		if(stages*2 > weaponOrAllyCount) {
			logger.info("A1 doesnt want to join tournament not enough weapons or allies");
			return false;
		}

		int toDiscard = (int)player.hand.getDeck().stream().
				filter(i -> i.getType() == TYPE.FOES && i.getBattlePoints() < 20).
				count();

		if(toDiscard < 2) {
			logger.info("A1 doesnt want to join tournament not enough cards to discard");
			return false;
		}

		logger.info("A1 wants to join the tournament");
		return true;
	}

	@Override
	public List<AdventureCard> playCardsForFoeQuest(QuestCard questCard) {
		logger.info("Selecting cards for A1 to play");
		stages++;
		List<AdventureCard> cards = new ArrayList<AdventureCard>();
		if(stages >= questCard.getNumStages()) {
			logger.info("Last stage");
			cards.addAll(bpc.listOfTypeDecreasingBp(player, TYPE.ALLIES, questCard, pm.iseultExists()));
			cards.addAll(bpc.uniqueListOfTypeDecreasingBp(player, TYPE.AMOUR, questCard, pm.iseultExists()));
			cards.addAll(bpc.uniqueListOfTypeDecreasingBp(player, TYPE.WEAPONS, questCard, pm.iseultExists()));
		} else {
			logger.info("not last stage");
			cards.addAll(bpc.listOfTypeDecreasingBp(player, TYPE.ALLIES, questCard, pm.iseultExists()));
			cards.addAll(bpc.uniqueListOfTypeDecreasingBp(player, TYPE.AMOUR, questCard, pm.iseultExists()));
			while(cards.size() > 2) cards.remove(0);

			if(cards.size() != 2) {
				List<AdventureCard> weapons = bpc.uniqueListOfTypeDecreasingBp(player, TYPE.WEAPONS, questCard, pm.iseultExists());
				Collections.reverse(weapons);
				while(cards.size() < 2 && weapons.size() != 0) cards.add(weapons.remove(0));
			}

		}
		logger.info("A1 selected: " + cards);
		return cards;
	}

	@Override
	public int nextBid(int prevBid) {
		rounds++;
		stages++;
		List<AdventureCard> cards = discardAfterWinningTest();
		logger.info("Cards willing to bid: " + cards);
		logger.info("Prev bid: " + prevBid);
		if(cards == null || cards.size() < prevBid) {
			logger.info("Cant bid more or too many rounds");
			return -1;
		} else {
			logger.info("Bidding cards: " + cards);
			return cards.size();
		}
	}

	@Override
	public List<AdventureCard> discardAfterWinningTest() {
		if(rounds == 1) {
			logger.info("Round one finding foes under 20 to potentially discard");
			return bpc.listOfTypeDecreasingBp(player, TYPE.FOES, null, pm.iseultExists()).stream().
					filter(i -> i.getBattlePoints() < 20).collect(Collectors.toList());
		} else {
			logger.info("Past round 1 not willing to bid");
			return null;
		}
	}
}
