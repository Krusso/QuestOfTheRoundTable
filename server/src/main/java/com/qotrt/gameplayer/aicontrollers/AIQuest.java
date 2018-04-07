package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;

public class AIQuest extends AIController {

	public AIQuest(Game game, Player player, AbstractAI ai) {
		super(game, player, ai);

		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("questionSponsor");
		Consumer<PropertyChangeEvent> func1 = x -> questionSponsor();

		Function<PropertyChangeEvent, Boolean> func2 = x -> x.getPropertyName().equals("questStage");
		Consumer<PropertyChangeEvent> func3 = x -> questStage();

		Function<PropertyChangeEvent, Boolean> func4 = x -> x.getPropertyName().equals("questQuestion") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func5 = x -> questQuestion();

		Function<PropertyChangeEvent, Boolean> func6 = x -> x.getPropertyName().equals("questionCardQuest") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func7 = x -> questionCardQuest();

		Function<PropertyChangeEvent, Boolean> func8 = x -> x.getPropertyName().equals("bid");
		Consumer<PropertyChangeEvent> func9 = x -> bid(x);

		Function<PropertyChangeEvent, Boolean> func10 = x -> x.getPropertyName().equals("discardQuest") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func11 = x -> discardQuest(x);

		events.add(new GenericPairTyped<>(func, func1));
		events.add(new GenericPairTyped<>(func2, func3));
		events.add(new GenericPairTyped<>(func4, func5));
		events.add(new GenericPairTyped<>(func6, func7));
		events.add(new GenericPairTyped<>(func8, func9));
		events.add(new GenericPairTyped<>(func10, func11));
	}

	private void questionSponsor() {
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

	private void questStage() {
		List<List<AdventureCard>> cards = ai.doISponsorAQuest((QuestCard) game.bmm.getBoardModel().getCard());
		for(int i = 0; i < cards.size(); i++) {
			for(AdventureCard c: cards.get(i)) {
				game.bmm.getQuestModel().attemptMove(i, player.findCardByID(c.id));
				player.getCardByID(c.id);
			}
		}
		game.bmm.getQuestModel().finishSelectingStages(player);
	}

	private void questQuestion() {
		if(ai.doIParticipateInQuest((QuestCard) game.bmm.getBoardModel().getCard())) {
			game.bmm.getQuestModel().acceptQuest(player);
		} else {
			game.bmm.getQuestModel().declineQuest(player);
		}
	}

	private void questionCardQuest() {
		List<AdventureCard> cards = ai.playCardsForFoeQuest((QuestCard) game.bmm.getBoardModel().getCard());
		cards.forEach(i -> player.setFaceDown(player.getCardByID(i.id)));
		game.bmm.getQuestModel().finishSelectingCards(player);
	}

	private void bid(PropertyChangeEvent evt) {
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

	private void discardQuest(PropertyChangeEvent evt) {
		List<AdventureCard> cards = ai.discardAfterWinningTest();
		for(int i = 0; i < (int)((GenericPair) evt.getNewValue()).value; i++) {
			game.bmm.getQuestModel().addDiscard(player.getCardByID(cards.get(i).id));
		}
		game.bmm.getQuestModel().finishDiscard(player);
	}

	@Override
	public void handleEvent(PropertyChangeEvent evt) {
		handlePropertyChangeEvent(evt);
	}

}
