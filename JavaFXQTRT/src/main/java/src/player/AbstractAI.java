package src.player;

import java.util.ArrayList;
import java.util.List;

import src.client.UIPlayerManager;
import src.game_logic.Card;
import src.game_logic.QuestCard;

public abstract class AbstractAI {
	
	protected UIPlayer player;
	protected List<Player> listPlayer;
	protected UIPlayerManager pm;
	
	protected AbstractAI() {}
	
	public AbstractAI(UIPlayer player, UIPlayerManager pm) {
		this.player = player;
		listPlayer = new ArrayList<Player>();
		listPlayer.add(player);
		this.pm = pm;
	}
	
	public abstract boolean doIParticipateInTournament();
	public abstract List<Card> playCardsForTournament();
	public abstract List<List<Card>> doISponserAQuest(List<Player> participants, QuestCard card);
	public abstract boolean doIParticipateInQuest(QuestCard questCard);
	public abstract List<Card> playCardsForFoeQuest(boolean lastStage);
	public abstract int nextBid(int round, int prevBid);
	public abstract List<Card> discardAfterWinningTest(int round);
	
}
