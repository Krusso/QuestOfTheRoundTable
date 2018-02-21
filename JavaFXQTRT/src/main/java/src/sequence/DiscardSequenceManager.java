package src.sequence;

import java.util.Iterator;

import src.game_logic.BoardModel;
import src.messages.QOTRTQueue;
import src.messages.hand.HandFullClient;
import src.player.Player;
import src.player.PlayerManager;

public class DiscardSequenceManager extends SequenceManager {
	
	private QOTRTQueue actions;
	private PlayerManager pm;
	private BoardModel bm;
	
	public DiscardSequenceManager(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
		this.actions = actions;
		this.pm = pm;
		this.bm = bm;
	};
	
	@Override
	public void start(QOTRTQueue actions1, PlayerManager pm1, BoardModel bm1) {
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			Player player = players.next();
			if(player.hand.size() > 12) {
				pm.setPlayer(player);
				pm.setState(player, Player.STATE.DISCARDING);
				
				HandFullClient x = actions.take(HandFullClient.class);
				Player p = pm.players[x.player];
				for(String s: x.discard) {
					p.hand.getCardByName(s);
				}
				for(String s: x.toFaceUp) {
					p.getFaceDownDeck().addCard(p.hand.getCardByName(s), 1);
				}
			}
		}
	}

}
