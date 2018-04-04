package com.qotrt.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.qotrt.calculator.BattlePointCalculator;
import com.qotrt.calculator.BidCalculator;
import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.cards.QuestCard;
import com.qotrt.confirmation.Confirmation;
import com.qotrt.confirmation.MultiShotConfirmation;
import com.qotrt.confirmation.NeverEndingConfirmation;
import com.qotrt.confirmation.SingleShotConfirmation;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.quest.QuestWinServer.WINTYPES;
import com.qotrt.sequence.Quest;
import com.qotrt.sequence.Stage;
import com.qotrt.util.PlayerUtil;

public class QuestModel extends Observable implements PropertyChangeListener , CanPick{

	private int toDiscard;
	private int merlinUses = 0;
	
	private Confirmation sponsor;
	
	public synchronized CountDownLatch sponsorLatch() { return sponsor.getCountDownLatch();}
	
	
	private Confirmation stageSetup;
	
	public synchronized CountDownLatch stageSetupLatch() { return stageSetup.getCountDownLatch(); }
	
	private Confirmation participate;
	
	public synchronized CountDownLatch participateLatch() { return participate.getCountDownLatch();}

	private Confirmation cards;
	
	public synchronized CountDownLatch cardsLatch() { return cards.getCountDownLatch();}
	
	private Confirmation discard;

	public synchronized CountDownLatch discardLatch() { return discard.getCountDownLatch();}
	
	private int maxBid = -1;
	private BidCalculator bc;
	private QuestCard card;
	private Confirmation bid;
	
	public synchronized CountDownLatch bidLatch() { return bid.getCountDownLatch();}
	
	private List<Player> participatents = new ArrayList<Player>();
	private Quest quest;
	private List<AdventureCard> discardCards;
	
	public QuestModel(boolean racing) {
		sponsor = new SingleShotConfirmation("questionSponsor", 
				"acceptSponsorQuest", 
				"declineSponsorQuest", racing);
		
		stageSetup = new SingleShotConfirmation("questStage",
				null,
				null, racing);
		
		participate = new MultiShotConfirmation("questionQuest", 
				"joinQuest", 
				"declineQuest", racing);
		
		cards = new MultiShotConfirmation("questionCardQuest", 
				"cardQuest", 
				null, true);
		
		discard = new SingleShotConfirmation("discardQuest",
				"discardQuestFinish",
				null, racing);
		
		bid = new NeverEndingConfirmation(null, 
				null, 
				null, racing);
		sponsor.subscribe(this);
		participate.subscribe(this);
		cards.subscribe(this);
		bid.subscribe(this);
		stageSetup.subscribe(this);
		discard.subscribe(this);
	}
	
	public synchronized void setQuest(Quest quest, List<Player> sponsors) {
		logger.info("starting quest setup sponsor: " + sponsors + " quest: " + quest);
		logger.info("quest stages: " + quest.getNumStages());
		logger.info("setting quest: " + quest);
		this.quest = quest;
		logger.info("quests: " + this.quest);
		stageSetup.start(sponsors, quest.getNumStages());
	}
	
	public synchronized boolean canPickCardsForStage() {
		return stageSetup.can();
	}
	
	public synchronized String finishSelectingStages(Player player) {
		int min = Integer.MIN_VALUE;
		BattlePointCalculator bpc = new BattlePointCalculator(null);
		for(int i = 0; i < quest.getNumStages(); i++) {
			if(!(quest.getStage(i).isFoeStage() || quest.getStage(i).isTestStage())) {
				logger.info("is foe: " + quest.getStage(i).isFoeStage());
				logger.info("is test: " + quest.getStage(i).isTestStage());
				return "each stages needs 1 test or 1 foe card";
			}
			if(quest.getStage(i).isFoeStage()) {
				if(bpc.calculateStage(quest.getStage(i).getStageCards(), 
						quest.getQuestCard()) <= min){
					return "each stage needs an increasing amount of bp";
				}

				min = bpc.calculateStage(quest.getStage(i).getStageCards(), 
						quest.getQuestCard());
			}
		}
		
		merlinUses = 1;
		stageSetup.accept(player, "player: " + player + " attempted to finish selecting cards", 
				"player: " + player + " finished selecting cards", 
				"player: " + player + " finish selecting cards too late");
		return "";
	}
	
	@Override
	//propagating events up
	public void propertyChange(PropertyChangeEvent arg0) {
		fireEvent(arg0.getPropertyName(), arg0.getOldValue(), arg0.getNewValue());
	}
			
	private List<Player> winners = new ArrayList<Player>();
	private WINTYPES types;
	
	public synchronized void questionSponsorPlayers(List<Player> potentialSponsors) {
		sponsor.start(potentialSponsors);
	}
	
	public synchronized void acceptSponsor(Player player) {
		sponsor.accept(player, "player: " + player + " attempting to sponsor",
				"player: " + player.getID() + " sponsored quest", 
				"player: " + player + " attempted to sponsor too late");
	}
	
	public synchronized void declineSponsor(Player player) {
		sponsor.decline(player, "player: " + player + " attempting to decline sponsorship",
				"player: " + player + " declined sponsorship", 
				"player: " + player + " decline sponsorship too late");
	}
	
	public synchronized List<Player> getPlayerWhoSponsor() {
		return sponsor.get();
	}
	
	public synchronized void setMessage(WINTYPES types) {
		this.types = types;
	}
	
	public synchronized void setWinners(List<Player> winners) {
		this.winners = winners;
		fireEvent("questWinners", null, 
				new GenericPair(
						this.winners.stream().mapToInt(i -> i.getID()).toArray(),
						this.types));
	}
	
