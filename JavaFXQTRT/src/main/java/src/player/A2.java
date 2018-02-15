package src.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.Card;
import src.game_logic.QuestCard;
import src.game_logic.Rank.RANKS;

public class A2 extends AbstractAI {

	public A2(UIPlayer player, UIPlayerManager pm) {
		super(player, pm);
	}
	
	@Override
	public boolean doIParticipateInTournament() {
		return true;
	}


	@Override
	public List<Card> playCardsForTournament() {
		BattlePointCalculator bpc = new BattlePointCalculator(pm);
		int currentBp = bpc.calculatePoints(listPlayer).get(0);
		if(currentBp >= 50) {
			return null;
		}
		List<AdventureCard> allies = player.listOfTypeDecreasingBp(TYPE.ALLIES);
		List<AdventureCard> weapons = player.uniqueListOfTypeDecreasingBp(TYPE.WEAPONS);
		// TODO: this doesnt account for synergies between cards
		List<Card> cardsToUse = new ArrayList<Card>();
		for(AdventureCard card: allies) {
			if(currentBp >= 50) {
				break;
			}
			cardsToUse.add(card);
			currentBp += card.getBattlePoints();
		}

		for(AdventureCard card: weapons) {
			if(currentBp >= 50) {
				break;
			}
			cardsToUse.add(card);
			currentBp += card.getBattlePoints();
		}

		return cardsToUse;

	}

	@Override
	public List<List<Card>> doISponserAQuest(List<Player> participants, QuestCard questCard) {
		for(Player player: participants) {
			if(player.getRank() == RANKS.CHAMPION && player.getShields() >= 10 - questCard.getNumStages()) {
				return new ArrayList<List<Card>>();
			}
		}

		List<List<Card>> cards = new ArrayList<List<Card>>();
		List<AdventureCard> sortedfoes = player.listOfTypeDecreasingBp(TYPE.FOES);
		List<AdventureCard> sortedweapons = player.uniqueListOfTypeDecreasingBp(TYPE.WEAPONS);
		List<AdventureCard> test = player.listOfTypeDecreasingBp(TYPE.TESTS);

		int stages = questCard.getNumStages();

		IntStream.range(0, stages + 1).forEach(i -> cards.add(new ArrayList<Card>()));

		//set up last stage to be atleast 40
		if(sortedfoes.size() <= 0) return new ArrayList<List<Card>>();

		AdventureCard biggestFoe = sortedfoes.remove(0);
		int bp = biggestFoe.getBattlePoints();
		cards.get(stages).add(biggestFoe);
		while(sortedweapons.size() != 0 && bp < 40) {
				AdventureCard biggestWeapon = sortedweapons.remove(0);
				bp += biggestWeapon.getBattlePoints();
				cards.get(stages).add(biggestWeapon);
		}

		if(bp < 40) return new ArrayList<List<Card>>();

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

		for(List<Card> i: cards) {
			if(i.size() == 0) return new ArrayList<List<Card>>();	
		}

		// TODO: make sure stage before last is less bp than last stage

		return cards;
	}

	@Override
	public boolean doIParticipateInQuest(QuestCard questCard) {
		List<AdventureCard> sortedfoes = player.listOfTypeDecreasingBp(TYPE.FOES);

		int tenIncrements = 0;
		for(AdventureCard card: player.hand.getDeck()) {
			if((card.getType() == TYPE.WEAPONS || card.getType() == TYPE.ALLIES
					|| card.getType() == TYPE.AMOUR) && card.getBattlePoints() == 10) {
				tenIncrements++;
			}
		}

		int foes = 0;
		for(AdventureCard foe: sortedfoes) {
			if(foe.getBattlePoints() < 25) {
				foes++;
			}
		}

		return (foes >= 2) && (tenIncrements >= questCard.getNumStages());
	}

	@Override
	public List<Card> playCardsForFoeQuest(boolean lastStage) {
		List<Card> cards = new ArrayList<Card>();

		List<AdventureCard> sortedweapons = player.uniqueListOfTypeDecreasingBp(TYPE.WEAPONS);
		List<AdventureCard> sortedallies = player.listOfTypeDecreasingBp(TYPE.ALLIES);
		sortedallies.addAll(player.listOfTypeDecreasingBp(TYPE.AMOUR));
		if(lastStage) {
			// if last stage add everything you can
			cards.addAll(sortedweapons);
			cards.addAll(sortedallies);
		} else {
			// play an incremement of +10 using amour, then ally, then weapons
			for(AdventureCard card: sortedallies) {
				if(card.getType().equals(TYPE.AMOUR)) {
					cards.add(card);
					return cards;
				}
			}
			for(AdventureCard card: sortedallies) {
				if(card.getBattlePoints() == 10) {
					cards.add(card);
					return cards;
				}
			}
			for(AdventureCard card: sortedweapons) {
				if(card.getBattlePoints() == 10) {
					cards.add(card);
					return cards;
				}
			}
		}
		return cards;
	}

	@Override
	public int nextBid(int round, int prevBid) {
		List<Card> cards = discardAfterWinningTest(round);
		if(cards == null || cards.size() <= prevBid) {
			return -1;
		} else {
			return cards.size();
		}
	}

	@Override
	public List<Card> discardAfterWinningTest(int round) {
		if(round == 1) {
			return player.listOfTypeDecreasingBp(TYPE.FOES).stream().
					filter(i -> i.getBattlePoints() < 25).collect(Collectors.toList());
		} else if(round == 2) {
			List<Card> cards = new ArrayList<Card>();
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
			return null;
		}
	}

}
