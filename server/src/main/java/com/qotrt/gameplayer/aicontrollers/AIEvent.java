package com.qotrt.gameplayer.aicontrollers;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.qotrt.cards.AdventureCard;
import com.qotrt.cards.AdventureCard.TYPE;
import com.qotrt.game.Game;
import com.qotrt.gameplayer.AbstractAI;
import com.qotrt.gameplayer.Player;
import com.qotrt.messages.game.PlayCardClient;
import com.qotrt.messages.game.PlayCardClient.ZONE;
import com.qotrt.model.GenericPairTyped;

public class AIEvent extends AIController {

	public AIEvent(Game game, Player player, AbstractAI ai) {
		super(game, player, ai);
		
		Function<PropertyChangeEvent, Boolean> func = x -> x.getPropertyName().equals("discardEvent");
		Consumer<PropertyChangeEvent> func1 = x -> wrapEvent(x, y -> discardEvent()).accept(x);
		
		events.add(new GenericPairTyped<>(func, func1));
	}
	
	private void discardEvent() {
		List<AdventureCard> cards = null;
		if(player.getTypeCount(TYPE.WEAPONS) >= 1) {
			cards = ai.discardKingsCalltoArms(1, TYPE.WEAPONS);
		} else if(player.getTypeCount(TYPE.FOES) >= 1) {
			cards = ai.discardKingsCalltoArms(Math.min(2,  player.getTypeCount(TYPE.FOES)), TYPE.FOES);
		}
		
		cards.forEach(i -> {
			pc.discardCard(game, player, game.bmm.getEventModel(), new PlayCardClient(player.getID(),
					i.id, ZONE.HAND, ZONE.DISCARD));
			pc.checkValidityAndSend(game, player, new PlayCardClient(player.getID(),
					i.id, ZONE.HAND, ZONE.DISCARD), "");
		});
		
		game.bmm.getEventModel().finishDiscarding(player);
		
	}

	@Override
	public void handleEvent(PropertyChangeEvent evt) {
		handlePropertyChangeEvent(evt);
	}

}