	public synchronized void questionJoinQuest(List<Player> potentialQuestPlayers) {
		participate.start(potentialQuestPlayers);
	}

	public synchronized void acceptQuest(Player player) {
		participate.accept(player, "player: " + player + " attempting to accept quest",
				"player: " + player.getID() + " accept quest",
				"player: " + player + " attempted to accept quest too late");
	}
	
	public synchronized void declineQuest(Player player) {
		participate.decline(player, "player: " + player + " attempting to decline quest",
				"player: " + player + " decline quest", 
				"player: " + player + " decline quest too late");
	}

	public synchronized List<Player> playerWhoJoined() {
		logger.info("Getting players who joined the quest");
		List<Player> x = participate.get();
		logger.info("Players: " + Arrays.toString(PlayerUtil.playersToIDs(x)));
		participatents = x;
		return x;
	}

	public synchronized void passStage(List<Player> passedStage) {
		participatents = passedStage;
		fireEvent("passStage", null, PlayerUtil.playersToIDs(passedStage));
	}
	
	public synchronized void questionCardsStage() {
		cards.start(participatents);
	}

	public synchronized boolean canPick() {
		return cards.can();
	}
	
	public synchronized void finishSelectingCards(Player player) {
		cards.accept(player, "player: " + player + " attempted to finish selecting cards", 
				"player: " + player + " finished selecting cards", 
				"player: " + player + " finish selecting cards too late");
	}
	
	public synchronized void finishPicking() {
		cards.get();
	}

	public synchronized void flipStage() {
		fireEvent("flipStage", null, new GenericPair(
				quest.getCurrentStageCards().stream().map(i -> i.getName()).toArray(String[]::new), 
				quest.getCurrentStage()));
	}

	public synchronized void questionBid(List<Player> participants, BidCalculator bc, QuestCard card, int minBid) {
		bid.start(participants);
		maxBid = minBid - 1;
		this.bc = bc;
		this.card = card;
		for(Player player: participants) {
			fireEvent("bid", null, new int[] {player.getID(), bc.maxBid(player, card), minBid, -1});
		}
	}

	public synchronized void bid(Player player, int bidAmount) {
		if(maxBid < bidAmount && bc.maxBid(player, card) >= bidAmount) {
			if(bid.accept(player, "player: " + player + " attempted to bid higher", 
				"player: " + player + " finished bidding higher", 
				"player: " + player + " finished bidding higher too late")) {
				maxBid = bidAmount;
				bid.forAllPlayers(i -> {
					fireEvent("bid", null, new int[] {i.getID(), bc.maxBid(i, card), maxBid + 1, player.getID()});
				});
			}
		}
	}
	
	public synchronized void declineBid(Player player) {
		bid.decline(player, "player: " + player + " attempting to decline bid",
				"player: " + player + " decline bid", 
				"player: " + player + " decline bid too late");
	}
	
	public synchronized List<Player> getBidWinner() {
		return bid.get();
	}
	
	public synchronized int getMaxBid() {
		return maxBid;
	}

	public synchronized void discardCards(List<Player> bidWinner, int toDiscard) {
		discard.start(bidWinner, toDiscard);
		this.toDiscard = toDiscard;
		discardCards = new ArrayList<AdventureCard>();
	}
	
	public synchronized String finishDiscard(Player player) {
		if(toDiscard != discardCards.size()) {
			return "Need to discard: " + (toDiscard - discardCards.size()) + " more cards";
		}
		discard.accept(player, "player: " + player + " attempted to finish discard", 
				"player: " + player + " finished discard", 
				"player: " + player + " finish discard too late");
		return "";
	}
	
	public synchronized void addDiscard(AdventureCard c) {
		discardCards.add(c);
	}
	
	public synchronized AdventureCard getDiscardCard(int id) {
		for(AdventureCard c: discardCards) {
			if(c.id == id) {
				discardCards.remove(c);
				return c;
			}
		}
		return null;
	}
	
	public synchronized boolean canDiscard() {
		return discard.can();
	}


	public synchronized String attemptMove(Integer zoneFrom, Integer zoneTo, int card) {
		Stage from = quest.getStage(zoneFrom);
		Stage to = quest.getStage(zoneTo);
		if(from == null || to == null) {
			return "not a playable zone currently";
		}
		String response = to.validToAdd(from.findCardByID(card));
		if(response.equals("")) {
			to.addCard(from.getCardByID(card));
		}
		
		return response;
	}

	public synchronized String attemptMove(Integer zoneTo, AdventureCard card) {
		logger.info("Quest: " + quest);
		Stage to = quest.getStage(zoneTo);
		if(to == null) {
			return "not a playable zone currently";
		}
		String response = to.validToAdd(card);
		logger.info("attempting to move card: " + card.getName());
		
		if(card.getType() == TYPE.TESTS && questContainsTest()) {
			response = "Cant play more than one test per quest";
		}
		
		if(response.equals("")) {
			to.addCard(card);
		}
		
		return response;
	}
	
	private synchronized boolean questContainsTest() {
		for(int i = 0; i < quest.getNumStages(); i++) {
			if(quest.getStage(i).isTestStage()) {
				return true;
			}
		}
		return false;
	}

	public synchronized AdventureCard getCard(int card, Integer integer) {
		return quest.getStage(integer).getCardByID(card);
	}

	public synchronized Quest getQuest() {
		return quest;
	}

	public synchronized GenericPair[] merlinCan(int stage) {
		if((cards.can() || bid.can() || discard.can()) && merlinUses > 0) {
			merlinUses -= 1;
			return quest.getStage(stage).getStageCards().
					stream().map(i -> new GenericPair(i.getName(), i.id)).toArray(GenericPair[]::new);
		}
		
		return null;
	}
}