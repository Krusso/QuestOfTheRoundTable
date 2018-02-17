package src.game_logic;

public class StoryDeck extends Deck<StoryCard> {
	
	public StoryDeck() {
		super();
	}
	
	void populate() {
<<<<<<< Upstream, based on master
		addCard(new QuestCard("Search for the Holy Grail",5,"All"),1);
		addCard(new QuestCard("Test of the Green Knight",4,"Green Knight"),1);
		addCard(new QuestCard("Search for the Questing Beast",4,"None"),1);
		addCard(new QuestCard("Defend the Queen's Honor",4,"All"),1);
		addCard(new QuestCard("Rescue the Fair Maiden",3,"Black Knight"),1);
		addCard(new QuestCard("Journey Through the Enchanted Forest",3,"Evil Knight"),1);
		addCard(new QuestCard("Vanquish King Arthur's Enemies",3,"None"),2);
		addCard(new QuestCard("Slay the Dragon",3,"Dragon"),1);
		addCard(new QuestCard("Boar Hunt",2,"Boar"),2);
		addCard(new QuestCard("Repel the Saxon Raiders",2,"All Saxons"),2);
		
//		addCard(new TournamentCard("Tournament at Camelot",3),1);
//		addCard(new TournamentCard("Tournament at Orkney",2),1);
//		addCard(new TournamentCard("Tournament at Tintagel",1),1);
//		addCard(new TournamentCard("Tournament at York",0),1);
=======
//		addCard(new QuestCard("Search for the Holy Grail",5,"All"),1);
//		addCard(new QuestCard("Test of the Green Knight",4,"Green Knight"),1);
//		addCard(new QuestCard("Search for the Questing Beast",4,"None"),1);
//		addCard(new QuestCard("Defend the Queen's Honor",4,"All"),1);
//		addCard(new QuestCard("Rescue the Fair Maiden",3,"Black Knight"),1);
//		addCard(new QuestCard("Journey Through the Enchanted Forest",3,"Evil Knight"),1);
//		addCard(new QuestCard("Vanquish King Arthur's Enemies",3,"None"),2);
//		addCard(new QuestCard("Slay the Dragon",3,"Dragon"),1);
//		addCard(new QuestCard("Boar Hunt",2,"Boar"),2);
//		addCard(new QuestCard("Repel the Saxon Raiders",2,"All Saxons"),2);
	
		addCard(new TournamentCard("Tournament at Camelot",3),1);
		addCard(new TournamentCard("Tournament at Orkney",2),1);
		addCard(new TournamentCard("Tournament at Tintagel",1),1);
		addCard(new TournamentCard("Tournament at York",0),1);
>>>>>>> b0c82b2 basic toast for tasks
		
//		addCard(new EventCard("King's Recognition"),2);
//		addCard(new EventCard("Queen's Favor"),2);
//		addCard(new EventCard("Court Called to Camelot"),2);
//		addCard(new EventCard("Pox"),1);
//		addCard(new EventCard("Plague"),1);
//		addCard(new EventCard("Chivalrous Deed"),1);
//		addCard(new EventCard("Prosperity Throughout the Realm"),1);
//		addCard(new EventCard("King's Call to Arms"),1);
	}
}