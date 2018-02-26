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
		return freeBids(player, quest) + player.hand.size();
	}

	private int freeBids(Player player, QuestCard quest) {
		int freeBids = 0;
		for(Card c : player.getFaceUp().getDeck()) {
			if(c.getName().equals("King Pellinore") && 
					quest.getName().equals("Search for the Questing Beast")) {
				freeBids += 4;
			} else if(c.getName().equals("Amour")) {
				freeBids += 1;
			} else if(c.getName().equals("King Arthur")) {
				freeBids += 2;
			} else if(c.getName().equals("Queen Guinevere")) {
				freeBids += 3;
			} else if(c.getName().equals("Queen Iseult")) {
				boolean foundTristan = false;
				Iterator<Player> players = pm.round();
				while(players.hasNext()) {
					if(players.next().tristan == true) foundTristan = true;
				}
				if(foundTristan) {
					freeBids += 4;	
				} else {
					freeBids += 2;
				}
			}
		}
		
		logger.info("Free bids: " + freeBids);
		return freeBids;
	}

	public int cardsToBid(int toBid, Player player, QuestCard quest) {
		return toBid - freeBids(player, quest);
	}

	// Feels bad when you have a sick lambda but no use for it :( 
	// I still love you lambda <3 
	//		return players
	//		        .stream()
	//		        .map(s -> new Pair(s, maxBid(s)))
	//		        .max((p1,p2) -> Integer.compare(p1.maxBid, p2.maxBid))
	//		        .get();

}

