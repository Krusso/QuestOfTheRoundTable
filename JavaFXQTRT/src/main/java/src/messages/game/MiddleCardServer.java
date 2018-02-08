package src.messages.game;

import src.messages.Message;

//from server
public class MiddleCardServer extends Message {
	public String card;
	
	public MiddleCardServer(String card) {
		super();
		this.card = card;
	}
	
	@Override
	public void setMessage() {
		this.message = "SHOW MIDDLECARD";
	}

}
