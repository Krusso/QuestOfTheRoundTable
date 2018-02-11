package src.messages;

public abstract class Message {

	public final String TYPE = "GAME";
	public MESSAGETYPES message;
	public int player;

	public Message() {
		this.setMessage();
	}

	public Message(int player) {
		this();
		this.player = player;
	}

	// set the message to what is expected
	public abstract void setMessage();

	public static enum MESSAGETYPES {
		EVENTDISCARD,
		STARTGAME, 
		CONTINUEGAME, 
		SHOWMIDDLECARD, 
		TURNNEXT, 
		JOINEDFINALTOURNAMENT,
		GAMEOVER,
		ADDCARDS,
		FACEDOWNCARDS,
		FACEUPCARDS,
		SHOWCARDS, 
		BIDQUEST, 
		DISCARDQUEST,
		JOINQUEST, 
		PICKQUEST,
		PICKSTAGES, 
		SPONSERQUEST,
		UPQUEST, 
		RANKUPDATE,
		JOINTOURNAMENT, 
		PICKTOURNAMENT, 
		WINTOURNAMENT, 
		DOWNQUEST, 
		SHIELDCOUNT

	};
}
