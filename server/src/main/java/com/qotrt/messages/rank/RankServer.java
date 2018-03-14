package com.qotrt.messages.rank;

import com.qotrt.gameplayer.Rank;
import com.qotrt.messages.Message;


// from server
public class RankServer extends Message {

	public Rank.RANKS newrank;
	
	public RankServer() {}
	
	public RankServer(int player, Rank.RANKS newrank) {
		super(player);
		this.newrank = newrank;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.RANKUPDATE;
	}

}
