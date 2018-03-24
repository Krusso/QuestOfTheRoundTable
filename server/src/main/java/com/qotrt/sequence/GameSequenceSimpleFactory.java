package com.qotrt.sequence;

import com.qotrt.cards.QuestCard;
import com.qotrt.cards.StoryCard;
import com.qotrt.cards.TournamentCard;

public class GameSequenceSimpleFactory {
	public SequenceManager createStoryManager(StoryCard card) {
		// TODO: fix the casting doesnt seem right
		if(card.getType() == StoryCard.TYPE.QUEST) {
			return new QuestSequenceManager((QuestCard) card);
		} else if (card.getType() == StoryCard.TYPE.EVENT) {
			//return new EventSequenceManager((EventCard) card);
		} else if (card.getType() == StoryCard.TYPE.TOURNAMENT) {
			return new TournamentSequenceManager((TournamentCard) card);
		} else if (card.getType() == StoryCard.TYPE.GAMEOVER) {
			//return new FinalTournamentSequenceManager();
		}
		return null;
	}
}