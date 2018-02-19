package src.messages.hand;

import java.util.ArrayList;
import java.util.Iterator;

import src.messages.Message;
import src.player.Player;

//from server
public class FaceUpServer extends Message {
	public Pair[] cards;

	public FaceUpServer(Iterator<Player> round) {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		System.out.println("Player stuff -----: " + round);
		round.forEachRemaining(i -> {
			System.out.println(i.getFaceUp().getDeck());
			pairs.add(new Pair(
					i.getFaceUp().getDeck().stream().map(c -> c.getName()).toArray(size -> new String[size]),
					i.getID()));
		});
		cards = pairs.stream().toArray(size -> new Pair[size]);
	}

	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.FACEUPCARDS;
	}
}

class Pair {

	public String[] cards;
	public int player;

	public Pair(String[] cards, int player) {
		this.cards = cards;
		this.player = player;
	}
}
