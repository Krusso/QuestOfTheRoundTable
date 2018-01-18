package src.socket;

import java.util.ArrayList;

public class GameModel {

	private ArrayList<Subscriber> subs = new ArrayList<Subscriber>();
	private int numPlayers = 4;
	
	public void setNumPlayers(int x) {
		subs.forEach((y) -> y.update(x) );
		numPlayers = x;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	
	public void subscribe(Subscriber x) {
		subs.add(x);
	}
	
	public void unsubscribe(Subscriber x) {
		subs.remove(x);
	}
	
}
