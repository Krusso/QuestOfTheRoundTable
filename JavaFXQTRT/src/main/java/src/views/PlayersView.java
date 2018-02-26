package src.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import src.game_logic.AdventureCard;
import src.messages.game.ContinueGameServer;
import src.messages.game.TurnNextServer;
import src.messages.gameend.FinalTournamentNotifyServer;
import src.messages.gameend.GameOverServer;
import src.messages.hand.FaceUpServer;
import src.messages.hand.ShowHandServer;
import src.messages.tournament.TournamentTieServer;
import src.messages.tournament.TournamentWinServer;
import src.player.Player;
import src.player.Player.STATE;
import src.messages.quest.QuestPassAllServer;
import src.messages.quest.QuestPassStageServer;
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
		if(win == Player.STATE.GAMEWON) {
			output.sendMessage(new GameOverServer(winners.stream().mapToInt(e -> e.getID()).toArray()));
		}
	}
	
	public void showFaceUp(Iterator<Player> round) {
		output.sendMessage(new FaceUpServer(round));
	}

	public void passStage(List<Player> winners) {
		output.sendMessage(new QuestPassStageServer(winners));
	}

	public void passQuest(List<Player> winners) {
		output.sendMessage(new QuestPassAllServer(winners));
	}

	public void joinFinalTournament(List<Player> allWithState, STATE state) {
		if(state == Player.STATE.WINNING) {
			output.sendMessage(new FinalTournamentNotifyServer(allWithState.stream().mapToInt(e -> e.getID()).toArray()));
		}
	}
	
	public void sendContinue(String string) {
		output.sendMessage(new ContinueGameServer(string));
	}

	public void showTournamentTie(List<Player> winners) {
		output.sendMessage(new TournamentTieServer(winners.stream().mapToInt(i -> i.getID()).toArray()));
	}

}
