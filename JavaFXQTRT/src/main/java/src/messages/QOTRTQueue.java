package src.messages;

import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import src.messages.Message.MESSAGETYPES;
import src.messages.hand.HandFullClient;
import src.player.PlayerManager;

public class QOTRTQueue extends LinkedBlockingQueue<String> {
	/**
	 * idk serialization :) got rid of the eclipse warning
	 */
	private static final long serialVersionUID = -165091956128205657L;
	private Gson gson = new Gson();
	private JsonParser json = new JsonParser();
	private PlayerManager pm;
	
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
				} else if (x.get("message").getAsString().equals(MESSAGETYPES.DISCARDHANDFULL.name())) {
					HandFullClient hfc = gson.fromJson(x, HandFullClient.class);
					if(pm != null) {
						this.pm.discardFromHand(hfc.player, hfc.cards);
					}
				} else {
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
	public void setPlayerManager(PlayerManager pm2) {
		this.pm = pm2;
	}

}