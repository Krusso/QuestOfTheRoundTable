package com.qotrt.model;

import com.qotrt.cards.AdventureCard;
import com.qotrt.deck.AdventureDeck;
import com.qotrt.gameplayer.Player;

public interface Discard {

	public boolean can();

	public String playCard(Player player, int card, AdventureDeck hand);

	public AdventureCard getCard(Player player, int card);

	public AdventureCard findCard(Player player, int card);
}
