package src.sequence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import src.game_logic.BoardModel;
import src.game_logic.QuestCard;
import src.player.Player;
import src.player.PlayerManager;
import src.game_logic.Card;

public class QuestSequenceManager extends SequenceManager {
	
	QuestCard card;
	Quest quest;
	Player sponsor;
	
	public QuestSequenceManager(QuestCard card) { this.card = card; }
	
	@Override
	public void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm) {
		// Finding player who wants to sponsor quest
		Iterator<Player> players = pm.round();
		while(players.hasNext()) {
			pm.setPlayer(players.next());
			pm.currentQuestionQuest();
			String string;
			try {
				string = actions.take();
				System.out.println("Action recieved: " + string);
				Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			    Matcher m = p.matcher(string);
			    m.find();
				if(m.group(3).equals("sponsors")) {
					pm.currentSponsorQuest();
					break;
				} else {
					pm.currentDeclineQuest();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// determining if anyone decided to sponsor
		List<Player> sponsors = pm.getAllWithState(Player.STATE.YES);
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
			pm.currentQuestionQuest();
			String string;
			try {
				string = actions.take();
				System.out.println("Action recieved: " + string);
				Pattern p = Pattern.compile("(.*)(\\s+)(.*?): player (\\d+)");
			    Matcher m = p.matcher(string);
			    m.find();
				if(m.group(3).equals("join quest")) {
					pm.currentJoinQuest();
				} else {
					pm.currentDeclineQuest();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
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
				questionPlayers(players, pm, actions, "cards picked for quest: player (\\d+) (.*)");
				pm.flipStage(sponsor, quest.getCurrentStage());
				quest.battleFoe(winners, pm);
			} else if (quest.currentStageType() == Quest.TYPE.TEST) {
				pm.flipStage(sponsor, quest.getCurrentStage());
				players = winners.iterator();
				questionPlayers(players, pm, actions, "quest bid: player (\\\\d+) (.*)");
				quest.winBid(winners, pm);
			}
			quest.advanceStage();
			// TODO: discard weapon cards
		}
		if(winners.size() > 0) {
			pm.changeShields(winners, quest.getNumStages());
		}
		// TODO: discard all cards from participants except Allies
		pm.drawCards(sponsor, quest.getNumStages() + quest.getNumCards());
	}
}