package com.qotrt.gameplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;

public abstract class AbstractAI {
	final static Logger logger = LogManager.getLogger(AbstractAI.class);
	
	protected int rounds = 0;
	protected int stages = 0;
	public Player player;
	protected List<Player> listPlayer;
	protected PlayerManager pm;
	protected BattlePointCalculator bpc;
	
	public AbstractAI(Player player, PlayerManager pm) {
		this.player = player;
		listPlayer = new ArrayList<Player>();
		listPlayer.add(player);
		this.pm = pm;
		this.bpc = new BattlePointCalculator(pm);
	}
	
	public abstract boolean doIParticipateInTournament();
	public abstract List<AdventureCard> playCardsForTournament();
	public abstract List<List<AdventureCard>> doISponsorAQuest(QuestCard card);
	public abstract boolean doIParticipateInQuest(QuestCard questCard);
	public abstract List<AdventureCard> discardAfterWinningTest();
	public abstract List<AdventureCard> playCardsForFoeQuest(QuestCard questCard);
	public abstract int nextBid(int prevBid);

	
	public List<AdventureCard> discardKingsCalltoArms(int n, TYPE type){
		logger.info("Discarding: " + n + " cards with type: " + type);
		List<AdventureCard> cards = new ArrayList<AdventureCard>();
		Iterator<AdventureCard> x = player.hand.getDeck().iterator();
		while(cards.size() != n && x.hasNext()) {
			AdventureCard y = x.next();
			if(y.getType() == type) {
				cards.add(y);
			}
		}
		logger.info("Discarded: " + Arrays.toString(cards.stream().map(i -> i.getName()).toArray(String[]::new)));
		return cards;
	}
	
	public List<AdventureCard> discardWhenHandFull(int n){
		logger.info("Discarding: " + n + " cards");
		logger.info("Hand: " + player.hand());
		List<AdventureCard> toReturn = new ArrayList<AdventureCard>(n);
		IntStream.range(0, n).forEach(i -> toReturn.add(player.hand.getDeck().get(12 + i)));
		logger.info("Discarded: " + toReturn.size() + " " + toReturn);
		return toReturn;
	}
	
	public boolean winPlayer(Player p) {
		logger.info("Player " + p.getID() + " rank: " + p.getRank() + " shields: " + p.getShields());
		if((p.getRank()  == Rank.RANKS.KNIGHTOFTHEROUNDTABLE) || 
				(p.getRank()  == Rank.RANKS.CHAMPION && p.getShields() >= (10 - pm.players.length)) ||
				(p.getRank()  == Rank.RANKS.KNIGHT && p.getShields() >= (7 - pm.players.length)) ||
				(p.getRank()  == Rank.RANKS.SQUIRE && p.getShields() >= (5 - pm.players.length))) {
			return true;
		}
		return false;
	}
	
	public boolean playerCanWinOrEvolve(PlayerManager pm) {
		Iterator<Player> round = pm.round();
		while(round.hasNext()) {
			if(winPlayer(round.next())) {
				logger.info("Someone can win/evolve the game if they join the tournament");
				return true;
			}
		}
		logger.info("No one can win/evolve the game if they join the tournament");
		return false;
	}
}
