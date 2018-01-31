package src.socket;


// NOTE: probably doesnt need to be its own class unless we add casual/ranked etc
public class GameModel {

	private int numPlayers = 4;
	public void setNumPlayers(int x) {
		numPlayers = x;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
}
