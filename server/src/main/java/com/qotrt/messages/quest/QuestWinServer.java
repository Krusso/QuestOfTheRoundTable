package com.qotrt.messages.quest;

import com.qotrt.messages.Message;


// from server
public class QuestWinServer extends Message {

	public static enum WINTYPES {
		NOSPONSOR, NOJOIN, PASSSTAGE, WON	
	};
	
	public int[] players;
	public WINTYPES type;
	
	public QuestWinServer() {}
	
	public QuestWinServer(int[] is, WINTYPES type) {
		this.players = is;
		this.type = type;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.WINQUEST;
	}

}
