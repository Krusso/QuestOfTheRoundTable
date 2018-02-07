package src.messages;

public abstract class Message {
	
	public final String TYPE = "GAME";
	public String message;
	public int player;
	
	public Message() {
		this.setMessage();
	}
	
	public Message(int player) {
		super();
		this.player = player;
	}
	
	// set the message to what is expected
	public abstract void setMessage();


}
