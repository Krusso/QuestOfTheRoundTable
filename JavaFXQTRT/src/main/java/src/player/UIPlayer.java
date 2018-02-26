package src.player;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import src.game_logic.AdventureCard;
import src.game_logic.Rank;

public class UIPlayer extends Player{

	//TODO:: Move player hand panes etc to this player, it will organize the code much better
	private Pane handPaneUI;
	private Pane faceDownPaneUI;
	private Pane faceUpPaneUI;
	private ImageView playerRankUI;
	
	public int viewableStage = -1;
	
	
	public UIPlayer(int id) {
		super(id);
	}
	
	public void addShields(int s) {
		shields = s;
	}
	public int faceDownLength() {
		return faceDownDeckLength();
	}
	public ArrayList<AdventureCard> getPlayerHandAsList() {
		return hand.getDeck();
	}
	/*
	 * Adds the AdventureCard c to the player's hand
	 */
	public void addCard(AdventureCard c) {
		hand.addCard(c, 1);
//		handPaneUI.getChildren().add(c.getImageView());
	}
	public void removeCard(int pos) {
		hand.getDeck().remove(pos);
	}
	
	public int getHandSize() {
		return hand.getDeck().size();
	}

	public boolean removeCard(AdventureCard c) {
		return hand.getDeck().remove(c);
	}

	public void playCard(AdventureCard card) {
		setFaceDown(card);
	}
	public void playCardFaceDown(AdventureCard card) {
		setFaceDown(card);
	}
	
	private void setFaceDown(AdventureCard card) {
		List<AdventureCard> list = new ArrayList<AdventureCard>();
		list.add(hand.getCard(card));
		faceDown.addCards(list);
	}

	public String getFaceDown() {
		return this.getFaceDownDeck().toString().replaceAll(" ", ",");
	}

	public void setPlayerRank(Rank.RANKS r) {
		rank = r;
	}
}
