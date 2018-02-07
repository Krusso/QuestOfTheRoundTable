package src.views;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Card;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;
import src.messages.gameend.FinalTournamentNotifyServer;
import src.messages.gameend.GameOverServer;
import src.messages.hand.AddCardsServer;
import src.messages.quest.QuestBidServer;
import src.messages.quest.QuestPickStagesServer;
import src.messages.quest.QuestSponserServer;
import src.messages.rank.RankServer;
import src.messages.tournament.TournamentAcceptDeclineServer;
import src.messages.tournament.TournamentPickCardsServer;
import src.messages.tournament.TournamentWinServer;
import src.player.Player;
import src.player.Player.STATE;
import src.socket.OutputController;

public class PlayerView {

	private OutputController output;

	public PlayerView(OutputController output) {
		this.output = output;
	}

	public void update(RANKS rank, int ID) {
		//output.sendMessage("rank set: player " + ID + " " + rank);
		//TournamentAcceptDeclineServer req = new TournamentAcceptDeclineServer(ID, );
		output.sendMessage(new RankServer(ID, rank));
	}

	public void updateCards(ArrayList<AdventureCard> cards, int ID) {
		//output.sendMessage("add cards: player " + ID + " " + cards);
		String[] cardNames = cards.stream().map(e -> e.getName()).toArray(size -> new String[size]);
		output.sendMessage(new AddCardsServer(ID, cardNames));
	}

	public void updateState(STATE state, int ID) {
		if(state == Player.STATE.QUESTIONED) {
			//output.sendMessage("tournament accept: player " + ID);
			output.sendMessage(new TournamentAcceptDeclineServer(ID));
		} else if(state == Player.STATE.QUESTQUESTIONED) {
			//output.sendMessage("quest deciding: player " + ID);
			output.sendMessage(new QuestSponserServer(ID));
		} else if(state == Player.STATE.SPONSORING) {
			//output.sendMessage("quest sponsoring: player " + ID);
			output.sendMessage(new QuestPickStagesServer(ID));
		} else if(state == Player.STATE.PICKING) {
			//output.sendMessage("tournament picking: player " + ID);
			output.sendMessage(new TournamentPickCardsServer(ID));
		} else if(state == Player.STATE.WIN) {
			//output.sendMessage("tournament won: player " + ID);
			output.sendMessage(new TournamentWinServer(ID));
		} else if(state == Player.STATE.WINNING) {
			//output.sendMessage("final tournament: player " + ID);
			output.sendMessage(new FinalTournamentNotifyServer(ID));
		} else if(state == Player.STATE.GAMEWON) {
			//output.sendMessage("player won: player " + ID);
			output.sendMessage(new GameOverServer(ID));
		} else if(state == Player.STATE.BIDDING) {
			//output.sendMessage("quest bidding: player " + ID);
			output.sendMessage(new QuestBidServer(ID));
		} else if(state == Player.STATE.TESTDISCARD) {
			output.sendMessage("quest bidding discard: player " + ID);
		} else if(state == Player.STATE.QUESTPICKING) {
			output.sendMessage("quest picking: player " + ID);
		}
	}

	public void updateFaceDown(List<AdventureCard> list, int ID) {
		output.sendMessage("face down: player " + ID + " " + list);	
	}
	
	public void updateQuestDown(List<List<Card>> list, int ID) {
		output.sendMessage("quest down: player " + ID + " " + list); // not sure about this
	}
	
	public void updateQuestUp(List<List<Card>> list, int ID) {
		output.sendMessage("quest up: player " + ID + " " + list); // not sure about this either c:
	}

	public void updateFaceUp(AdventureDeck faceUp, int ID) {
		output.sendMessage("quest up: player " + ID + " " + faceUp);
	}

	public void updateState(STATE question, int ID, int i, TYPE type) {
		output.sendMessage("discard hand: player " + ID + " type " + type + " amount " + i);
	}

	public void updateMiddle(StoryCard card) {
		output.sendMessage("middle card: " + card.getName());
	}

}
