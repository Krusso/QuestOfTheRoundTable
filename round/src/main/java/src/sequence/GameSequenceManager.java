package src.sequence;

import src.game_logic.EventCard;
import src.game_logic.StoryCard;
import src.game_logic.TournamentCard;

public class GameSequenceManager {
	public SequenceManager createStoryManager(StoryCard card) {
		
		// TODO: fix the casting doesnt seem right
		
		if(card.getType() == StoryCard.TYPE.QUEST) {
			// make quest sequence
		} else if (card.getType() == StoryCard.TYPE.EVENT) {
			return new EventSequenceManager((EventCard) card);
		} else if (card.getType() == StoryCard.TYPE.TOURNAMENT) {
			return new TournamentSequenceManager((TournamentCard) card);
		}
		return null;
	}
}