package com.qotrt.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

	
	private Confirmation sponsor = new SingleShotConfirmation("questionSponsor", 
			"acceptSponsorQuest", 
			"declineSponsorQuest");
	
	public CountDownLatch sponsorLatch() { return sponsor.getCountDownLatch();}
	
	
	private Confirmation stageSetup = new SingleShotConfirmation("questStage",
			null,
			null);
	
	public CountDownLatch stageSetupLatch() { return stageSetup.getCountDownLatch(); }
	
	private Confirmation participate = new MultiShotConfirmation("questionQuest", 
			"joinQuest", 
			"declineQuest");
	
	public CountDownLatch participateLatch() { return participate.getCountDownLatch();}

	private Confirmation cards = new MultiShotConfirmation("questionCardQuest", 
			"cardQuest", 
			null);
	
	public CountDownLatch cardsLatch() { return cards.getCountDownLatch();}
	
	private Confirmation discard = new SingleShotConfirmation("discardQuest",
			null,
			null);
	
	public CountDownLatch discardLatch() { return discard.getCountDownLatch();}
	
	private int maxBid = -1;
	private Confirmation bid = new NeverEndingConfirmation(null, 
			null, 
			null);
	
	public CountDownLatch bidLatch() { return bid.getCountDownLatch();}
	
	private List<Player> participatents = new ArrayList<Player>();
	private Quest quest;
	
	public QuestModel() {
		sponsor.subscribe(this);
		participate.subscribe(this);
		cards.subscribe(this);
		bid.subscribe(this);
		stageSetup.subscribe(this);
	}
	
	public void setQuest(Quest quest, List<Player> sponsors) {
		System.out.println("starting quest setup sponsor: " + sponsors);
		stageSetup.start(sponsors, quest.getNumStages());
		this.quest = quest;
	}
	
	public boolean canPickCardsForStage() {
		return stageSetup.can();
	}
	
	public synchronized void finishSelectingStages(Player player) {
		stageSetup.accept(player, "player: " + player + " attempted to finish selecting cards", 
				"player: " + player + " finished selecting cards", 
				"player: " + player + " finish selecting cards too late");
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
		System.out.println("Getting players who joined the quest");
		List<Player> x = participate.get();
		System.out.println("Players: " + Arrays.toString(PlayerUtil.playersToIDs(x)));
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
		for(Player player: participants) {
			fireEvent("bid", null, new int[] {player.getID(), bc.maxBid(player, card), minBid, -1});
		}
	}

	public synchronized void bid(Player player, int bidAmount) {
		if(maxBid < bidAmount) {
			bid.accept(player, "player: " + player + " attempted to bid higher", 
				"player: " + player + " finished bidding higher", 
				"player: " + player + " finished bidding higher too late");
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

	public synchronized void discardCards(List<Player> bidWinner) {
		discard.start(bidWinner);
	}

	public synchronized String[] getDiscardCards() {
		// TODO Auto-generated method stub
		return null;
	}

	public String attemptMove(Integer zoneFrom, Integer zoneTo, int card) {
		Stage from = quest.getStage(zoneFrom);
		Stage to = quest.getStage(zoneTo);
		String response = to.validToAdd(from.findCardByID(card));
		if(response.equals("")) {
			to.addCard(from.getCardByID(card));
		}
		
		return response;
	}

	public String attemptMove(Integer zoneTo, AdventureCard card) {
		Stage to = quest.getStage(zoneTo);
		String response = to.validToAdd(card);
		System.out.println("attempting to move card: " + card.getName());
		
		if(card.getType() == TYPE.TESTS && questContainsTest()) {
			response = "Cant play more than one test per quest";
		}
		
		if(response.equals("")) {
			to.addCard(card);
		}
		
		return response;
	}
	
	private boolean questContainsTest() {
		for(int i = 0; i < quest.getNumStages(); i++) {
			if(quest.getStage(i).isTestStage()) {
				return true;
			}
		}
		return false;
	}

	public AdventureCard getCard(int card, Integer integer) {
		return quest.getStage(integer).getCardByID(card);
	}
}