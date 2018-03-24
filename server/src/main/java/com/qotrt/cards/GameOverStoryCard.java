package com.qotrt.cards;

public class GameOverStoryCard extends StoryCard {

	private GameOverStoryCard(String name, TYPE type) {
		super(name, type);
	}

	public static final StoryCard GAMEOVER = new GameOverStoryCard("GAME OVER TOURNAMENT", TYPE.GAMEOVER);
}
