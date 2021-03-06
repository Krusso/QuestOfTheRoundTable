package com.qotrt.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.qotrt.messages.discard.HandFullFinishPickingServer;
import com.qotrt.messages.discard.HandFullServer;
import com.qotrt.messages.events.EventDiscardCardsServer;
import com.qotrt.messages.events.EventDiscardFinishPickingServer;
import com.qotrt.messages.events.EventDiscardOverServer;
import com.qotrt.messages.game.BattlePointsServer;
import com.qotrt.messages.game.GameJoinServer;
import com.qotrt.messages.game.GameListServer;
import com.qotrt.messages.game.GameStartServer;
import com.qotrt.messages.game.MiddleCardServer;
import com.qotrt.messages.game.PlayCardServer;
import com.qotrt.messages.game.ShieldCountServer;
import com.qotrt.messages.gameover.FinalTournamentNotifyServer;
import com.qotrt.messages.gameover.GameOverServer;
import com.qotrt.messages.hand.AddCardsServer;
import com.qotrt.messages.hand.FaceUpDiscardServer;
import com.qotrt.messages.hand.FaceUpServer;
import com.qotrt.messages.quest.BidDiscardFinishPickingServer;
import com.qotrt.messages.quest.FinishPickingStagesServer;
import com.qotrt.messages.quest.QuestBidServer;
import com.qotrt.messages.quest.QuestDiscardCardsServer;
import com.qotrt.messages.quest.QuestJoinServer;
import com.qotrt.messages.quest.QuestJoinedServer;
import com.qotrt.messages.quest.QuestPickCardsServer;
import com.qotrt.messages.quest.QuestPickStagesServer;
import com.qotrt.messages.quest.QuestSponsorServer;
import com.qotrt.messages.quest.QuestUpServer;
import com.qotrt.messages.quest.QuestWinServer;
import com.qotrt.messages.rank.RankServer;
import com.qotrt.messages.special.CheatServer;
import com.qotrt.messages.special.MerlinServer;
import com.qotrt.messages.special.MordredServer;
import com.qotrt.messages.tournament.TournamentAcceptDeclineServer;
import com.qotrt.messages.tournament.TournamentAcceptedDeclinedServer;
import com.qotrt.messages.tournament.TournamentPickCardsServer;
import com.qotrt.messages.tournament.TournamentWinServer;

@JsonTypeInfo(  
	    use = Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "java_class",
	    visible = true)
	// Needed for jackson converting to abstract class 
	@JsonSubTypes({
	    @Type(value = GameListServer.class, name = "LISTSERVER"), 
	    @Type(value = GameJoinServer.class, name = "JOINGAME"),
	    @Type(value = AddCardsServer.class, name = "ADDCARDS"),
	    @Type(value = ShieldCountServer.class, name = "SHIELDCOUNT"),
	    @Type(value = RankServer.class, name = "RANKUPDATE"),
	    @Type(value = MiddleCardServer.class, name = "SHOWMIDDLECARD"),
	    @Type(value = TournamentAcceptDeclineServer.class, name="JOINTOURNAMENT"),
	    @Type(value = TournamentAcceptedDeclinedServer.class, name="JOINEDTOURNAMENT"),
	    @Type(value = TournamentWinServer.class, name="WINTOURNAMENT"),
	    @Type(value = TournamentPickCardsServer.class, name="PICKTOURNAMENT"),
	    @Type(value = PlayCardServer.class, name="PLAYCARD"),
	    @Type(value = GameStartServer.class, name="GAMESTART"),
	    @Type(value = QuestSponsorServer.class, name="SPONSORQUEST"),
	    @Type(value = QuestJoinServer.class, name="JOINQUEST"),
	    @Type(value = QuestPickStagesServer.class, name="PICKSTAGES"),
	    @Type(value = QuestPickCardsServer.class, name="PICKQUEST"),
	    @Type(value = QuestWinServer.class, name="WINQUEST"),
	    @Type(value = QuestPickStagesServer.class, name="PICKSTAGES"),
	    @Type(value = QuestBidServer.class, name="BIDQUEST"),
	    @Type(value = QuestDiscardCardsServer.class, name="DISCARDQUEST"),
	    @Type(value = QuestUpServer.class, name="UPQUEST"),
	    @Type(value = EventDiscardCardsServer.class, name="EVENTDISCARD"),
	    @Type(value = EventDiscardFinishPickingServer.class, name="FINISHPICKEVENT"),
	    @Type(value = EventDiscardOverServer.class, name = "EVENTDISCARDOVER"),
	    @Type(value = HandFullServer.class, name = "HANDDISCARD"),
	    @Type(value = HandFullFinishPickingServer.class, name = "FINISHDISCARD"),
	    @Type(value = BattlePointsServer.class, name = "BATTLEPOINTS"),
	    @Type(value = FaceUpServer.class, name ="FACEUPCARDS"),
	    @Type(value = FaceUpDiscardServer.class, name ="DISCARDFACEUP"),
	    @Type(value = FinalTournamentNotifyServer.class, name = "JOINEDFINALTOURNAMENT"),
	    @Type(value = GameOverServer.class, name = "GAMEOVER"),
	    @Type(value = MordredServer.class, name = "MORDRED"),
	    @Type(value = MerlinServer.class, name = "MERLIN"),
	    @Type(value = BidDiscardFinishPickingServer.class, name = "FINISHBIDDISCARD"),
	    @Type(value = FinishPickingStagesServer.class, name = "FINISHSTAGESETUP"),
	    @Type(value = QuestJoinedServer.class, name = "JOINEDQUEST"),
	    @Type(value = CheatServer.class, name ="CHEATS")
	    })  
public abstract class Message {

	public final String TYPE = "GAME";

	public MESSAGETYPES messageType;
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
		WINTOURNAMENT, 
		SHIELDCOUNT, 
		RANKUPDATE, 
		ADDCARDS, 
		FACEDOWNCARD, 
		LISTSERVER, 
		PLAYCARD, 
		SHOWMIDDLECARD, 
		JOINEDTOURNAMENT, 
		FINISHPICKTOURNAMENT, 
		SPONSERQUEST, 
		JOINQUEST, 
		PICKSTAGES, 
		PICKQUEST, 
		WINQUEST, 
		BIDQUEST, 
		DISCARDQUEST, 
		UPQUEST, 
		BATTLEPOINTS, 
		EVENTDISCARD, 
		FINISHPICKEVENT, 
		EVENTDISCARDOVER,
		HANDDISCARD,
		FINISHDISCARD, 
		FACEUPCARDS, 
		DISCARDFACEUP, 
		JOINEDFINALTOURNAMENT, 
		GAMEOVER, 
		FINISHFINALTOURNAMENT, 
		FINISHBIDDISCARD, 
		FINISHSTAGESETUP,
		MORDRED,
		MERLIN, 
		JOINEDQUEST, 
		CHEAT
	};
	
//	@Override
//	public String toString() {
//		return "Message type: " + this.message;
//	}
}
