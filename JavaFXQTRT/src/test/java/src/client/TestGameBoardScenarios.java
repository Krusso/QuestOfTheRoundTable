package src.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import src.client.GameBoardController;
import src.client.GameBoardController.GAME_STATE;
import src.client.TitleScreenController;
import src.game_logic.AdventureCard;
import src.game_logic.Rank.RANKS;
import static org.junit.Assert.*;

import static org.junit.Assert.assertTrue;

import static org.testfx.api.FxAssert.verifyThat;

import java.util.ArrayList;

public class TestGameBoardScenarios extends TestFXBase {
	final static Logger logger = LogManager.getLogger(TestGameBoardScenarios.class);
	
	GameBoardController gbc;
	TitleScreenController tsc;
	
	
	final String START_TURN = "#startTurn";
	final String ACCEPT = "#accept";
	final String DECLINE = "#decline";
	final String END_TURN = "#endTurn";
	final String TOAST = "#toast";
	final String DISCARD = "#discard";
	final String USE_MERLIN = "#useMerlin";
	final String CONTINUE = "#nextTurn";

	public void setup2Players(){
		//Rig the game
		press(KeyCode.LEFT);
		clickOn(NEW_GAME_BUTTON_ID);
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);
	
		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_HUMAN_ID);
		clickOn(MENU_BUTTON_2_ID);
		clickOn(TITLE_PANE_2_ID);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		clickOn(START_BUTTON_ID); 
		
		WaitForAsyncUtils.waitForFxEvents();

		logger.info("m: " + m);
		//get tsc
		tsc = m.getTitleScreenController();
	}
	
	public void setupScenario1(){
		//Rig the game
		press(KeyCode.DOWN);
		clickOn(NEW_GAME_BUTTON_ID);
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);
	
		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_HUMAN_ID);
		clickOn(TITLE_PANE_2_ID);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		
		clickOn(MENU_BUTTON_3_ID);
		clickOn(MENU_OPTION_3_HUMAN_ID);
		clickOn(TITLE_PANE_3_ID);
		sleep(1000);
		clickOn(NEXT_SHIELD_BUTTON_3_ID);
		clickOn(NEXT_SHIELD_BUTTON_3_ID);
		
		clickOn(MENU_BUTTON_4_ID);
		clickOn(MENU_OPTION_4_HUMAN_ID);
		clickOn(TITLE_PANE_4_ID);
		clickOn(PREV_SHIELD_BUTTON_4_ID);
		
		clickOn(START_BUTTON_ID); 
		
		WaitForAsyncUtils.waitForFxEvents();

		logger.info("m: " + m);
		//get tsc
		tsc = m.getTitleScreenController();
	}
	
	public void setupScenario2() throws InterruptedException {
		//Rig the game
		press(KeyCode.Z);
		clickOn(NEW_GAME_BUTTON_ID);
		clickOn(MENU_BUTTON_1_ID);
		clickOn(MENU_OPTION_1_HUMAN_ID);
	
		clickOn(MENU_BUTTON_2_ID);
		clickOn(MENU_OPTION_2_HUMAN_ID);
		clickOn(TITLE_PANE_2_ID);
		Thread.sleep(1000);
		clickOn(NEXT_SHIELD_BUTTON_2_ID);
		
		clickOn(MENU_BUTTON_3_ID);
		clickOn(MENU_OPTION_3_HUMAN_ID);
		clickOn(TITLE_PANE_3_ID);
		Thread.sleep(1000);
		clickOn(NEXT_SHIELD_BUTTON_3_ID);
		Thread.sleep(1000);
		clickOn(NEXT_SHIELD_BUTTON_3_ID);
		
		clickOn(MENU_BUTTON_4_ID);
		clickOn(MENU_OPTION_4_HUMAN_ID);
		clickOn(TITLE_PANE_4_ID);
		clickOn(PREV_SHIELD_BUTTON_4_ID);
		
		clickOn(START_BUTTON_ID); 
		
		WaitForAsyncUtils.waitForFxEvents();

		logger.info("m: " + m);
		//get tsc
		tsc = m.getTitleScreenController();
	}
	
	@Test
	public void testScenario1() {
		setupScenario1();
		sleep(1000);
		gbc = tsc.getGameBoardController();
		clickOn(START_TURN);
		logger.info(gbc.CURRENT_STATE);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.SPONSOR_QUEST);
		
		// are both accept/decline buttons visible?
		BooleanBinding acceptVisible = Bindings.selectBoolean(gbc.accept.sceneProperty(), "window", "showing");
		assertTrue(acceptVisible.get() == true);
		BooleanBinding declineVisible = Bindings.selectBoolean(gbc.decline.sceneProperty(), "window", "showing");
		assertTrue(declineVisible.get() == true);
		// p0 is going to accept the quest
		clickOn(ACCEPT);
		// Once player accepts, it changes to pick_stages
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.PICK_STAGES);
		
		Pane stage1 = gbc.stages[0];
		Pane stage2 = gbc.stages[1];
		Pane handPane1 = gbc.playerhand0;
		ImageView boar = gbc.findCardInHand("Boar");
		ImageView saxons = gbc.findCardInHand("Saxons");
		ImageView dagger = gbc.findCardInHand("Dagger");
		ImageView sword = gbc.findCardInHand("Sword");
		
		// make sure we can select cards for both stages
		drag(saxons).moveTo(stage1).release(MouseButton.PRIMARY);
		assertTrue(stage1.getChildren().contains(saxons));

		drag(boar).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(boar));
		drag(dagger).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(dagger));
		drag(sword).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(sword));
		
		clickOn(END_TURN);
		
		// make sure all other players are prompted to join
		// and accept as all players
		int size2,size3,size4,newSize2,newSize3,newSize4=0;
		
		Pane handPane2 = gbc.playerHand1;
		size2 = handPane2.getChildren().size();
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		clickOn(ACCEPT);

		Pane handPane3 = gbc.playerHand2;
		size3 = handPane3.getChildren().size();
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		clickOn(ACCEPT);
		
		Pane handPane4 = gbc.playerHand3;
		size4 = handPane4.getChildren().size();
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		clickOn(ACCEPT);
		
		newSize2 = handPane2.getChildren().size();
		newSize3 = handPane3.getChildren().size();
		newSize4 = handPane4.getChildren().size();
		
		// make sure everyone drew a card
		assertTrue(newSize2-size2==1);
		assertTrue(newSize3-size3==1);
		assertTrue(newSize4-size4==1);
		
		clickOn(START_TURN);
		// make sure everyone is forced to discard
		if(newSize2>12) {
			assertTrue(gbc.CURRENT_STATE == GAME_STATE.DISCARDING_CARDS);
			assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
			// discard 1 card
			ImageView battleax = gbc.findCardInHand("Battle-ax");
			drag(battleax).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
			clickOn(DISCARD);
			assertTrue(handPane2.getChildren().size() <= 12);
		}
		clickOn(START_TURN);
		if(newSize3>12) {
			assertTrue(gbc.CURRENT_STATE == GAME_STATE.DISCARDING_CARDS);
			assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
			// discard 1 card
			ImageView battleax = gbc.findCardInHand("Battle-ax");
			drag(battleax).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
			clickOn(DISCARD);
			assertTrue(handPane3.getChildren().size() <= 12);
		}
		clickOn(START_TURN);
		if(newSize4>12) {
			assertTrue(gbc.CURRENT_STATE == GAME_STATE.DISCARDING_CARDS);
			assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
			ImageView battleax = gbc.findCardInHand("Battle-ax");
			drag(battleax).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
			clickOn(DISCARD);
			assertTrue(handPane3.getChildren().size() <= 12);
		}
		
		clickOn(START_TURN);
		// play no cards
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		clickOn(END_TURN);
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.CHILLING);

		clickOn(START_TURN);
		// play horse
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		ImageView horse = gbc.findCardInHand("Horse");
		drag(horse).moveTo(gbc.playerFaceDown2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		sleep(2000);
		clickOn(START_TURN);
		// play battle ax
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		ImageView ax = gbc.findCardInHand("Battle-ax");
		drag(ax).moveTo(gbc.playerFaceDown3).release(MouseButton.PRIMARY);
		sleep(1000); // sometimes it clicks too soon idk this helps
		clickOn(END_TURN);
		
		sleep(1000); // more lags c:
		assertTrue(gbc.toast.getText().equals("Players #: 3,4 passed"));
		
		sleep(5000); // wait for cards to flip over
		
		// p1 didn't play any cards
		ArrayList<AdventureCard> p2Cards = gbc.playerManager.getFaceUpCardsAsList(2);
		for(int i=0;i<p2Cards.size();i++) {
			assertTrue(p2Cards.get(i).isCardFaceUp());
		}
		ArrayList<AdventureCard> p3Cards = gbc.playerManager.getFaceUpCardsAsList(3);
		for(int i=0;i<p3Cards.size();i++) {
			assertTrue(p3Cards.get(i).isCardFaceUp());
		}
		
		clickOn(START_TURN);
		
		// p3 faceUp size should be 0, p4 should be 1 (amour)
		// and make sure they drew a card
		sleep(1000);
		assertEquals(gbc.playerFaceUp2.getChildren().size(),0);
		assertEquals(gbc.playerFaceUp3.getChildren().size(),0);
		assertEquals(gbc.playerHand2.getChildren().size(),12);
		assertEquals(gbc.playerHand3.getChildren().size(),12);
		
		clickOn(START_TURN);
		// play excalibur
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		ImageView exc = gbc.findCardInHand("Excalibur");
		drag(exc).moveTo(gbc.playerFaceDown2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.CHILLING);
		
		clickOn(START_TURN);
		// play lance
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		ImageView lance = gbc.findCardInHand("Lance");
		drag(lance).moveTo(gbc.playerFaceDown3).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.CHILLING);
		
		sleep(5000); // wait for cards to flip
		
		assertTrue(gbc.toast.getText().equals("Player #: 3 passed the stage"));
		
		clickOn(START_TURN);
		clickOn(START_TURN);
		
		assertEquals(gbc.playerhand0.getChildren().size(),14);
		
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.DISCARDING_CARDS);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		// discard 2 cards to get back to 12
		ImageView merlin = gbc.findCardInHand("Merlin");
		drag(merlin).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		ImageView thieves = gbc.findCardInHand("Thieves");
		drag(thieves).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		assertTrue(handPane1.getChildren().size() <= 12);

		clickOn(START_TURN);
		
		assertEquals(gbc.pickStage0.getChildren().size(),0);
		assertEquals(gbc.pickStage1.getChildren().size(),0);
