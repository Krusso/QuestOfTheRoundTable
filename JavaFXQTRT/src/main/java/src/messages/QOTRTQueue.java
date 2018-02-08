package src.messages;

import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class QOTRTQueue extends LinkedBlockingQueue<String> {
	/**
	 * idk serilization :) got rid of the eclipse warning
	 */
	private static final long serialVersionUID = -165091956128205657L;
	private Gson gson = new Gson();
	
	@Override
	public String take() {
		String message = null;
		try {
			// we aint never getting an error right c:
			message = super.take();
			while(true) {
				if("mordred".equals(message)) {
					// TODO handle mordred
				} else if ("merlin stage: 1".equals(message)) {
					// TODO handle merlin
				} else if ("discard".equals(message)) {
					// TODO handle user discarding cards if 13+ cards in hand
				} else {
					break;
				}
				super.take();
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

}