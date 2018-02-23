package src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import src.game_logic.AdventureCard;
import src.game_logic.AllyCard;
import src.game_logic.DeckManager;
import src.game_logic.FoeCard;
import src.game_logic.Rank;
import src.game_logic.TestCard;
import src.game_logic.WeaponCard;
import src.player.Player.STATE;
import src.sequence.DiscardSequenceManager;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.Card;
import src.views.PlayerView;
import src.views.PlayersView;

public class PlayerManager {

	private int actualPlayer = -1;
	private int currentPlayer = -1;
	// Note: using arraylists but for now only have one instance of a view. Might change
	private ArrayList<PlayersView> pvs = new ArrayList<PlayersView>();
	private DeckManager dm;
	private boolean rigged;
	private DiscardSequenceManager dsm;

	public Player[] players;
	public PlayerManager(int numPlayers, DeckManager dm, boolean rigged) {
		this.players = new Player[numPlayers];
		this.dm = dm;
		this.rigged = rigged;
		for(int i = numPlayers; i > 0; i--) {
			players[i - 1] = new Player(i - 1);
		}
	}

	// Used just so there is an animation at the start of all players getting cards
	public void start() {
		for(int i = players.length; i > 0; i--) {
			if(rigged) {
				ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
				cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
				cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
				cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
				cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
				//cards.add(new WeaponCard("Battle-ax", 15, TYPE.WEAPONS));
				cards.add(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
//				cards.add(new WeaponCard("Sword",10, TYPE.WEAPONS));
				//cards.add(new WeaponCard("Sword",10, TYPE.WEAPONS));
				cards.add(new TestCard("Test of Valor", TYPE.TESTS));
				cards.add(new FoeCard("Saxons",10,20, TYPE.FOES));
				cards.add(new FoeCard("Green Knight",25,40, TYPE.FOES));
				cards.add(new FoeCard("Saxons",10,20, TYPE.FOES));
//				cards.add(new FoeCard("Thieves",5, TYPE.FOES));
				//cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
				cards.add(new AllyCard("Merlin",10,10,2, TYPE.ALLIES));
				//cards.add(new FoeCard("Mordred", 30, 30, TYPE.ALLIES));
				cards.add(new FoeCard("Thieves",5, TYPE.FOES));
				//cards.add(new TestCard("Test of the Questing Beast", TYPE.TESTS));
				//cards.add(new AllyCard("King Arthur",10,10,2, TYPE.ALLIES));
				//cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
				//cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
				cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
//				cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
//				cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
//				cards.add(new AmourCard("Amour",10,1, TYPE.AMOUR));
//				cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
				players[i - 1].addCards(cards);
			} else {
				players[i - 1].addCards(dm.getAdventureCard(12));	
			}
			//players[i - 1].changeShields(100);
		}
	}

	public void setDiscardSequenceManager(DiscardSequenceManager dsm) {
		this.dsm = dsm;
	}
	
	public void drawCards(List<Player> players, int cards) {
		players.forEach(player -> {
			player.addCards(dm.getAdventureCard(cards));
		});
		if(dsm != null) dsm.start(null, null, null);
	}
	
	public void setPlayer(Player playerFind) {
		for(int i = 0; i < players.length; i++) {
			if(players[i]== playerFind) {
				if(currentPlayer == i) return;
				currentPlayer = i;
			}
		}
		pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand.getDeck()));
	}

	public void nextPlayer() {
		currentPlayer++;
		if(currentPlayer >= players.length) {
			currentPlayer = 0;
		}
		pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand.getDeck()));
	}

	public void nextTurn() {
		// TODO: probably need to reset some attributes there
		// like questioning etc
		actualPlayer++;
		if(actualPlayer >= players.length) {
			actualPlayer = 0;
		}
		currentPlayer = actualPlayer;
		pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand.getDeck()));
	}


	public void subscribe(PlayersView psv) {
		pvs.add(psv);
	}

	public void subscribe(PlayerView pv) {
		for(Player player: players) {
			player.subscribe(pv);
		}
	}

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

	// I believe this is only used for the one event where a player must discard 2 weapons or if not possible 2 foes
	public void setState(List<Player> partipcipants, Player.STATE state, int i, TYPE weapons) {
		partipcipants.forEach(e -> e.setState(state, i, weapons));
	}

	public void setState(Player partipcipants, Player.STATE state, int i, TYPE weapons) {
		partipcipants.setState(state, i, weapons);
	}

	public void setState(Player participant, Player.STATE state) {
		participant.setState(state);
	}

	public void setState(List<Player> participants, Player.STATE state) {
		participants.forEach(e -> e.setState(state));
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
		// TODO: should be its own method imo
		//		for(int i = 0; i < players.length; i++) {
		//			if(players[i]== next) {
		//				players[i].flipCards();
		//				return;
		//			}
		//		}
		players.forEachRemaining(i -> i.flipCards());
		pvs.forEach(i -> i.showFaceUp(this.round()));
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
		player.removeCards(cards);
	}


	public void discardFromHand(int player, String[] cards) {
		discardFromHand(players[player], cards);
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

	public void setStates(List<Player> winners, STATE win) {
		winners.forEach(i -> i.setState(win));
		pvs.forEach(i -> i.win(winners, win));
	}
	public void passStage(List<Player> winners) {
		pvs.forEach(i -> i.passStage(winners));
	}

	public void passQuest(List<Player> winners) {
		pvs.forEach(i -> i.passQuest(winners));
	}

	public void sendContinue(String string) {
		pvs.forEach(i -> i.sendContinue(string));
	}


}
