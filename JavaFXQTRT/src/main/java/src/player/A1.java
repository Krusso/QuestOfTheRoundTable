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
import src.game_logic.Rank;

public class A1 extends AbstractAI {

	public A1(UIPlayer player, UIPlayerManager pm) {
		super(player, pm);
	}
	
	@Override
	public boolean doIParticipateInTournament() {
		int length = pm.getNumPlayers();
		for(int i = 0; i < length; i++) {
			// TODO check shields as well
			if(pm.getPlayerRank(i) == Rank.RANKS.CHAMPION) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Card> playCardsForTournament() {
		// TODO check if anyone who has participated can win
		// if yes go HAM

		List<Card> cards = new ArrayList<Card>();
		List<AdventureCard> weapons = player.listOfTypeDecreasingBp(TYPE.WEAPONS);
		Set<String> uniqueCards = new HashSet<String>();
		for(AdventureCard card: weapons) {
			if(uniqueCards.contains(card.getName())) {
				cards.add(card);
			} else {
				uniqueCards.add(card.getName());
			}
		}

		return cards;
	}

	@Override
	// TODO
	public List<List<Card>> doISponserAQuest(List<Player> participants, QuestCard questCard) {
		int length = pm.getNumPlayers();
		for(int i = 0; i < length; i++) {
			// TODO check shields as well
			if(pm.getPlayerRank(i) == Rank.RANKS.CHAMPION) {
				return null;
			}
		}

		List<List<Card>> cards = new ArrayList<List<Card>>();
		List<AdventureCard> sortedfoes = player.listOfTypeDecreasingBp(TYPE.FOES);
		List<AdventureCard> sortedweapons = player.uniqueListOfTypeDecreasingBp(TYPE.WEAPONS);
		List<AdventureCard> test = player.listOfTypeDecreasingBp(TYPE.TESTS);

		int stages = questCard.getNumStages();

		IntStream.range(0, stages + 1).forEach(i -> cards.add(new ArrayList<Card>()));

		//set up last stage to be atleast 50
		if(sortedfoes.size() <= 0) return null;

		AdventureCard biggestFoe = sortedfoes.remove(0);
		int bp = biggestFoe.getBattlePoints();
		cards.get(stages).add(biggestFoe);
		while(sortedweapons.size() != 0) {
			if(bp < 50) {
				AdventureCard biggestWeapon = sortedweapons.remove(0);
				bp += biggestWeapon.getBattlePoints();
				cards.get(stages).add(biggestWeapon);
			}	
		}

		if(bp < 50) return null;

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
			if(i.size() == 0) return null;	
		}

		// TODO: make sure stage before last is less bp than last stage

		return cards;
	}

	@Override
	public boolean doIParticipateInQuest(QuestCard questCard) {
		int stages = questCard.getNumStages();
		int weaponorallycount = player.getTypeCount(TYPE.WEAPONS) + player.getTypeCount(TYPE.ALLIES);
		if(stages*2 > weaponorallycount) return false;

		int toDiscard = (int)player.getPlayerHandAsList().stream().
				filter(i -> i.getType() == TYPE.FOES && i.getBattlePoints() < 20).
				count();

		if(toDiscard < 2) return false;

		return true;

	}

	@Override
	public List<Card> playCardsForFoeQuest(boolean lastStage) {
		List<Card> cards = new ArrayList<Card>();
		if(lastStage) {
			cards.addAll(player.listOfTypeDecreasingBp(TYPE.ALLIES));
			cards.addAll(player.listOfTypeDecreasingBp(TYPE.AMOUR));
			cards.addAll(player.uniqueListOfTypeDecreasingBp(TYPE.WEAPONS));
		} else {
			cards.addAll(player.listOfTypeDecreasingBp(TYPE.ALLIES));
			cards.addAll(player.listOfTypeDecreasingBp(TYPE.AMOUR));
			while(cards.size() > 2) cards.remove(0);
			
			if(cards.size() != 2) {
				List<AdventureCard> weapons = player.uniqueListOfTypeDecreasingBp(TYPE.WEAPONS);
				Collections.reverse(weapons);
				while(cards.size() < 2 && weapons.size() != 0) cards.add(weapons.remove(0));
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
					filter(i -> i.getBattlePoints() < 20).collect(Collectors.toList());
		} else {
			return null;
		}
	}

}
