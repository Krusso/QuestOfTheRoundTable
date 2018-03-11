package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.Card;
import com.qotrt.cards.FoeCard;
import com.qotrt.cards.TestCard;
import com.qotrt.cards.WeaponCard;
import com.qotrt.deck.DeckManager;
import com.qotrt.gameplayer.Player.STATE;
import com.qotrt.model.RiggedModel.RIGGED;


public class PlayerManager {

	private int actualPlayer = -1;
	private int currentPlayer = -1;
	// Note: using arraylists but for now only have one instance of a view. Might change
	//private ArrayList<PlayersView> pvs = new ArrayList<PlayersView>();
	private DeckManager dm;
	private RIGGED rigged;
	//private DiscardSequenceManager dsm;

	public Player[] players;
	public PlayerManager(int numPlayers, DeckManager dm, RIGGED rigged2) {
		this.players = new Player[numPlayers];
		this.dm = dm;
		this.rigged = rigged2;
		for(int i = numPlayers; i > 0; i--) {
			players[i - 1] = new Player(i - 1);
		}
	}

	// Used just so there is an animation at the start of all players getting cards
	public void start() {
		dm.setRigged(rigged);
		for(int i = players.length; i > 0; i--) {
			if(rigged.equals(RIGGED.ONE)) {
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
				players[i - 1].addCards(cards);
			} else if(rigged.equals(RIGGED.TWO)) {
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
				if(i == 2) {
					cards.add(dm.getAdventureCard("Merlin"));
				} else {
					cards.add(dm.getAdventureCard("Dragon"));
				}
				cards.add(dm.getAdventureCard("Thieves"));
				cards.add(dm.getAdventureCard("Dagger"));
				players[i - 1].addCards(cards);
			} else if(rigged.equals(RIGGED.THREE)){
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.add(dm.getAdventureCard("Boar"));
				if(i == 3) {
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
				
				if(i == 1) {
					cards.add(dm.getAdventureCard("Merlin"));	
				} else {
					cards.add(dm.getAdventureCard("Sword"));
				}
				cards.add(dm.getAdventureCard("Thieves"));
				cards.add(dm.getAdventureCard("Dagger"));
				players[i - 1].addCards(cards);
			} else if (rigged.equals(RIGGED.FOUR)) {
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				if(i == 2) {
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
				
				if(i == 2) {
					cards.add(dm.getAdventureCard("Evil Knight"));
					cards.add(dm.getAdventureCard("Giant"));
				} else {
					cards.add(dm.getAdventureCard("Horse"));
					cards.add(dm.getAdventureCard("Sword"));
				}
				
				cards.add(dm.getAdventureCard("Thieves"));
				players[i - 1].addCards(cards);
			} else if (rigged.equals(RIGGED.LONG)) {
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				if(i == 2) {
					cards.add(dm.getAdventureCard("Sir Percival"));
				} else {
					cards.add(dm.getAdventureCard("King Arthur"));
				}
				
				if(i == 1) {
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
				players[i - 1].addCards(cards);
			} else if(rigged.equals(RIGGED.AITOURNAMENT)) {
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.add(dm.getAdventureCard("Thieves"));
				cards.add(dm.getAdventureCard("Saxons"));
				cards.add(dm.getAdventureCard("Thieves"));
				cards.add(dm.getAdventureCard("Robber Knight"));
				cards.add(dm.getAdventureCard("Black Knight"));
				cards.add(dm.getAdventureCard("Mordred"));
				if(i == 1) {
					cards.add(dm.getAdventureCard("Dragon"));
				} else {
					cards.add(dm.getAdventureCard("Green Knight"));
				}
				cards.add(dm.getAdventureCard("Lance"));
				cards.add(dm.getAdventureCard("Lance"));
				cards.add(dm.getAdventureCard("Amour"));
				cards.add(dm.getAdventureCard("Battle-ax"));
				if(i == 1 || i == 2) {
					cards.add(dm.getAdventureCard("Excalibur"));
				} else {
					cards.add(dm.getAdventureCard("Battle-ax"));
				}
				players[i - 1].addCards(cards);
				players[i - 1].changeShields(18);
				players[i - 1].increaseLevel();
			} else if(rigged.equals(RIGGED.AIQUEST)) {
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
				players[i - 1].addCards(cards);
				players[i - 1].changeShields(20);
				players[i - 1].increaseLevel();
			} else if(rigged.equals(RIGGED.AIQUEST1)) {
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.add(dm.getAdventureCard("Thieves"));
				if(i == 1) {
					cards.add(dm.getAdventureCard("Green Knight"));
				} else {
					cards.add(dm.getAdventureCard("Thieves"));
				}
				if(i == 1) {
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
				
				players[i - 1].addCards(cards);
				players[i - 1].changeShields(20);
				players[i - 1].increaseLevel();
			} else if(rigged.equals(RIGGED.AIQUEST2)) {
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.add(dm.getAdventureCard("Thieves"));
				if(i == 1) {
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
				if(i == 1) {
					cards.add(dm.getAdventureCard("Sir Lancelot"));
				} else {
					cards.add(dm.getAdventureCard("Excalibur"));
				}
				
				players[i - 1].addCards(cards);
			} else if(rigged.equals(RIGGED.GAMEEND)) {
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
				players[i - 1].addCards(cards);
				players[i - 1].changeShields(22);
				players[i - 1].increaseLevel();
			} else {
				players[i - 1].addCards(dm.getAdventureCard(12));	
			}
		}
	}

//	public void setDiscardSequenceManager(DiscardSequenceManager dsm) {
//		this.dsm = dsm;
//	}
	
	public void drawCards(List<Player> players, int cards) {
		players.forEach(player -> {
			player.addCards(dm.getAdventureCard(cards));
		});
		//if(dsm != null) dsm.start(null, null, null);
	}
	
	public void setPlayer(Player playerFind) {
		for(int i = 0; i < players.length; i++) {
			if(players[i]== playerFind) {
				if(currentPlayer == i) return;
				currentPlayer = i;
			}
		}
		//pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand.getDeck()));
	}

	public void nextTurn() {
		// TODO: probably need to reset some attributes there
		// like questioning etc
		actualPlayer++;
		if(actualPlayer >= players.length) {
			actualPlayer = 0;
		}
		currentPlayer = actualPlayer;
		//pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand.getDeck()));
	}


//	public void subscribe(PlayersView psv) {
//		pvs.add(psv);
//	}

//	public void subscribe(PlayerView pv) {
//		for(Player player: players) {
//			player.subscribe(pv);
//		}
//	}

	public Iterator<Player> round(){
		List<Player> list = new ArrayList<Player>(Arrays.asList(players)).subList(actualPlayer, players.length);
		list.addAll(new ArrayList<Player>(Arrays.asList(players)).subList(0, actualPlayer));
		return list.iterator();
	}

	public void flushState() {
		for(int i = players.length; i > 0; i--) {
			players[i - 1].setState(Player.STATE.NEUTRAL);
		}
	}

	public void setState(Player partipcipants, Player.STATE state, int i, TYPE weapons) {
		partipcipants.setState(state, i, weapons);
	}

	public void setState(Player participant, Player.STATE state) {
		participant.setState(state);
	}

	public void setState(Player participant, STATE sponsoring, int numStages) {
		participant.setState(sponsoring, numStages);
	}

	public List<Player> getAllWithState(Player.STATE state) {
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(round(), Spliterator.ORDERED),
				false)
				.filter(i -> i.getQuestion() == state)
				.collect(Collectors.toList());
	}

	public void currentFaceDown(String[] cards) {
		players[currentPlayer].setFaceDown(cards);
	}


	public void questDown(Player sponsor, List<List<Card>> cards) {
		sponsor.setQuestDown(cards);
	}

	public void flipStage(Player sponsor, int stage) {
		sponsor.flipStage(stage);
	}

	public void flipCards(Iterator<Player> players) {
		players.forEachRemaining(i -> i.flipCards());
		//pvs.forEach(i -> i.showFaceUp(this.round()));
	}

	public void changeShields(List<Player> winners, int shields) {
		winners.forEach(i -> i.changeShields(shields));
	}


	public void discardCards(List<Player> participants) {
		participants.forEach(i -> {
			i.discardType(TYPE.WEAPONS);
			i.discardType(TYPE.AMOUR);
		});
	}

	public void discardWeapons(List<Player> participants) {
		participants.forEach(i -> {
			i.discardType(TYPE.WEAPONS);
		});
	}

	public void discardAllies(List<Player> participants) {
		participants.forEach(i -> {
			i.discardType(TYPE.ALLIES);
		});
	}

	public void discardFromHand(Player player, String[] cards) {
		dm.addAdventureCard(player.removeCards(cards));
	}

	public boolean rankUp() {
		AtomicBoolean winners = new AtomicBoolean();
		round().forEachRemaining(player ->{
			player.increaseLevel();
			if(player.getRank() == Rank.RANKS.KNIGHTOFTHEROUNDTABLE) {
				player.setState(Player.STATE.WINNING);
				winners.set(true);
			}
		});
		return winners.get();
	}

	public void setBidAmount(Player next, STATE bidding, int maxBidValue, int i) {
		next.setBidAmount(bidding, maxBidValue, i);
	}

	public void setDiscarding(Player player, STATE testdiscard, int cardsToBid) {
		player.setDiscardAmount(testdiscard, cardsToBid);
	}

//	public void setStates(List<Player> winners, STATE win) {
//		winners.forEach(i -> i.setState(win));
//		pvs.forEach(i -> i.win(winners, win));
//	}
//	
//	public void passStage(List<Player> winners) {
//		pvs.forEach(i -> i.passStage(winners));
//	}
//
//	public void passQuest(List<Player> winners) {
//		pvs.forEach(i -> i.passQuest(winners));
//	}
//
//	public void sendContinue(String string) {
//		pvs.forEach(i -> i.sendContinue(string));
//	}
//
//	public void showTournamentTie(List<Player> winners) {
//		pvs.forEach(i -> i.showTournamentTie(winners));
//	}


}
