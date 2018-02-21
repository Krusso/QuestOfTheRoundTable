package src.messages;

import java.util.concurrent.LinkedBlockingQueue;

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
import src.messages.hand.HandFullClient;
import src.player.BattlePointCalculator;
import src.player.PlayerManager;
import src.socket.OutputController;

public class QOTRTQueue extends LinkedBlockingQueue<String> {
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
				JsonObject x = json.parse(message).getAsJsonObject();
				if("mordred".equals(message)) {
					// TODO handle mordred
				} else if ("merlin stage: 1".equals(message)) {
					// TODO handle merlin
				} else if(x.get("message").getAsString().equals(MESSAGETYPES.CALCULATEPLAYER.name())) {
					BattlePointCalculator bc = new BattlePointCalculator(pm);
					CalculatePlayerClient cpc = gson.fromJson(x, CalculatePlayerClient.class);
					output.sendMessage(new CalculatePlayerServer(bc.calculatePlayer(cpc.player, cpc.cards, bm.getCard()), cpc.player));
				} else if(x.get("message").getAsString().equals(MESSAGETYPES.CALCULATESTAGE.name())) {
					BattlePointCalculator bc = new BattlePointCalculator(pm);
					CalculateStageClient csc = gson.fromJson(x, CalculateStageClient.class);
					output.sendMessage(new CalculateStageServer(bc.calculateStage(csc.player, csc.cards, bm.getCard()), csc.player, csc.stage));
				}else {
					break;
				}
				message = super.take();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public void put(Message message) {
		try {
			super.put(gson.toJson(message));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// generics woooo 
	public <T extends Message> T take(Class<T> c) {
		while(true) {
			try {
				T x = gson.fromJson(this.take(), c);
				return x;
			} catch (JsonSyntaxException e) {

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