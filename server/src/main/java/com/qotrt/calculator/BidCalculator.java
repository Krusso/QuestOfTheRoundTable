package com.qotrt.calculator;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.AllyCard;
import com.qotrt.cards.AmourCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.gameplayer.Player;
import com.qotrt.gameplayer.PlayerManager;

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
		for(AdventureCard c : player.getFaceUp().getDeck()) {
			logger.info("Card name: " + c.getName() + " free bids so far: " + freeBids);
			if(c.getName().equals("King Pellinore") && 
					quest.getName().equals("Search for the Questing Beast")) {
				logger.info("King Pellinore and search for the questing beast");
				freeBids += 4;
			} else if(c.getType().equals(TYPE.AMOUR)) {
				logger.info("Amour");
				freeBids += ((AmourCard) c).getBids();
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
			} else if(c.getType().equals(TYPE.ALLIES)) {
				logger.info("Ally: " + c.getName());
				freeBids += ((AllyCard) c).getBids();
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

