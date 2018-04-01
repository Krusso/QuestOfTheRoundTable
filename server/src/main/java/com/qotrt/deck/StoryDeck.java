package com.qotrt.deck;

import java.util.ArrayList;

import com.qotrt.cards.EventCard;
import com.qotrt.cards.QuestCard;
import com.qotrt.cards.StoryCard;
import com.qotrt.cards.TournamentCard;
import com.qotrt.cards.events.ChivalrousDeed;
import com.qotrt.cards.events.CourtCalledToCamelot;
import com.qotrt.cards.events.KingRecognition;
import com.qotrt.cards.events.KingsCallToArms;
import com.qotrt.cards.events.Plague;
import com.qotrt.cards.events.Pox;
import com.qotrt.cards.events.ProsperityThroughoutTheRealm;
import com.qotrt.cards.events.QueenFavor;

public class StoryDeck extends Deck<StoryCard> {
	
	public StoryDeck() {
		super();
	}
	
	public ArrayList<StoryCard> discards = new ArrayList<StoryCard>();

	void populate() {
//		addCard(new QuestCard("Search for the Holy Grail",5,new String[] {"All", "Sir Percival"}),1);
//		addCard(new QuestCard("Test of the Green Knight",4,new String[] {"Green Knight", "Sir Gawain"}),1);
//		addCard(new QuestCard("Search for the Questing Beast",4,new String[] {}),1);
//		addCard(new QuestCard("Defend the Queen's Honor",4,new String[] {"All", "Sir Lancelot"}),1);
//		addCard(new QuestCard("Rescue the Fair Maiden",3,new String[] {"Black Knight"}),1);
//		addCard(new QuestCard("Journey Through the Enchanted Forest",3,new String[] {"Evil Knight"}),1);
//		addCard(new QuestCard("Vanquish King Arthur's Enemies",3,new String[] {}),2);
//		addCard(new QuestCard("Slay the Dragon",3,new String[] {"Dragon"}),1);
//		addCard(new QuestCard("Boar Hunt",2,new String[] {"Boar"}),2);
//		addCard(new QuestCard("Repel the Saxon Raiders",2,new String[] {"Saxons", "Saxon Knight"}),2);

		addCard(new TournamentCard("Tournament at Camelot",3),1);
//		addCard(new TournamentCard("Tournament at Orkney",2),1);
//		addCard(new TournamentCard("Tournament at Tintagel",1),1);
//		addCard(new TournamentCard("Tournament at York",0),1);

//		addCard(new EventCard("King's Recognition", new KingRecognition()),2);
//		addCard(new EventCard("Queen's Favor", new QueenFavor()),2);
//		addCard(new EventCard("Court Called to Camelot", new CourtCalledToCamelot()),2);
//		addCard(new EventCard("Pox", new Pox()),1);
//		addCard(new EventCard("Plague", new Plague()),1);
//		addCard(new EventCard("Chivalrous Deed", new ChivalrousDeed()),1);
//		addCard(new EventCard("Prosperity Throughout the Realm", new ProsperityThroughoutTheRealm()),1);
//		addCard(new EventCard("King's Call to Arms", new KingsCallToArms()),1);

	}

	public void reshuffle() {
		this.deck.addAll(discards);
	}
}