package src.player;

import src.game_logic.Card;
import src.game_logic.QuestCard;

public class BidCalculator {

	public int maxBid(Player player, QuestCard quest) {
		int freeBids = 0;
		// this should probably go into the card?
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
				if(player.hasTristanIseultBoost()) {
					freeBids += 4;	
				} else {
					freeBids += 2;
				}
			}
		}
		return freeBids + player.hand.size();
	}

	// Feels bad when you have a sick lambda but no use for it :( 
	// I still love you lambda <3 
	//		return players
	//		        .stream()
	//		        .map(s -> new Pair(s, maxBid(s)))
	//		        .max((p1,p2) -> Integer.compare(p1.maxBid, p2.maxBid))
	//		        .get();

}

