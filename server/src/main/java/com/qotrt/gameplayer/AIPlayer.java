package com.qotrt.gameplayer;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.concurrent.Executors;

import javax.swing.plaf.synth.SynthSplitPaneUI;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.game.Game;
import com.qotrt.model.GenericPair;
import com.qotrt.views.Observer;

public class AIPlayer extends Observer {

	private int strat;
	private AbstractAI ai;
	private Game game;
	private Player player;

	public AIPlayer(int strat, Game game) {
		this.strat = strat;
		this.game = game;
	}

	public void startAIPlayer(Player player, PlayerManager pm) {
		System.out.println("Player: " + player.getID() + " strat: " + strat);
		this.player = player;
		if(strat == 1) {
			ai = new A1(player, pm);
		} else if(strat == 2) {
			ai = new A2(player, pm);
		} else {
			// TODO
			//ai = new A3(player, pm); 
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Player: " + player.getID() + " strat: " + strat + " got event: " + evt.getPropertyName());
		
		if(evt.getPropertyName().equals("questiontournament")) {
			if(ai.doIParticipateInTournament()) {
				game.bmm.getTournamentModel().acceptTournament(player);
			} else {
				game.bmm.getTournamentModel().declineTournament(player);
			}
		}

		if(evt.getPropertyName().equals("questioncardtournament") && contains((int[]) evt.getNewValue())) {
			List<AdventureCard> cards = ai.playCardsForTournament();
			cards.forEach(i -> player.setFaceDown(player.getCardByID(i.id)));
			game.bmm.getTournamentModel().finishSelectingCards(player);
		}

		if(evt.getPropertyName().equals("questionSponsor")) {
			Executors.newScheduledThreadPool(1).execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(10000);
						List<List<AdventureCard>> accept = ai.doISponsorAQuest((QuestCard)game.bmm.getBoardModel().getCard());
						if(accept != null) {
							game.bmm.getQuestModel().acceptSponsor(player);
						} else {
							game.bmm.getQuestModel().declineSponsor(player);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		if(evt.getPropertyName().equals("questStage")) {
			List<List<AdventureCard>> cards = ai.doISponsorAQuest((QuestCard) game.bmm.getBoardModel().getCard());
			for(int i = 0; i < cards.size(); i++) {
				for(AdventureCard c: cards.get(i)) {
					game.bmm.getQuestModel().attemptMove(i, player.findCardByID(c.id));
					player.getCardByID(c.id);
				}
			}
			game.bmm.getQuestModel().finishSelectingStages(player);
		}
		
		if(evt.getPropertyName().equals("questionQuest") && contains((int[]) evt.getNewValue())) {
			if(ai.doIParticipateInQuest((QuestCard) game.bmm.getBoardModel().getCard())) {
				game.bmm.getQuestModel().acceptQuest(player);
			} else {
				game.bmm.getQuestModel().declineQuest(player);
			}
		}
		
		if(evt.getPropertyName().equals("questionCardQuest") && contains((int[]) evt.getNewValue())) {
			List<AdventureCard> cards = ai.playCardsForFoeQuest((QuestCard) game.bmm.getBoardModel().getCard());
			cards.forEach(i -> player.setFaceDown(player.getCardByID(i.id)));
			game.bmm.getQuestModel().finishSelectingCards(player);
		}
		
		if(evt.getPropertyName().equals("bid")) {
			Executors.newScheduledThreadPool(1).execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(10000);
						int bid = ai.nextBid(((int[]) evt.getNewValue())[2]);
						if(bid != -1) {
							game.bmm.getQuestModel().bid(player, bid);
						} else {
							game.bmm.getQuestModel().declineBid(player);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		if(evt.getPropertyName().equals("discardQuest") && contains((int[]) evt.getNewValue())) {
			List<AdventureCard> cards = ai.discardAfterWinningTest();
			for(int i = 0; i < (int)((GenericPair) evt.getNewValue()).value; i++) {
				game.bmm.getQuestModel().addDiscard(player.getCardByID(cards.get(i).id));
			}
			game.bmm.getQuestModel().finishDiscard(player);
		}
	}

	private boolean contains(int[] newValue) {
		for(int i: newValue) {
			if(i == player.getID()) {
				return true;
			}
		}

		return false;
	}

}
