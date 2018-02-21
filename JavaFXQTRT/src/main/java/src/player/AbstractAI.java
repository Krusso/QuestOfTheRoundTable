package src.player;

import java.util.ArrayList;
import java.util.List;

import src.client.UIPlayerManager;
import src.game_logic.AdventureCard;
import src.game_logic.Card;
import src.game_logic.QuestCard;

public abstract class AbstractAI {
	protected int rounds = 0;
	public UIPlayer player;
	protected List<Player> listPlayer;
	protected UIPlayerManager pm;
	protected BattlePointCalculator bpc;
	
	protected AbstractAI() {}
	
	public AbstractAI(UIPlayer player, UIPlayerManager pm) {
		this.player = player;
		listPlayer = new ArrayList<Player>();
		listPlayer.add(player);
		this.pm = pm;
		this.bpc = new BattlePointCalculator(null);
	}
	
	public abstract boolean doIParticipateInTournament();
	public abstract List<AdventureCard> playCardsForTournament();
	protected abstract List<List<AdventureCard>> implDoISponserAQuest(QuestCard card);
	public abstract boolean doIParticipateInQuest(QuestCard questCard);
	protected abstract int implNextBid(int prevBid);
	public abstract List<AdventureCard> discardAfterWinningTest();
	public abstract List<AdventureCard> playCardsForFoeQuest(boolean lastStage, QuestCard questCard);
	
	public int nextBid(int prevBid) {
		rounds++;
		return implNextBid(prevBid);
	}
	
	public List<List<AdventureCard>> doISponserAQuest(QuestCard card){
		rounds = 0;
		return this.implDoISponserAQuest(card);
	}
	
	public List<AdventureCard> discardWhenHandFull(int n){
		return player.hand.drawTopCards(n);
	}
}
