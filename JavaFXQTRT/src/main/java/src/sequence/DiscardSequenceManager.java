package src.sequence;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.BoardModel;
import src.messages.Message.MESSAGETYPES;
import src.messages.QOTRTQueue;
import src.messages.hand.HandFullClient;
import src.player.Player;
import src.player.PlayerManager;

public class DiscardSequenceManager extends SequenceManager {
	
	final static Logger logger = LogManager.getLogger(DiscardSequenceManager.class);
	
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
		logger.info("Starting discard sequence manager");
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			Player player = players.next();
			if(player.hand.size() > 12) {
				logger.info("Player: " + player.getID() + " has too many cards: " + player.hand.size());
				pm.setPlayer(player);
				pm.setState(player, Player.STATE.DISCARDING);
				
				HandFullClient x = actions.take(HandFullClient.class, MESSAGETYPES.DISCARDHANDFULL);
				Player p = pm.players[x.player];
				for(String s: x.discard) {
					p.hand.getCardByName(s);
				}
				for(String s: x.toFaceUp) {
					if(s.equals("Sir Tristan")) {
						p.tristan = true;
					} else if(s.equals("Queen Iseult")) {
						p.iseult = true;
					}
					p.getFaceUp().addCard(p.hand.getCardByName(s), 1);
				}
			}
		}
		logger.info("Ending discard sequence manager");
	}

}
