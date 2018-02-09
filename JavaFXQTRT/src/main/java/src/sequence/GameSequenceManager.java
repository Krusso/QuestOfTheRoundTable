package src.sequence;

import src.game_logic.EventCard;
import src.game_logic.StoryCard;
import src.game_logic.TournamentCard;
import src.game_logic.QuestCard;

public class GameSequenceManager {
	public SequenceManager createStoryManager(StoryCard card) {
		
		// TODO: fix the casting doesnt seem right
		if(card.getType() == StoryCard.TYPE.QUEST) {
			// make quest sequence
			return new QuestSequenceManager((QuestCard) card);
		} else if (card.getType() == StoryCard.TYPE.EVENT) {
			return new EventSequenceManager((EventCard) card);
		} else if (card.getType() == StoryCard.TYPE.TOURNAMENT) {
			return new TournamentSequenceManager((TournamentCard) card);
		} else if (card.getType() == StoryCard.TYPE.GAMEOVER) {
			return new FinalTournamentSequenceManager();
		}
		return null;
	}
}