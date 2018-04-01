package com.qotrt.messages.quest;

import com.qotrt.messages.Message;

//Sever
public class FinishPickingStagesServer extends Message{

	public boolean successful;
	public String response;
	
	public FinishPickingStagesServer() {}
	
	public FinishPickingStagesServer(int player, boolean successful, String response) {
		super(player);
		this.successful = successful;
		this.response = response;
	}
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.FINISHSTAGESETUP;
	}
}
