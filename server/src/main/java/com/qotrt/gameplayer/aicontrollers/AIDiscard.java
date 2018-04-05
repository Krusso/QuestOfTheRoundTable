package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.qotrt.cards.AdventureCard;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.model.GenericPairTyped;

public class AIDiscard extends AIController {

	public AIDiscard(Game game, Player player, AbstractAI ai) {
		super(game, player, ai);
		
		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("discard") && contains((Player[]) x.getNewValue());
		Consumer<PropertyChangeEvent> func1 = x -> discard();
		
		events.add(new GenericPairTyped<>(func, func1));
	}

	private void discard() {
		List<AdventureCard> cards = ai.discardWhenHandFull(player.hand.size() - 12);
		cards.forEach(i -> game.bmm.getDiscardModel().playCard(player, i.id, player.hand));
		game.bmm.getDiscardModel().finishDiscarding(player);
	}

	@Override
	public void handleEvent(PropertyChangeEvent evt) {
		handlePropertyChangeEvent(evt);
	}

}
