package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;

public class AIQuest extends AIController {

	public AIQuest(Game game, Player player, AbstractAI ai) {
		super(game, player, ai);

		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("questionSponsor") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func1 = x ->wrapEvent(x, y -> questionSponsor()).accept(x);

		Function<PropertyChangeEvent, Boolean> func2 = x -> x.getPropertyName().equals("questStage");
		Consumer<PropertyChangeEvent> func3 = x -> wrapEvent(x, y -> questStage(y)).accept(x);

		Function<PropertyChangeEvent, Boolean> func4 = x -> x.getPropertyName().equals("questionQuest") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func5 = x -> wrapEvent(x, y -> questQuestion()).accept(x);

		Function<PropertyChangeEvent, Boolean> func6 = x -> x.getPropertyName().equals("questionCardQuest") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func7 = x ->wrapEvent(x, y -> questionCardQuest()).accept(x);

		Function<PropertyChangeEvent, Boolean> func8 = x -> x.getPropertyName().equals("bid") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func9 = x -> wrapEvent(x, y -> bid(y)).accept(x);

		Function<PropertyChangeEvent, Boolean> func10 = x -> x.getPropertyName().equals("discardQuest");
		Consumer<PropertyChangeEvent> func11 = x -> wrapEvent(x, y -> discardQuest(y)).accept(x);

		events.add(new GenericPairTyped<>(func, func1));
		events.add(new GenericPairTyped<>(func2, func3));
		events.add(new GenericPairTyped<>(func4, func5));
		events.add(new GenericPairTyped<>(func6, func7));
		events.add(new GenericPairTyped<>(func8, func9));
		events.add(new GenericPairTyped<>(func10, func11));
	}

	private void questionSponsor() {
		List<List<AdventureCard>> accept = ai.doISponsorAQuest((QuestCard)game.bmm.getBoardModel().getCard());
		if(accept != null) {
			game.bmm.getQuestModel().acceptSponsor(player);
		} else {
			game.bmm.getQuestModel().declineSponsor(player);
		}
	}

	private void questStage(PropertyChangeEvent a) {
		GenericPair e = (GenericPair) a.getNewValue();
		if(((int[])e.key)[0] != player.getID()) {
			return;
		}
		if(game.bmm.getQuestModel().getPlayerWhoSponsor().size() != 0 &&
				game.bmm.getQuestModel().getPlayerWhoSponsor().get(0).getID() != player.getID()) {
			return;
		}
		HashMap<Integer, ZONE> map = new HashMap<Integer, ZONE>();
		map.put(0, ZONE.STAGE1);
		map.put(1, ZONE.STAGE2);
		map.put(2, ZONE.STAGE3);
		map.put(3, ZONE.STAGE4);
		map.put(4, ZONE.STAGE5);

		List<List<AdventureCard>> cards = ai.doISponsorAQuest((QuestCard) game.bmm.getBoardModel().getCard());
		for(int i = 0; i < cards.size(); i++) {
			for(AdventureCard c: cards.get(i)) {
				game.bmm.getQuestModel().attemptMove(i, player.getCardByID(c.id));

				pc.checkValidityAndSend(game, player, new PlayCardClient(player.getID(),
						c.id, ZONE.HAND, map.get(i)), "");
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
		cards.forEach(i -> {
			pc.playCard(game, player, game.bmm.getQuestModel(), new PlayCardClient(player.getID(),
					i.id, ZONE.HAND, ZONE.FACEDOWN));
			
			
		});
		game.bmm.getQuestModel().finishSelectingCards(player);
	}

	private void bid(PropertyChangeEvent evt) {
		int bid = ai.nextBid(((int[]) evt.getNewValue())[2]);
		if(bid != -1) {
			game.bmm.getQuestModel().bid(player, bid);
		} else {
			game.bmm.getQuestModel().declineBid(player);
		}
	}

	private void discardQuest(PropertyChangeEvent evt) {
		GenericPair e = (GenericPair) evt.getNewValue();
		if(((int[])e.key)[0] != player.getID()) {
			return;
		}
		List<AdventureCard> cards = ai.discardAfterWinningTest();
		for(int i = 0; i < (int)((GenericPair) evt.getNewValue()).value; i++) {
			game.bmm.getQuestModel().addDiscard(player.getCardByID(cards.get(i).id));
			
			pc.checkValidityAndSend(game, player, new PlayCardClient(player.getID(),
					cards.get(i).id, ZONE.HAND, ZONE.DISCARD), "");
		}
		game.bmm.getQuestModel().finishDiscard(player);
	}

	@Override
	public void handleEvent(PropertyChangeEvent evt) {
		handlePropertyChangeEvent(evt);
	}
}
