package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//from client
public class FinishPickingStagesClient extends Message {
	
	public FinishPickingStagesClient() {}
	
	public FinishPickingStagesClient(int player) {
		super(player);
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHBIDDISCARD;
	}

}
