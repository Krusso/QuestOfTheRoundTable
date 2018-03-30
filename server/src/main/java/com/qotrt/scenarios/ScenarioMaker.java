package com.qotrt.scenarios;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.TestCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.deck.DeckManager;
import com.qotrt.gameplayer.Player;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.RiggedModel.RIGGED;

public class ScenarioMaker {

	private ArrayList<GenericPairTyped<Function<RIGGED, Boolean>, Consumer<GenericPairTyped<Player, Integer>>>> scenarios(DeckManager dm){
		ArrayList<GenericPairTyped<Function<RIGGED, Boolean>, Consumer<GenericPairTyped<Player, Integer>>>> scenarios = 
				new ArrayList<GenericPairTyped<Function<RIGGED, Boolean>, Consumer<GenericPairTyped<Player, Integer>>>>();

		Function<RIGGED, Boolean> cd1 = x -> x.equals(RIGGED.ONE) || x.equals(RIGGED.ONESTAGETOURNAMENT)
				|| x.equals(RIGGED.TWOSTAGETOURNAMENT) || x.equals(RIGGED.ONEHUNDREDSTAGETOURNAMENT) || x.equals(RIGGED.KINGSCALLTOARMS);
		Consumer<GenericPairTyped<Player, Integer>> re1 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
			cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
			cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
			cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
			cards.add(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
			cards.add(new TestCard("Test of Valor", TYPE.TESTS));
			cards.add(new FoeCard("Saxons",10,20, TYPE.FOES));
			cards.add(new FoeCard("Saxons",10,20, TYPE.FOES));
			cards.add(new FoeCard("Boar",5,15, TYPE.FOES));
			cards.add(new FoeCard("Thieves",5, TYPE.FOES));
			cards.add(new FoeCard("Thieves",5, TYPE.FOES));
			cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
			x.key.addCards(cards);
		};

		Function<RIGGED, Boolean> cd2 = x -> x.equals(RIGGED.TWO);
		Consumer<GenericPairTyped<Player, Integer>> re2 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(dm.getAdventureCard("Excalibur"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Test of Valor"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Green Knight"));
			if(x.value == 2) {
				cards.add(dm.getAdventureCard("Merlin"));
			} else {
				cards.add(dm.getAdventureCard("Dragon"));
			}
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Dagger"));
			x.key.addCards(cards);
		};

