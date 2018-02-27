package src.messages;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import src.game_logic.BoardModel;
import src.messages.Message.MESSAGETYPES;
import src.messages.game.CalculatePlayerClient;
import src.messages.game.CalculatePlayerServer;
import src.messages.game.CalculateStageClient;
import src.messages.game.CalculateStageServer;
import src.messages.game.MordredClient;
import src.messages.hand.FaceUpDiscardServer;
import src.player.BattlePointCalculator;
import src.player.PlayerManager;
import src.socket.OutputController;

public class QOTRTQueue extends LinkedBlockingQueue<String> {
	final static Logger logger = LogManager.getLogger(QOTRTQueue.class);
	
	/**
	 * idk serialization :) got rid of the eclipse warning
	 */
	private static final long serialVersionUID = -165091956128205657L;
	private Gson gson = new Gson();
	private JsonParser json = new JsonParser();
	private PlayerManager pm;
	private OutputController output; 
	private BoardModel bm;

	@Override
	public String take() {
		String message = null;
		try {
			// we aint never getting an error right c:
			message = super.take();
			while(true) {
				logger.info("message in qotrtqueue: " + message);
				JsonObject x = json.parse(message).getAsJsonObject();
				if(x.get("message").getAsString().equals(MESSAGETYPES.MORDRED.name())) {
					MordredClient mc = gson.fromJson(x, MordredClient.class);
					handleMordred(mc);
				} else if ("merlin stage: 1".equals(message)) {
					// TODO handle merlin
					// currently handled by UI
				} else if(x.get("message").getAsString().equals(MESSAGETYPES.CALCULATEPLAYER.name())) {
					BattlePointCalculator bc = new BattlePointCalculator(pm);
					CalculatePlayerClient cpc = gson.fromJson(x, CalculatePlayerClient.class);
					output.sendMessage(new CalculatePlayerServer(bc.calculatePlayer(cpc.player, cpc.cards, bm.getCard()), cpc.player));
				} else if(x.get("message").getAsString().equals(MESSAGETYPES.CALCULATESTAGE.name())) {
					BattlePointCalculator bc = new BattlePointCalculator(pm);
					CalculateStageClient csc = gson.fromJson(x, CalculateStageClient.class);
					output.sendMessage(new CalculateStageServer(bc.calculateStage(pm.players[csc.player].hand, csc.cards, bm.getCard()), csc.player, csc.stage));
				}else {
					break;
				}
				message = super.take();
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		return message;
	}

	private void handleMordred(MordredClient mc) {
		this.pm.players[mc.player].hand.getCardByName("Mordred");
		this.pm.players[mc.otherPlayer].getFaceUp().getCardByName(mc.otherAllyCard);
		if(output != null && mc != null) {
			output.sendMessage(new FaceUpDiscardServer(mc.otherPlayer, new String[] {mc.otherAllyCard}));
		}
	}

	public void put(Message message) {
		try {
			super.put(gson.toJson(message));
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	// generics woooo 
	public <T extends Message> T take(Class<T> c, MESSAGETYPES t) {
		while(true) {
			try {
				T x = gson.fromJson(this.take(), c);
				logger.info("Comparing: " + x.message + " to: " + t.name());
				if(!x.message.equals(t)) continue;
				return x;
			} catch (JsonSyntaxException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void setBoardModel(BoardModel bm) {
		this.bm = bm;
	}

	public void setOutputController(OutputController output) {
		this.output = output;
	}

	public void setPlayerManager(PlayerManager pm2) {
		this.pm = pm2;
	}

}