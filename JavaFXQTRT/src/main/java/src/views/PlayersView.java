package src.views;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard;
import src.messages.game.TurnNextServer;
import src.messages.hand.ShowHandServer;
import src.messages.tournament.TournamentWinServer;
import src.player.Player;
import src.player.Player.STATE;
import src.socket.OutputController;

public class PlayersView {
	
	private OutputController output;
	
	public PlayersView(OutputController output) {
		this.output = output;
	}
	
	public void update(int playerTurn, ArrayList<AdventureCard> arrayList) {
		output.sendMessage(new TurnNextServer(playerTurn));
		output.sendMessage(new ShowHandServer(playerTurn, 
				arrayList.stream().map(e -> e.getName()).toArray(size -> new String[size])));
	}

	public void win(List<Player> winners, STATE win) {
		if(win == Player.STATE.WIN) {
			output.sendMessage(new TournamentWinServer(winners.stream().mapToInt(e -> e.getID()).toArray()));
		}
	}

}