		Function<RIGGED, Boolean> cd3 = x -> x.equals(RIGGED.THREE);
		Consumer<GenericPairTyped<Player, Integer>> re3 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(dm.getAdventureCard("Boar"));
			if(x.value == 3) {
				cards.add(dm.getAdventureCard("Excalibur"));
			} else {
				cards.add(dm.getAdventureCard("Sword"));
			}

			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Amour"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Horse"));

			if(x.value == 1) {
				cards.add(dm.getAdventureCard("Merlin"));	
			} else {
				cards.add(dm.getAdventureCard("Sword"));
			}
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Dagger"));
			x.key.addCards(cards);
		};

		Function<RIGGED, Boolean> cd4 = x -> x.equals(RIGGED.THREE);
		Consumer<GenericPairTyped<Player, Integer>> re4 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			if(x.value == 2) {
				cards.add(dm.getAdventureCard("Excalibur"));
			} else {
				cards.add(dm.getAdventureCard("Sword"));
			}

			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Horse"));
			cards.add(dm.getAdventureCard("Dagger"));
			cards.add(dm.getAdventureCard("Boar"));
			cards.add(dm.getAdventureCard("Saxons"));

			if(x.value == 2) {
				cards.add(dm.getAdventureCard("Evil Knight"));
				cards.add(dm.getAdventureCard("Giant"));
			} else {
				cards.add(dm.getAdventureCard("Horse"));
				cards.add(dm.getAdventureCard("Sword"));
			}

			cards.add(dm.getAdventureCard("Thieves"));
			x.key.addCards(cards);
		};

		Function<RIGGED, Boolean> cd5 = x -> x.equals(RIGGED.LONG) || x.equals(RIGGED.PROSPERITY);
		Consumer<GenericPairTyped<Player, Integer>> re5 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			if(x.value == 2) {
				cards.add(dm.getAdventureCard("Sir Percival"));
			} else {
				cards.add(dm.getAdventureCard("King Arthur"));
			}

			if(x.value == 1) {
				cards.add(dm.getAdventureCard("Sir Gawain"));
			} else {
				cards.add(dm.getAdventureCard("Queen Guinevere"));
			}

			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Robber Knight"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Mordred"));
			cards.add(dm.getAdventureCard("Green Knight"));

			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Amour"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Excalibur"));
			x.key.addCards(cards);
		};

		Function<RIGGED, Boolean> cd10 = x -> x.equals(RIGGED.AITOURNAMENT);
		Consumer<GenericPairTyped<Player, Integer>> re10 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Robber Knight"));
			cards.add(dm.getAdventureCard("Black Knight"));
			cards.add(dm.getAdventureCard("Mordred"));
			if(x.value == 1) {
				cards.add(dm.getAdventureCard("Dragon"));
			} else {
				cards.add(dm.getAdventureCard("Green Knight"));
			}
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Amour"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			if(x.value == 1 || x.value == 2) {
				cards.add(dm.getAdventureCard("Excalibur"));
			} else {
				cards.add(dm.getAdventureCard("Battle-ax"));
			}
			x.key.addCards(cards);
			x.key.changeShields(18);
			x.key.increaseLevel();
		};

		Function<RIGGED, Boolean> cd6 = x -> x.equals(RIGGED.AIQUEST);
		Consumer<GenericPairTyped<Player, Integer>> re6 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			// can be freed up to be anything
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Boar"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Dagger"));
			cards.add(dm.getAdventureCard("Evil Knight"));
			cards.add(dm.getAdventureCard("Mordred"));
			cards.add(dm.getAdventureCard("Amour"));
			x.key.addCards(cards);
			x.key.changeShields(20);
			x.key.increaseLevel();
		};


		Function<RIGGED, Boolean> cd7 = x -> x.equals(RIGGED.AIQUEST1);
		Consumer<GenericPairTyped<Player, Integer>> re7= x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(dm.getAdventureCard("Thieves"));
			if(x.value == 1) {
				cards.add(dm.getAdventureCard("Green Knight"));
			} else {
				cards.add(dm.getAdventureCard("Thieves"));
			}
			if(x.value == 1) {
				cards.add(dm.getAdventureCard("Test of Valor"));
			} else {
				cards.add(dm.getAdventureCard("Test of Morgan Le Fey"));
			}

			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Robber Knight"));
			cards.add(dm.getAdventureCard("Robber Knight"));
			cards.add(dm.getAdventureCard("Mordred"));
			cards.add(dm.getAdventureCard("Amour"));
			cards.add(dm.getAdventureCard("Saxon Knight"));
			cards.add(dm.getAdventureCard("Dagger"));

			x.key.addCards(cards);
			x.key.changeShields(20);
			x.key.increaseLevel();
		};

		Function<RIGGED, Boolean> cd8 = x -> x.equals(RIGGED.AIQUEST2);
		Consumer<GenericPairTyped<Player, Integer>> re8 = x -> {
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(dm.getAdventureCard("Thieves"));
			if(x.value == 1) {
				cards.add(dm.getAdventureCard("King Pellinore"));
			} else {
				cards.add(dm.getAdventureCard("Test of Valor"));
			}

			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Boar"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Amour"));
			cards.add(dm.getAdventureCard("Amour"));
			if(x.value == 1) {
				cards.add(dm.getAdventureCard("Sir Lancelot"));
			} else {
				cards.add(dm.getAdventureCard("Excalibur"));
			}

			x.key.addCards(cards);
		};

		Function<RIGGED, Boolean> cd9 = x -> x.equals(RIGGED.GAMEEND);
		Consumer<GenericPairTyped<Player, Integer>> re9 = x -> {

			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			// can be freed up to be anything
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Thieves"));
			cards.add(dm.getAdventureCard("Saxons"));
			cards.add(dm.getAdventureCard("Boar"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Sword"));
			cards.add(dm.getAdventureCard("Battle-ax"));
			cards.add(dm.getAdventureCard("Lance"));
			cards.add(dm.getAdventureCard("Dagger"));
			cards.add(dm.getAdventureCard("Evil Knight"));
			cards.add(dm.getAdventureCard("Mordred"));
			cards.add(dm.getAdventureCard("Amour"));
			x.key.addCards(cards);
			x.key.changeShields(22);
			x.key.increaseLevel();
		};

		scenarios.add(new GenericPairTyped<>(cd1, re1));
		scenarios.add(new GenericPairTyped<>(cd2, re2));
		scenarios.add(new GenericPairTyped<>(cd3, re3));
		scenarios.add(new GenericPairTyped<>(cd4, re4));
		scenarios.add(new GenericPairTyped<>(cd5, re5));
		scenarios.add(new GenericPairTyped<>(cd6, re6));
		scenarios.add(new GenericPairTyped<>(cd7, re7));
		scenarios.add(new GenericPairTyped<>(cd8, re8));
		scenarios.add(new GenericPairTyped<>(cd9, re9));
		scenarios.add(new GenericPairTyped<>(cd10, re10));
		return scenarios;
	}


	public void getHandRigged(int i, Player player, RIGGED rigged, DeckManager dm){
		ArrayList<GenericPairTyped<Function<RIGGED,Boolean>,Consumer<GenericPairTyped<Player,Integer>>>> x = scenarios(dm);
		for(int count = 0; count < x.size(); count++) {
			if(x.get(count).key.apply(rigged)) {
				x.get(count).value.accept(new GenericPairTyped<Player, Integer>(player, i));
				return;
			}
		}
		
		player.addCards(dm.getAdventureCard(12));
	}


}
