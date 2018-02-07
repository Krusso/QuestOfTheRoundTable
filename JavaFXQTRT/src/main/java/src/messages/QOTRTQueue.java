package src.messages;

import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

public class QOTRTQueue extends LinkedBlockingQueue<String> {
	/**
	 * idk serilization :) got rid of the eclipse warning
	 */
	private static final long serialVersionUID = -165091956128205657L;

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
	
	public <T extends Message>Message take(Class<T> c) {
		Gson gson = new Gson();
		T x = gson.fromJson(this.take(), c);
		return x;
	}

}