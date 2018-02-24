package src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.QuestCard;
import src.game_logic.Rank;

public abstract class AbstractAI {
	final static Logger logger = LogManager.getLogger(AbstractAI.class);
	
	protected int rounds = 0;
	public UIPlayer player;
	protected List<Player> listPlayer;
	protected UIPlayerManager pm;
	protected BattlePointCalculator bpc;
	
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
	public abstract List<AdventureCard> playCardsForFoeQuest(boolean lastStage, QuestCard questCard);
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
		return player.hand.drawTopCards(n);
	}
	
	public boolean playerCanWin(UIPlayerManager pm) {
		int length = pm.getNumPlayers();
		for(int i = 0; i < length; i++) {
			logger.info("Someone can win the game if they join the tournament");
			if(pm.getPlayerRank(i) == Rank.RANKS.CHAMPION && pm.players[i].shields >= (10 - pm.getNumPlayers())) {
				return true;
			}
		}
		return false;
	}
}
