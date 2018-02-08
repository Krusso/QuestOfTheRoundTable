package src.player;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard;
import src.game_logic.Rank;

public class UIPlayer extends Player{

	public UIPlayer(int id) {
		super(id);
	}
	public int faceDownLength() {
		return faceDownDeckLength();
	}
	public ArrayList<AdventureCard> getPlayerHandAsList() {
		return hand.getDeck();
	}
	public void addCard(AdventureCard c) {
		hand.addCard(c, 1);
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
