package client;

import org.junit.Before;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import src.client.GameBoardController;
import src.client.GameBoardController.GAME_STATE;
import src.client.TitleScreenController;
import src.game_logic.QuestCard;
import static org.junit.Assert.*;

import static org.junit.Assert.assertTrue;

import static org.testfx.api.FxAssert.verifyThat;
public class TestGameBoardScenarios extends TestFXBase {
	GameBoardController gbc;
	TitleScreenController tsc;
	
	
	final String START_TURN = "#startTurn";
	final String ACCEPT = "#accept";
	final String END_TURN = "#endTurn";
	final String TOAST = "#toast";
	final String DISCARD = "#discard";
	@Before
	public void setup2Players(){
		//Rig the game
		press(KeyCode.UP);
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
		//get tsc
		tsc = m.getTitleScreenController();
	}
	
	/*
	 * Expect the game to have a quest card "Test of the Green Knight"
	 */
	@Test
	public void test2PlayerGameQuest() {

		gbc = tsc.getGameBoardController();
		//start turn for first player
		clickOn(START_TURN);
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
		assertTrue(!stage5.getChildren().contains(thieves));
		
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
		
		//still should not work since we have stage 2 and 3 with the same battle poitns (should all be increasing)
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
		
		//drag the green knight into stage 3 isntead (should be using 2nd bp
		ImageView greenKnight = gbc.findCardInHand("Green Knight");
		drag(greenKnight).moveTo(stage3).release(MouseButton.PRIMARY);
		
		
		//TODO:: DO TESTING WHEN BPC CALC IS FIXED
		//so for now remove the test card and add in a some saxxons and weapons
		
		drag(testCard).moveTo(handPane1).release(MouseButton.PRIMARY);
		drag(saxons2).moveTo(stage4).release(MouseButton.PRIMARY);
		ImageView excalibur1 = gbc.findCardInHand("Excalibur");
		ImageView lance1 = gbc.findCardInHand("Lance");
		drag(excalibur1).moveTo(stage4).release(MouseButton.PRIMARY);
		drag(lance1).moveTo(stage4).release(MouseButton.PRIMARY);
		//make sure the BPC are correct
		assertTrue(gbc.bpTextStage0.getText().equals("5"));
		assertTrue(gbc.bpTextStage1.getText().equals("10"));
		assertTrue(gbc.bpTextStage2.getText().equals("40"));
		assertTrue(gbc.bpTextStage3.getText().equals("60"));
		clickOn(END_TURN);

		WaitForAsyncUtils.waitForFxEvents();
		
		//2nd player's turn, we're going to accept the quest
		Pane handPane2 = gbc.playerHand1;
		Pane fdc2 = gbc.playerFaceDown1;
		clickOn(START_TURN); 
		clickOn(ACCEPT);

		WaitForAsyncUtils.waitForFxEvents();
		sleep(1000);
		System.out.println(gbc.CURRENT_STATE);
		//Hand should be too full since we start off wtih 12 cards (and we just drew one so 13 now)
//		assertTrue(gbc.CURRENT_STATE == GAME_STATE.DISCARDING_CARDS);
//		assertTrue(gbc.toast.getText().equals("Your hand is too full. Play Ally or Amour cards or discard cards until your hand has 12 or less cards"));
		
		ImageView battleax = gbc.findCardInHand("Battle-ax");
		
		drag(battleax).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		drag(gbc.findCardInHand("Dagger")).moveTo(gbc.discardPane).release(MouseButton.PRIMARY);
		assertTrue(gbc.toast.getText().equals("Can only discard cards if hand has more than 12 cards"));
		drag(battleax).moveTo(handPane2).release(MouseButton.PRIMARY);
		clickOn(DISCARD);
		
		
		sleep(2000);

		
	}
	

}
