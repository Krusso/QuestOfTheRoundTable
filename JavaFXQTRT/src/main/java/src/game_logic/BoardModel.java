package src.game_logic;

import src.views.PlayerView;

public class BoardModel {

	private StoryCard card;
	private PlayerView pv;
	private boolean setKingRecognition;

	public StoryCard getCard() {
		return card;
	}

	public void setCard(StoryCard card) {
		this.card = card;
		pv.updateMiddle(card);
	}
	
	public void subscribe(PlayerView pv) {
		this.pv = pv;
	}

	public boolean isSetKingRecognition() {
		return setKingRecognition;
	}

	public void setSetKingRecognition(boolean setKingRecognition) {
		this.setKingRecognition = setKingRecognition;
	}
}
