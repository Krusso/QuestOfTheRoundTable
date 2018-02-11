package src.views;

import java.util.ArrayList;
import java.util.List;

import src.game_logic.AdventureCard;
import src.game_logic.AdventureCard.TYPE;
import src.game_logic.AdventureDeck;
import src.game_logic.Card;
import src.game_logic.Rank.RANKS;
import src.game_logic.StoryCard;
import src.messages.events.EventDiscardCardsServer;
import src.messages.game.MiddleCardServer;
import src.messages.game.ShieldCountServer;
import src.messages.gameend.FinalTournamentNotifyServer;
import src.messages.gameend.GameOverServer;
import src.messages.hand.AddCardsServer;
import src.messages.hand.FaceDownServer;
import src.messages.hand.FaceUpDiscardServer;
import src.messages.hand.FaceUpServer;
import src.messages.quest.QuestBidServer;
import src.messages.quest.QuestDiscardCardsServer;
import src.messages.quest.QuestDownServer;
import src.messages.quest.QuestJoinServer;
import src.messages.quest.QuestPickCardsServer;
import src.messages.quest.QuestPickStagesServer;
import src.messages.quest.QuestSponsorServer;
import src.messages.quest.QuestUpServer;
import src.messages.rank.RankServer;
import src.messages.tournament.TournamentAcceptDeclineServer;
import src.messages.tournament.TournamentPickCardsServer;
import src.messages.tournament.TournamentWinServer;
import src.player.Player;
import src.player.PlayerManager;
import src.player.Player.STATE;
import src.socket.OutputController;

public class PlayerView {

	private OutputController output;

	public PlayerView(OutputController output) {
		this.output = output;
	}

	public void update(RANKS rank, int ID) {
		output.sendMessage(new RankServer(ID, rank));
	}

	public void updateCards(ArrayList<AdventureCard> cards, int ID) {
		String[] cardNames = cards.stream().map(e -> e.getName()).toArray(size -> new String[size]);
		output.sendMessage(new AddCardsServer(ID, cardNames));
	}

	public void updateState(STATE state, int ID, int numStages) {
		if(state == Player.STATE.SPONSORING) {
			output.sendMessage(new QuestPickStagesServer(ID, numStages));
		}	
	}

	public void updateState(STATE state, int ID) {
		if(state == Player.STATE.QUESTIONED) {
			output.sendMessage(new TournamentAcceptDeclineServer(ID));
		} else if(state == Player.STATE.QUESTQUESTIONED) {
			output.sendMessage(new QuestSponsorServer(ID));
		} else if(state == Player.STATE.PICKING) {
			output.sendMessage(new TournamentPickCardsServer(ID));
		} else if(state == Player.STATE.WIN) {
			output.sendMessage(new TournamentWinServer(ID));
		} else if(state == Player.STATE.WINNING) {
			output.sendMessage(new FinalTournamentNotifyServer(ID));
		} else if(state == Player.STATE.GAMEWON) {
			output.sendMessage(new GameOverServer(ID));
		} else if(state == Player.STATE.TESTDISCARD) {
			output.sendMessage(new QuestDiscardCardsServer(ID));
		} else if(state == Player.STATE.QUESTPICKING) {
			output.sendMessage(new QuestPickCardsServer(ID));
		} else if(state == Player.STATE.QUESTJOINQUESTIONED) {
			output.sendMessage(new QuestJoinServer(ID));
		}
	}

	public void updateFaceDown(List<AdventureCard> list, int ID) {
		String[] cardNames = list.stream().map(e -> e.getName()).toArray(size -> new String[size]);
		output.sendMessage(new FaceDownServer(ID, cardNames));
	}

	public void updateQuestDown(List<List<Card>> list, int ID) {
		String[][] cards = list.stream().
				map(i -> i.stream().map(e -> e.getName()).toArray(size -> new String[size])).
				toArray(String[][]::new);
		output.sendMessage(new  QuestDownServer(ID, cards));
	}

	public void updateQuestUp(List<Card> list, int ID, int stage) {
		String[] cards = list.stream().
				map(e -> e.getName()).toArray(size -> new String[size]);
		output.sendMessage(new QuestUpServer(ID, cards, stage));
	}

	public void updateFaceUp(AdventureDeck faceUp, int ID) {
		String[] cardNames = faceUp.getDeck().stream().map(e -> e.getName()).toArray(size -> new String[size]);
		output.sendMessage(new FaceUpServer(ID, cardNames));
	}

	public void updateState(STATE question, int ID, int i, TYPE type) {
		output.sendMessage(new EventDiscardCardsServer(ID, i, type));
	}

	public void updateMiddle(StoryCard card) {
		output.sendMessage(new MiddleCardServer(card.getName()));
	}

	public void updateShieldCount(int ID, int shields) {
		output.sendMessage(new ShieldCountServer(ID, shields));
	}

	public void discard(int ID, List<Card> removedCards) {
		String[] cardNames = removedCards.stream().map(e -> e.getName()).toArray(size -> new String[size]);
		output.sendMessage(new FaceUpDiscardServer(ID, cardNames));
	}



	public void setBidAmount(STATE bidding, int ID, int maxBidValue, int i) {
		output.sendMessage(new QuestBidServer(ID, maxBidValue,i));
	}

}