//		assertEquals(Integer.parseInt(gbc.p3Shields.getText()),2);
		assertTrue(gbc.p3Shields.getText().equals("2"));
		
		clickOn(START_TURN);
		
		assertEquals(gbc.playerhand0.getChildren().size(),14);
		assertEquals(gbc.playerHand1.getChildren().size(),14);
		assertEquals(gbc.playerHand2.getChildren().size(),13);
		assertEquals(gbc.playerHand3.getChildren().size(),13);
		
		// make sure everyone has to discard
		
		// for player 2 it says to discard a weapon (not weapons) but at this point we have 14 for p2
		// so we have to discard 2 can someone pls confirm
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView weapon2a = gbc.findCardInHandByType("Weapon");
		drag(weapon2a).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		ImageView weapon2b = gbc.findCardInHandByType("Weapon");
		drag(weapon2b).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		
		sleep(1000);
		
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView amour = gbc.findCardInHand("Amour");
		drag(amour).moveTo(gbc.playerFaceDown2).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		
		sleep(1000);
		
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView foe = gbc.findCardInHandByType("Foe");
		drag(foe).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		
		sleep(1000);
		
		clickOn(START_TURN);

		assertEquals(gbc.playerHand1.getChildren().size(),12);
		assertEquals(gbc.playerHand2.getChildren().size(),12);
		assertEquals(gbc.playerHand3.getChildren().size(),12);
		
		// JP doesnt say to do this but i think we need to can someone confirm?
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView weapon1a = gbc.findCardInHandByType("Weapon");
		drag(weapon1a).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		ImageView weapon1b = gbc.findCardInHandByType("Weapon");
		drag(weapon1b).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);

		clickOn(START_TURN);
		clickOn(START_TURN);
		assertEquals(gbc.p1Shields.getText(), "3");
		assertEquals(gbc.p2Shields.getText(), "3");
		assertEquals(gbc.p3Shields.getText(), "2");
		assertEquals(gbc.p4Shields.getText(), "3");
		
		sleep(3000);

	}
	
	@Test
	public void testScenario2() throws InterruptedException {
		setupScenario2();
		gbc = tsc.getGameBoardController();
		// *** p1 ***
		Thread.sleep(20);
		clickOn(START_TURN);
		Thread.sleep(20);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.SPONSOR_QUEST);
		clickOn(DECLINE);
		// *** p2 ***
		Thread.sleep(20);
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.SPONSOR_QUEST);
		Thread.sleep(20);
		clickOn(ACCEPT);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.PICK_STAGES);
		// setting up quest
		Pane stage1 = gbc.stages[0];
		Pane stage2 = gbc.stages[1];
