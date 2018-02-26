package src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.QuestCard;
import src.game_logic.StoryCard;

public class A2 extends AbstractAI {
	final static Logger logger = LogManager.getLogger(A2.class);
	
	public A2(UIPlayer player, UIPlayerManager pm) {
		super(player, pm);
	}
	
	@Override
	public boolean doIParticipateInTournament() {
		logger.info("Participate in tournament");
		return true;
	}


	@Override
	public List<AdventureCard> playCardsForTournament() {
		int currentBp = bpc.calculatePoints(player, pm.iseultExists());
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
		if(playerCanWinOrEvolve(pm)) {
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
				logger.info("A2 doesnt want to sponsor tournament not enough different bp foes/tests");
				return null;	
			}
			int pointsInStage = bc.calculateStage(this.pm.players[pm.getCurrentPlayer()].hand, 
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

	@Override
	public boolean doIParticipateInQuest(QuestCard questCard) {
		logger.info("Asking A2 if wants to join tournament");
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
		logger.info("Discarding cards after round: " + rounds);
		if(rounds == 1) {
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
			return cards;
		} else {
			logger.info("Past round 2 not willing to bid");
			return null;
		}
	}
}
