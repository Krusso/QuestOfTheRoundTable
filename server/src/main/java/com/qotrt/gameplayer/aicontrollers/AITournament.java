package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.qotrt.cards.AdventureCard;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.model.GenericPairTyped;

public class AITournament extends AIController {

	public AITournament(Game game, Player player, AbstractAI ai) {
		super(game, player, ai);
		
		logger.info("addeding handlers");
		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("questiontournament") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func1 = x ->  wrapEvent(x, y -> questiontournament()).accept(x);
		
		Function<PropertyChangeEvent, Boolean> func2 = x -> x.getPropertyName().equals("questioncardtournament") && contains((int[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func3 = x -> wrapEvent(x, y -> questioncardtournament()).accept(x);
		
		events.add(new GenericPairTyped<>(func, func1));
		events.add(new GenericPairTyped<>(func2, func3));
	}

	private void questioncardtournament() {
		List<AdventureCard> cards = ai.playCardsForTournament();
		
		cards.forEach(i -> pc.playCard(game, player, game.bmm.getTournamentModel(),
				new PlayCardClient(player.getID(), i.id, ZONE.HAND, ZONE.FACEDOWN)));
		game.bmm.getTournamentModel().finishSelectingCards(player);
	}

	private void questiontournament() {
		if(ai.doIParticipateInTournament()) {
			game.bmm.getTournamentModel().acceptTournament(player);
		} else {
			game.bmm.getTournamentModel().declineTournament(player);
		}
	}

	@Override
	public void handleEvent(PropertyChangeEvent evt) {
		handlePropertyChangeEvent(evt);
	}

}