//		Pane handPane1 = gbc.playerhand0;
		ImageView thieves = gbc.findCardInHand("Thieves");
		ImageView sword = gbc.findCardInHand("Sword");
		ImageView saxons = gbc.findCardInHand("Evil Knight");
		ImageView battleax = gbc.findCardInHand("Battle-ax");
		drag(thieves).moveTo(stage1).release(MouseButton.PRIMARY);
		assertTrue(stage1.getChildren().contains(thieves));
		drag(sword).moveTo(stage1).release(MouseButton.PRIMARY);
		assertTrue(stage1.getChildren().contains(sword));
		drag(saxons).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(saxons));
		drag(battleax).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(battleax));
		Thread.sleep(20);
		clickOn(END_TURN);
		// *** p1 ***
		Thread.sleep(20);
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		Thread.sleep(20);
		clickOn(ACCEPT);
		// *** p3 ***
		// don't participate
		Thread.sleep(20);
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		Thread.sleep(20);
		clickOn(DECLINE);
		// *** p4 ***
		Thread.sleep(20);
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		Thread.sleep(20);
		clickOn(ACCEPT);
		
		
		//DISCARDING
		// *** p1 ***
		Thread.sleep(20);
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView dagger1 = gbc.findCardInHand("Dagger");
		drag(dagger1).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		// *** p4 ***
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView dagger4 = gbc.findCardInHand("Dagger");
		drag(dagger4).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		
		// STAGE 1
		// *** p4 ***
		// play no cards and get eliminated
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		Thread.sleep(20);
		clickOn(END_TURN);
		// *** p1 ***
		Thread.sleep(20);
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		ImageView horse1 = gbc.findCardInHand("Horse");
		drag(horse1).moveTo(gbc.playerFaceDown0).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		// make sure p4 got eliminated
		sleep(1000);
		assertTrue(gbc.toast.getText().equals("Player #: 1 passed the stage"));
		sleep(5000); // wait for cards to flip over
		
		// STAGE 2
		// *** p1 ***
		// play no cards and get eliminated
		clickOn(START_TURN);
		clickOn(END_TURN);
		// make sure p1 got eliminated
		sleep(1000);
		assertTrue(gbc.toast.getText().equals("No players passed the stage"));
		sleep(5000);
		
		// next quest
		clickOn(START_TURN);
		clickOn(START_TURN);
		
		// discard first
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView lance = gbc.findCardInHand("Lance");
		drag(lance).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		ImageView exc = gbc.findCardInHand("Excalibur");
		drag(exc).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		
		clickOn(START_TURN);
		clickOn(START_TURN);
		sleep(5000);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.SPONSOR_QUEST);
		clickOn(ACCEPT);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.PICK_STAGES);
		// setting up quest
		stage1 = gbc.stages[0];
		stage2 = gbc.stages[1];
		ImageView saxons1 = gbc.findCardInHand("Saxons");
		ImageView giant = gbc.findCardInHand("Giant");
		drag(saxons1).moveTo(stage1).release(MouseButton.PRIMARY);
		assertTrue(stage1.getChildren().contains(saxons1));
		drag(giant).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(giant));
		clickOn(END_TURN);
		// playing quest
		// *** p1 ***
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		clickOn(ACCEPT);
		// *** p3 ***
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		clickOn(ACCEPT);
		// *** p4 ***
		clickOn(START_TURN);
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.JOIN_QUEST);
		clickOn(ACCEPT);
		
		// discard first
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView boar = gbc.findCardInHand("Boar");
		drag(boar).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView boar2 = gbc.findCardInHand("Boar");
		drag(boar2).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		ImageView boar3 = gbc.findCardInHand("Boar");
		drag(boar3).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		clickOn(START_TURN);
		
		// select cards
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		ImageView weap = gbc.findCardInHandByType("Weapon");
		drag(weap).moveTo(gbc.playerFaceDown2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		clickOn(END_TURN);
		clickOn(START_TURN);
		assertTrue(gbc.toast.getText().equals("Select Cards for current stage"));
		clickOn(END_TURN);
		
		// play no cards and get eliminated
		assertTrue(gbc.toast.getText().equals("No players passed the stage"));
		clickOn(START_TURN);
		clickOn(START_TURN);
	}
	
	@Test
	public void testPlayerHandRankShieldVisbility() {
		setup2Players();
		gbc = tsc.getGameBoardController();
		clickOn(START_TURN);
		
		for(AdventureCard c: gbc.playerManager.players[0].hand.getDeck()) {
			assertTrue(c.isVisibile());
			assertEquals(c.img, c.imgView.getImage());
		}
		
		for(AdventureCard c: gbc.playerManager.players[1].hand.getDeck()) {
			assertTrue(c.isVisibile());
			assertEquals(c.cardBack, c.imgView.getImage());
		}
		
		assertEquals(12, gbc.playerManager.players[0].hand.size());
		assertEquals(12, gbc.playerManager.players[1].hand.size());
		
		assertTrue(gbc.playerRanks[0].isVisible());
		assertTrue(gbc.playerRanks[1].isVisible());
		
		assertEquals(RANKS.SQUIRE, gbc.playerManager.players[0].getRank());
		assertEquals(RANKS.SQUIRE, gbc.playerManager.players[1].getRank());
		
		assertEquals("0", gbc.p1Shields.getText());
		assertEquals("0", gbc.p2Shields.getText());
		assertTrue(gbc.p1Shields.isVisible());
		assertTrue(gbc.p2Shields.isVisible());
		
		
	}
	
	/*
	 * Expect the game to have a quest card "Test of the Green Knight"
	 */
	@Test
	public void test2PlayerGameQuest() throws InterruptedException {
		setup2Players();
		gbc = tsc.getGameBoardController();
		//start turn for first player
		clickOn(START_TURN);
		logger.info(gbc.CURRENT_STATE);
		//make sure we have set the game state to Sponsoring Quest
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.SPONSOR_QUEST);
		//p0 is going to accept the quest
		clickOn(ACCEPT);
		//Once player accepts, it changes to pick_stages
		assertTrue(gbc.CURRENT_STATE ==  GAME_STATE.PICK_STAGES);
		//setup the quest for Rescue The fair Maiden (3 stages)
		
		//expect error message since stage is not set up
		clickOn(END_TURN);
		verifyThat(TOAST, (Text t)->{
			if(t.getText().equals("Quest stages are not valid. Each stage needs 1 foe or 1 test")) {
				return true;
			}
			return false;
		});
		Pane stage1 = gbc.stages[0];
		Pane stage2 = gbc.stages[1];
		Pane stage3 = gbc.stages[2];
		Pane stage4 = gbc.stages[3];
		Pane stage5 = gbc.stages[4];
		Pane handPane1 = gbc.playerhand0;
		ImageView thieves = gbc.findCardInHand("Thieves");
		
	
		//testing the drag feature for foe cards
		
		//move thieves to stage1 (should work)
		drag(thieves).moveTo(stage1).release(MouseButton.PRIMARY);
		assertTrue(stage1.getChildren().contains(thieves));

		//move thieves to stage2 (should work)
		drag(thieves).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(!stage1.getChildren().contains(thieves));
		assertTrue(stage2.getChildren().contains(thieves));
		

		//move thieves to stage4 (should not work since quest only has 3 stages)
		drag(thieves).moveTo(stage5).release(MouseButton.PRIMARY);
		assertTrue(stage2.getChildren().contains(thieves));
		assertFalse(stage5.getChildren().contains(thieves));
		
		drag(thieves).moveTo(stage1).release(MouseButton.PRIMARY);

		//Move saxons1 to stage 2 (should work since no other foes)
		ImageView saxons1 = gbc.findCardInHand("Saxons");
		drag(saxons1).moveTo(stage2).release(MouseButton.PRIMARY);
		

		//Move saxons2 to stage 2 (should not work since already a foe there)
		ImageView saxons2 = gbc.findCardInHand("Saxons");
		drag(saxons2).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(!stage2.getChildren().contains(saxons2));
		
		//Move test card to stage 2 (should not work since there are cards already in stage 2)
		ImageView testCard = gbc.findCardInHand("Test of Valor");
		drag(testCard).moveTo(stage2).release(MouseButton.PRIMARY);
		assertTrue(!stage2.getChildren().contains(testCard));
		
		//move testCard to stage 4 (should work since there are no cards in stage 3)
		drag(testCard).moveTo(stage4).release(MouseButton.PRIMARY);
		assertTrue(stage4.getChildren().contains(testCard));
		
		//move saxon to stage 3 so now we have a foe/test card in each stage
		drag(saxons2).moveTo(stage3).release(MouseButton.PRIMARY);
		
		//still should not work since we have stage 2 and 3 with the same battle points (should all be increasing)
		clickOn(END_TURN);
		verifyThat(TOAST, (Text t)->{
			if(t.getText().equals("Quest stages are not valid. Each stage needs an increasing amount of bp")) {
				return true;
			}
			return false;
		});
		
		//drag the saxons2 back into hand pane
		drag(saxons2).moveTo(handPane1).release(MouseButton.PRIMARY);
		assertTrue(handPane1.getChildren().contains(saxons2));
		

		
		drag(testCard).moveTo(handPane1).release(MouseButton.PRIMARY);
		//drag the green knight into stage 3 isntead (should be using 2nd bp
		ImageView greenKnight = gbc.findCardInHand("Green Knight");
		drag(greenKnight).moveTo(stage4).release(MouseButton.PRIMARY);	
		
		drag(saxons2).moveTo(stage3).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Battle-ax")).moveTo(stage3).release(MouseButton.PRIMARY);
		//make sure the BPC are correct
		assertTrue(gbc.bpTextStage0.getText().equals("5"));
		assertTrue(gbc.bpTextStage1.getText().equals("10"));
		assertTrue(gbc.bpTextStage2.getText().equals("25"));
		assertTrue(gbc.bpTextStage3.getText().equals("40"));
		clickOn(END_TURN);

		WaitForAsyncUtils.waitForFxEvents();
		
		//2nd player's turn, we're going to accept the quest
		Pane handPane2 = gbc.playerHand1;
		Pane fdc2 = gbc.playerFaceDown1;
		clickOn(START_TURN); 
		clickOn(ACCEPT);

		WaitForAsyncUtils.waitForFxEvents();
		
		//since we got 13 cards, got to discard one
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.DISCARDING_CARDS);
		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		
		//trying to discard 2 cards
		ImageView battleax = gbc.findCardInHand("Battle-ax");
		drag(battleax).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		ImageView dagger = gbc.findCardInHand("Dagger");
		drag(dagger).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		//won't work since you can only discard 1
		assertTrue(gbc.toast.getText().equals("Can only discard cards if hand has more than 12 cards"));
		
		//going to play merlin instead and put back the other 2 cards
		ImageView merlin = gbc.findCardInHand("Merlin");
		drag(merlin).moveTo(fdc2).release(MouseButton.PRIMARY);
		drag(battleax).moveTo(handPane2).release(MouseButton.PRIMARY);
		drag(dagger).moveTo(handPane2).release(MouseButton.PRIMARY);
		
		//try playing these cards into face up / stage (shouldn't work)
		drag(battleax).moveTo(stage1).release(MouseButton.PRIMARY);
		assertTrue(!stage1.getChildren().contains(battleax));
		drag(battleax).moveTo(gbc.playerFaceUp1).release(MouseButton.PRIMARY);
		assertTrue(!gbc.playerFaceUp1.getChildren().contains(battleax));
		
		//finsh discarding so we'll click it now
		clickOn(DISCARD);
		
		//now p2 should be choosing cards to play for the quest stage 1.
		assertTrue(gbc.CURRENT_STATE == GAME_STATE.QUEST_PICK_CARDS);
		
		//try using Merlin power
		//find the merline card
		ArrayList<AdventureCard> fuc = gbc.playerManager.getFaceUpCardsAsList(1);
		AdventureCard merlinCard;
		for(AdventureCard c : fuc) {
			if(c.getName().equals("Merlin")) {
				merlinCard = c;
				gbc.useMerlinPower(1, merlinCard);
				break;
			}
		}
		//make sure all stage cards are visible
		for(Node n : gbc.stages[1].getChildren()) {
			assertTrue(n.isVisible());
		}
		
		//now play a card for the quest (dagger
		drag(dagger).moveTo(fdc2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		
		WaitForAsyncUtils.waitForFxEvents();
		logger.info(gbc.nextTurn.isVisible());
		clickOn(START_TURN);

		//continue next stage
		Thread.sleep(30);
		drag(gbc.findCardInHand("Battle-ax")).moveTo(fdc2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		clickOn(START_TURN);
		
		drag(gbc.findCardInHand("Lance")).moveTo(fdc2).release(MouseButton.PRIMARY);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		clickOn(END_TURN);
		clickOn(START_TURN);
		
		drag(gbc.findCardInHand("Lance")).moveTo(fdc2).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Excalibur")).moveTo(fdc2).release(MouseButton.PRIMARY);
		clickOn(END_TURN);
		clickOn(START_TURN);

		//now we gotta discard cards for p0 since quest is finished (discard 4 cards)
		clickOn(START_TURN);
		ArrayList<AdventureCard> hand = gbc.playerManager.getPlayerHand(0);
		for(int i = 0 ; i < 4; i++) {
			drag(hand.get(0).getImageView()).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		}
		clickOn(DISCARD);
		clickOn(START_TURN);
		//make sure it is now player1's turn
		WaitForAsyncUtils.waitForFxEvents();
		assertTrue(gbc.playerManager.getCurrentPlayer() == 1);

		assertEquals(4, gbc.playerManager.players[gbc.playerManager.getCurrentPlayer()].getShields());
		assertEquals("4", gbc.p2Shields.getText());
		assertEquals(0, gbc.playerManager.players[0].getShields());
		assertEquals("0", gbc.p1Shields.getText());
	}
	

}
