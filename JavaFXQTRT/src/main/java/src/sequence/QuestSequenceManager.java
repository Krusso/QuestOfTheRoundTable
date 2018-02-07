package src.sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.game_logic.BoardModel;
import src.game_logic.QuestCard;
import src.messages.QOTRTQueue;
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
			String string;
			string = actions.take();
			System.out.println("Action recieved: " + string);
			Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			Matcher m = p.matcher(string);
			m.find();
			if(m.group(3).equals("sponsors")) {
				pm.setState(next, Player.STATE.SPONSORING);
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
			// TODO: set card in center of board
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
			pm.setState(curr, Player.STATE.QUESTQUESTIONED);
			String string;
			string = actions.take();
			System.out.println("Action recieved: " + string);
			Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			Matcher m = p.matcher(string);
			m.find();
			if(m.group(3).equals("join")) {
				pm.setState(curr, Player.STATE.YES);
			} else {
				pm.setState(curr, Player.STATE.NO);
			}
		}
		
		List<Player> participants = pm.getAllWithState(Player.STATE.YES);
		if(participants.size() == 0) {
			return;
		}
		
		List<Player> winners = new ArrayList<Player>(participants);
		
		while(winners.size() > 0 && quest.getCurrentStage() < quest.getNumStages()) {
			pm.drawCards(winners, 1);
			if (quest.currentStageType() == Quest.TYPE.FOE) {
				players = winners.iterator();
				// some constraints when choosing cards... can be done on client side c:
				//questionPlayers(players, pm, actions, "game cards picked for quest: player (\\d+) (.*)");
				
				while(players.hasNext()) {
					Player pick = players.next();
					pm.setPlayer(pick);
					pm.setState(pick, Player.STATE.QUESTPICKING);
					String string;
					string = actions.take();
					Pattern p = Pattern.compile("game cards picked for quest: player (\\d+) (.*)");
					Matcher m = p.matcher(string);
					m.find();
					String cards = m.group(2);
					pm.currentFaceDown(cards);
					System.out.println("Action recieved: " + string); 
				}
				
				pm.flipStage(sponsor, quest.getCurrentStage());
				quest.battleFoe(winners, pm);
				pm.discardWeapons(winners);
			} else if (quest.currentStageType() == Quest.TYPE.TEST) {
				pm.flipStage(sponsor, quest.getCurrentStage());
				players = winners.iterator();
				Player bidWinner = questionPlayersForBid(players, pm, actions, "game quest bid: player (\\d+) (.*)");
				// No one decided to bid not sure if legal?
				if(bidWinner == null) {
					winners.clear();
					break;
				}
				pm.setPlayer(bidWinner);
				pm.setState(bidWinner, Player.STATE.TESTDISCARD);
				String string;
				string = actions.take();
				System.out.println("Action recieved: " + string);
				Pattern p = Pattern.compile("game test discard: player (\\d+) (.*)");
				Matcher m = p.matcher(string);
				m.find();
				pm.discardFromHand(bidWinner, m.group(2));
				winners.clear();
				winners.add(bidWinner);
			}
			quest.advanceStage();
		}

		if(winners.size() > 0) {
			pm.changeShields(winners, quest.getNumStages());
		}
		// TODO: discard all cards from participants except Allies
		pm.drawCards(sponsor, quest.getNumStages() + quest.getNumCards());
	}
}