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
import src.game_logic.DeckManager;
import src.game_logic.Rank;
import src.game_logic.WeaponCard;
import src.game_logic.AdventureCard.TYPE;
import src.views.PlayerView;
import src.views.PlayersView;

public class PlayerManager {

	private int actualPlayer = -1;
	private int currentPlayer = -1;
	// Note: using arraylists but for now only have one instance of a view. Might change
	private ArrayList<PlayersView> pvs = new ArrayList<PlayersView>();
	private DeckManager dm;

	private Player[] players;

	public PlayerManager(int numPlayers, DeckManager dm) {
		this.players = new Player[numPlayers];
		this.dm = dm;

		for(int i = numPlayers; i > 0; i--) {
			players[i - 1] = new Player(i - 1);
		}
	}

	// Used just so there is an animation at the start of all players getting cards
	public void start() {
		for(int i = players.length; i > 0; i--) {
			/**
			 *  RIGGING the game needs to be removed/be triggered another way
			 *  maybe a message?
			 *  game deck: rigged?
			 */
			ArrayList<AdventureCard> cards = new ArrayList<AdventureCard>();
			cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
			cards.add(new WeaponCard("Excalibur",30, TYPE.WEAPONS));
			cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
			cards.add(new WeaponCard("Lance",20, TYPE.WEAPONS));
			cards.add(new WeaponCard("Battle-ax", 15, TYPE.WEAPONS));
			cards.add(new WeaponCard("Battle-ax",15, TYPE.WEAPONS));
			cards.add(new WeaponCard("Sword",10, TYPE.WEAPONS));
			cards.add(new WeaponCard("Sword",10, TYPE.WEAPONS));
			cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
			cards.add(new WeaponCard("Horse",10, TYPE.WEAPONS));
			cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
			cards.add(new WeaponCard("Dagger",5, TYPE.WEAPONS));
			//players[i - 1].addCards(dm.getAdventureCard(12));
			players[i - 1].addCards(cards);
		}
	}


	public void drawCards(List<Player> players, int cards) {
		players.forEach(player -> player.addCards(dm.getAdventureCard(cards)));
	}
	
	public void drawCards(Player player, int cards) {
		player.addCards(dm.getAdventureCard(cards));
	}
	
	public void setPlayer(Player playerFind) {
		for(int i = 0; i < players.length; i++) {
			if(players[i]== playerFind) {
				if(currentPlayer == i) return;
				currentPlayer = i;
			}
		}
		pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand()));
	}

	public void nextPlayer() {
		currentPlayer++;
		if(currentPlayer >= players.length) {
			currentPlayer = 0;
		}
		pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand()));
	}

	public void nextTurn() {
		// TODO: probably need to reset some attributes there
		// like questioning etc
		actualPlayer++;
		if(actualPlayer >= players.length) {
			actualPlayer = 0;
		}
		currentPlayer = actualPlayer;
		pvs.forEach(i -> i.update(currentPlayer, players[currentPlayer].hand()));
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
		List<Player> list = Arrays.asList(players).subList(actualPlayer, players.length);
		list.addAll(Arrays.asList(players).subList(0, actualPlayer));
		return list.iterator();
	}
	
	public void flushState() {
		for(int i = players.length; i > 0; i--) {
			players[i - 1].setState(Player.STATE.NEUTRAL);
		}
	}
	
	public void currentQuestionQuest() {
		players[currentPlayer].setState(Player.STATE.QUESTQUESTIONED);
	}
	
	public void currentSetupQuest() {
		// should we have a separate state for setting up a quest?
		players[currentPlayer].setState(Player.STATE.PICKING);
	}
	
	public void currentSponsorQuest() {
		players[currentPlayer].setState(Player.STATE.YES);
	}
	
	public void currentJoinQuest() {
		players[currentPlayer].setState(Player.STATE.YES);
	}
	
	public void currentDeclineQuest() {
		players[currentPlayer].setState(Player.STATE.NO);
	}
	
	public void currentQuestionTournament() {
		players[currentPlayer].setState(Player.STATE.QUESTIONED);
	}
	
	public void currentQuestionCards() {
		players[currentPlayer].setState(Player.STATE.PICKING);
	}

	public void currentDiscard(int i, TYPE weapons) {
		players[currentPlayer].setState(Player.STATE.DISCARDING, i, weapons);
	}
	
	// TODO: bounds checking
	public void currentAcceptTournament() {
		players[currentPlayer].setState(Player.STATE.YES);
	}

	public void currentDeclineTournament() {
		players[currentPlayer].setState(Player.STATE.NO);
	}

	public List<Player> getAllWithState(Player.STATE state) {
		return StreamSupport.stream(
		          Spliterators.spliteratorUnknownSize(round(), Spliterator.ORDERED),
		          false)
				.filter(i -> i.getQuestion() == state)
				.collect(Collectors.toList());
	}

	public void currentFaceDown(String cards) {
		players[currentPlayer].setFaceDown(cards.split(","));
	}

	public void currentFaceUp(String cards) {
		players[currentPlayer].setFaceUp(cards.split(","));
	}
	
	public void questDown(Player sponsor, List cards) {
		sponsor.setQuestDown(cards);
	}
	
	public void flipStage(Player sponsor, int stage) {
		sponsor.flipStage(stage);
	}
	
	public void setTournamentWinner(List<Player> participants) {
		participants.forEach(i -> i.setState(Player.STATE.WIN));
	}

	public void flipCards(Player next) {
		// TODO: should be its own method imo
		for(int i = 0; i < players.length; i++) {
			if(players[i]== next) {
				players[i].flipCards();
				return;
			}
		}
	}

	public void changeShields(List<Player> winners, int shields) {
		winners.forEach(i -> i.changeShields(shields));
	}


	public void discardCards(List<Player> participants) {
		participants.forEach(i -> {
			i.discardWeapons();
			i.discardAmours();
		});
	}

	public void discardWeapons(List<Player> participants) {
		participants.forEach(i -> {
			i.discardWeapons();
		});
	}

	public void discardAllies(List<Player> participants) {
		participants.forEach(i -> {
			i.discardAllies();
		});
	}

	public void discardFromHand(Player player, String cards) {
		player.removeCards(cards.split(" "));
	}
	
	public void discardFaceUp(Player player) {
		player.discardFaceUp();
	}
	
	public boolean rankUp() {
		AtomicBoolean winners = new AtomicBoolean();
		round().forEachRemaining(player ->{
			player.increaseLevel();
			if(player.getRank() == Rank.RANKS.KNIGHTOFTHEROUNDTABLE) {
				player.setState(Player.STATE.WINNING);
				winners.set(true);;
			}
		});
		return winners.get();
	}

	public void setGameWinners(List<Player> winners) {
		winners.forEach(player -> {
			player.setState(Player.STATE.GAMEWON);
		});
	}
}

