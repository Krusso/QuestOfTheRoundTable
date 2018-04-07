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

public class AIDiscard extends AIController {

	public AIDiscard(Game game, Player player, AbstractAI ai) {
		super(game, player, ai);
		
		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("discard") && contains((Player[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func1 = x -> wrapEvent(x, y -> discardHand()).accept(x);
		
		events.add(new GenericPairTyped<>(func, func1));
	}

	private void discardHand() {
		List<AdventureCard> cards = ai.discardWhenHandFull(player.hand.size() - 12);
		cards.forEach(i -> {
			pc.discardCard(game, player, game.bmm.getDiscardModel(), new PlayCardClient(player.getID(),
					i.id, ZONE.HAND, ZONE.DISCARD));
			pc.checkValidityAndSend(game, player, new PlayCardClient(player.getID(),
					i.id, ZONE.HAND, ZONE.DISCARD), "");
		});
		
		game.bmm.getDiscardModel().finishDiscarding(player);
	}

	@Override
	public void handleEvent(PropertyChangeEvent evt) {
		handlePropertyChangeEvent(evt);
	}

}
