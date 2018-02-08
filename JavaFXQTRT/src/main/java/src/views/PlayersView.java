package src.views;

import java.util.ArrayList;

import src.game_logic.AdventureCard;
import src.game_logic.Card;
import src.messages.game.TurnNextServer;
import src.messages.hand.ShowHandServer;
import src.socket.OutputController;

public class PlayersView {
	
	private OutputController output;
	
	public PlayersView(OutputController output) {
		this.output = output;
	}
	
	public void update(int playerTurn, ArrayList<AdventureCard> arrayList) {
		//output.sendMessage("turn next:" + playerTurn);
		//output.sendMessage("hand show:" + hand);
		output.sendMessage(new TurnNextServer(playerTurn));
		output.sendMessage(new ShowHandServer(playerTurn, 
				arrayList.stream().map(e -> e.getName()).toArray(size -> new String[size])));
	}

}
