package src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.client.GameBoardController;
import src.client.UIPlayerManager;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.QuestCard;
import src.game_logic.Rank;

public abstract class AbstractAI {
	final static Logger logger = LogManager.getLogger(AbstractAI.class);
	
	protected int rounds = 0;
	protected int stages = 0;
	public UIPlayer player;
	protected List<Player> listPlayer;
	protected UIPlayerManager pm;
	protected BattlePointCalculator bpc;
	public GameBoardController gbc;
	
	public AbstractAI(UIPlayer player, UIPlayerManager pm) {
		this.player = player;
		listPlayer = new ArrayList<Player>();
		listPlayer.add(player);
		this.pm = pm;
		this.bpc = new BattlePointCalculator(null);
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
		Iterator<AdventureCard> x = player.getPlayerHandAsList().iterator();
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
	
	public boolean winPlayer(int i) {
		if((pm.getPlayerRank(i) == Rank.RANKS.CHAMPION && pm.players[i].shields >= (10 - pm.getNumPlayers())) ||
				(pm.getPlayerRank(i) == Rank.RANKS.KNIGHT && pm.players[i].shields >= (7 - pm.getNumPlayers())) ||
				(pm.getPlayerRank(i) == Rank.RANKS.SQUIRE && pm.players[i].shields >= (5 - pm.getNumPlayers()))) {
			return true;
		}
		return false;
	}
	
	public boolean playerCanWinOrEvolve(UIPlayerManager pm) {
		int length = pm.getNumPlayers();
		for(int i = 0; i < length; i++) {
			if(winPlayer(i)) {
				logger.info("Someone can win/evolve the game if they join the tournament");
				return true;
			}
		}
		logger.info("No one can win/evolve the game if they join the tournament");
		return false;
	}
}
