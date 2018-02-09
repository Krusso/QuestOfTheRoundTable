package src.messages.rank;

import src.game_logic.Rank;
import src.messages.Message;


// from server
public class RankServer extends Message {

	public Rank.RANKS newrank;
	
	public RankServer(int player, Rank.RANKS newrank) {
		super(player);
		this.newrank = newrank;
	}
	
	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.RANKUPDATE;
	}

}
