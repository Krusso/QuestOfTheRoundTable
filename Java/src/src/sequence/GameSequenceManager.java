package src.sequence;

import src.game_logic.StoryCard;

public class GameSequenceManager {
	public SequenceManager createStoryManager(StoryCard card) {
		if(card.getType() == StoryCard.TYPE.Quest) {
			// make quest sequence
		} else if (card.getType() == StoryCard.TYPE.Event) {
			// make event sequence
		} else if (card.getType() == StoryCard.TYPE.Tournament) {
			// make tournament sequence
		}
		return null;
	}
}