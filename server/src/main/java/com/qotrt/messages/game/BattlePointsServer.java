package com.qotrt.messages.game;

import com.qotrt.messages.Message;
import com.qotrt.messages.game.PlayCardClient.ZONE;

// Server
public class BattlePointsServer extends Message {

	public int battlePoints;
	public ZONE zone;
	
	public BattlePointsServer() {}
	
	public BattlePointsServer(int player, int battlePoints, ZONE zone){
		super(player);
		this.battlePoints = battlePoints;
		this.zone = zone;
	}
	
	
	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.BATTLEPOINTS;
	}
}
