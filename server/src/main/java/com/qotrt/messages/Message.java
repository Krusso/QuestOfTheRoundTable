package com.qotrt.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.qotrt.messages.game.GameJoinServer;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.messages.game.ShieldCountServer;
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.messages.rank.RankServer;

@JsonTypeInfo(  
	    use = Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "java-class",
	    visible = true)
	// Needed for jackson converting to abstract class 
	@JsonSubTypes({
	    @Type(value = GameListServer.class, name = "LISTSERVER"), 
	    @Type(value = GameJoinServer.class, name = "JOINGAME"),
	    @Type(value = AddCardsServer.class, name = "ADDCARDS"),
	    @Type(value = ShieldCountServer.class, name = "SHIELDCOUNT"),
	    @Type(value = RankServer.class, name = "RANKUPDATE"),
	    @Type(value = MiddleCardServer.class, name = "SHOWMIDDLECARD"),
	    })  
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
		GAMESTART, 
		JOINGAME, 
		JOINTOURNAMENT, 
		PICKTOURNAMENT, 
		TIETOURNAMENT, 
		WINTOURNAMENT, 
		SHIELDCOUNT, 
		RANKUPDATE, 
		ADDCARDS, 
		FACEDOWNCARD, 
		LISTSERVER, 
		PLAYCARD, 
		SHOWMIDDLECARD
	};
	
//	@Override
//	public String toString() {
//		return "Message type: " + this.message;
//	}
}
