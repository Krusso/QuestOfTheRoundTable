package src.player;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import src.game_logic.Card;
import src.game_logic.QuestCard;

public class BidCalculator {

	private PlayerManager pm;
	
	final static Logger logger = LogManager.getLogger(BidCalculator.class);
	
	public BidCalculator(PlayerManager pm) {
		this.pm = pm;
	}
	
	public int maxBid(Player player, QuestCard quest) {
		if(quest != null) {
			logger.info("Free bids for: " + quest.getName());
		}
		logger.info("Hand size: " + player.hand.size());
		return freeBids(player, quest) + player.hand.size();
	}

	private int freeBids(Player player, QuestCard quest) {
		int freeBids = 0;
		for(Card c : player.getFaceUp().getDeck()) {
			logger.info("Card name: " + c.getName() + " free bids so far: " + freeBids);
			if(c.getName().equals("King Pellinore") && 
					quest.getName().equals("Search for the Questing Beast")) {
				logger.info("King Pellinore and search for the questing beast");
				freeBids += 4;
			} else if(c.getName().equals("Amour")) {
				logger.info("Amour");
				freeBids += 1;
			} else if(c.getName().equals("King Arthur")) {
				logger.info("King Arthur");
				freeBids += 2;
			} else if(c.getName().equals("Queen Guinevere")) {
				logger.info("Queen Guinevere");
				freeBids += 3;
			} else if(c.getName().equals("Queen Iseult")) {
				boolean foundTristan = false;
				Iterator<Player> players = pm.round();
				while(players.hasNext()) {
					if(players.next().tristan == true) foundTristan = true;
				}
				if(foundTristan) {
					logger.info("Tristan and Queen Iseult");
					freeBids += 4;	
				} else {
					logger.info("Queen Iseult only");
					freeBids += 2;
				}
			}
		}
		
		logger.info("Free bids: " + freeBids);
		return freeBids;
	}

	public int cardsToBid(int toBid, Player player, QuestCard quest) {
		if(quest != null) {
			logger.info("Getting cards to bid for quest: " + quest.getName());
		}
		logger.info("Need to bid: " + toBid);
		return toBid - freeBids(player, quest);
	}
}

