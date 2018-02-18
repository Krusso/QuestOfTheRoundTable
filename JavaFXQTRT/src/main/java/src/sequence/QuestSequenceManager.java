package src.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import src.game_logic.BoardModel;
import src.game_logic.QuestCard;
import src.messages.QOTRTQueue;
import src.messages.quest.QuestDiscardCardsClient;
import src.messages.quest.QuestJoinClient;
import src.messages.quest.QuestPickCardsClient;
import src.messages.quest.QuestSponsorClient;
import src.player.Player;
import src.player.PlayerManager;

public class QuestSequenceManager extends SequenceManager {

	QuestCard card;
	Quest quest;
	Player sponsor;

	public QuestSequenceManager(QuestCard card) { this.card = card; }

	@Override
	public void start(QOTRTQueue actions, PlayerManager pm, BoardModel bm) {
		// Finding player who wants to sponsor quest
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			Player next = players.next();
			pm.setPlayer(next);
			pm.setState(next, Player.STATE.QUESTQUESTIONED);
			QuestSponsorClient qsc = actions.take(QuestSponsorClient.class);
			if(qsc.sponser) {
				pm.setState(next, Player.STATE.SPONSORING, card.getNumStages());
				break;
			} else {
				pm.setState(next, Player.STATE.NO);
			}
		}

		// determining if anyone decided to sponsor
		List<Player> sponsors = pm.getAllWithState(Player.STATE.SPONSORING);
		if(sponsors.size() == 0) {
			return;
		} else if(sponsors.size() == 1) {
			sponsor = sponsors.get(0);
			quest = new Quest(card, sponsor);
			quest.setUpQuest(actions,pm);
		} // never more than one sponsor

		pm.flushState();

		// Finding players who want to join
		players = pm.round();
		while(players.hasNext()) {
			Player curr = players.next();
			if(curr == sponsor) continue;
			pm.setPlayer(curr);
			pm.setState(curr, Player.STATE.QUESTJOINQUESTIONED);
			QuestJoinClient qjc = actions.take(QuestJoinClient.class);
			if(qjc.joined) {
				pm.setState(curr, Player.STATE.YES);
			} else {
				pm.setState(curr, Player.STATE.NO);
			}
		}

		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		List<Player> winners = new ArrayList<Player>(participants);

		if(participants.size() != 0) {
			while(winners.size() > 0 && quest.getCurrentStage() < quest.getNumStages()) {
				pm.drawCards(winners, 1);
				if (quest.currentStageType() == Quest.TYPE.FOE) {
					players = winners.iterator();
					// some constraints when choosing cards... can be done on client side c:
					while(players.hasNext()) {
						Player pick = players.next();
						pm.setPlayer(pick);
						pm.setState(pick, Player.STATE.QUESTPICKING);
						QuestPickCardsClient qpcc = actions.take(QuestPickCardsClient.class);
						pm.currentFaceDown(qpcc.cards); 
					}
					pm.flipStage(sponsor, quest.getCurrentStage());
					quest.battleFoe(winners, pm);
					pm.discardWeapons(participants);
				} else if (quest.currentStageType() == Quest.TYPE.TEST) {
					pm.flipStage(sponsor, quest.getCurrentStage());
					players = winners.iterator();
					Pair bidWinner = questionPlayersForBid(players, pm, actions, card);
					// No one decided to bid not sure if legal?
					if(bidWinner == null) {
						winners.clear();
						break;
					}
					pm.setPlayer(bidWinner.player);
					pm.setDiscarding(bidWinner.player, Player.STATE.TESTDISCARD, Math.max(0, bidWinner.cardsToBid(card)));
					QuestDiscardCardsClient qdcc = actions.take(QuestDiscardCardsClient.class);
					pm.discardFromHand(bidWinner.player, qdcc.cards);
					winners.clear();
					winners.add(bidWinner.player);
				}
				quest.advanceStage();
				pm.passStage(winners);
			}
			pm.passQuest(winners);
		}
		if(winners.size() > 0) {
			pm.changeShields(winners, quest.getNumStages());
		}

		// TODO: discard all cards from participants except Allies
		pm.drawCards(sponsor, quest.getNumStages() + quest.getNumCards());
	}
}