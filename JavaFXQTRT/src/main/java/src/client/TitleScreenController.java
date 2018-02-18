package src.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.messages.game.GameStartClient;

public class TitleScreenController implements Initializable{

	private Client client;
	
	@FXML private Text	numPlayerslabel;
	@FXML private TextField numPlayers;
	@FXML private Button start;
	@FXML private Pane p;
	@FXML public Pane background;
	
	@FXML public Pane menuPane;
	@FXML public Button newGame;
	@FXML public Pane playerSelect;
	/* placeholders for now
	@FXML public Pane audioSelect;
	@FXML public Pane help;
	@FXML public Pane quit;
	 */
	
	private String[] players;
	private Image[] shieldImages;
	
	@FXML private Pane shieldPane1;
	@FXML private Pane shieldPane2;
	@FXML private Pane shieldPane3;
	@FXML private Pane shieldPane4;
	@FXML private ImageView shieldView1;
	@FXML private ImageView shieldView2;
	@FXML private ImageView shieldView3;
	@FXML private ImageView shieldView4;
	private int playerShield1=-1;
	private int playerShield2=-1;
	private int playerShield3=-1;
	private int playerShield4=-1;
	
	@FXML private MenuButton b1;
	@FXML private MenuButton b2;
	@FXML private MenuButton b3;
	@FXML private MenuButton b4;
	
	public void setClient(Client c) {
		client = c;
	}
	public void addImage(ImageView i) {
		p.getChildren().add(i);
	}

	// sorry guys
	@FXML private void setHuman1(ActionEvent e) throws IOException { this.players[0] = "Human"; b1.setText("Human"); }
	@FXML private void setHuman2(ActionEvent e) throws IOException { this.players[1] = "Human"; b2.setText("Human"); }
	@FXML private void setHuman3(ActionEvent e) throws IOException { this.players[2] = "Human"; b3.setText("Human"); }
	@FXML private void setHuman4(ActionEvent e) throws IOException { this.players[3] = "Human"; b4.setText("Human"); }
	@FXML private void setAI1(ActionEvent e) throws IOException { this.players[0] = "AI"; b1.setText("AI"); }
	@FXML private void setAI2(ActionEvent e) throws IOException { this.players[1] = "AI"; b2.setText("AI"); }
	@FXML private void setAI3(ActionEvent e) throws IOException { this.players[2] = "AI"; b3.setText("AI"); }
	@FXML private void setAI4(ActionEvent e) throws IOException { this.players[3] = "AI"; b4.setText("AI"); }
	
	// hardcode is better than no code c:
	@FXML private void nextShield1(ActionEvent e) throws IOException {
		playerShield1 = (playerShield1 + 1) % shieldImages.length;
		if(playerShield1 == 0) playerShield1++;
		// mkay i fixed it so we only load on init file I/O is 
		// expensive bois c:
		try {
			shieldView1.setImage(shieldImages[playerShield1]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void nextShield2(ActionEvent e) throws IOException {
		playerShield2 = (playerShield2 + 1) % shieldImages.length;
		if(playerShield2 == 0) playerShield1++;
		// mkay i fixed it so we only load on init file I/O is 
		// expensive bois c:
		try {
			shieldView2.setImage(shieldImages[playerShield2]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void nextShield3(ActionEvent e) throws IOException {
		playerShield3 = (playerShield3 + 1) % shieldImages.length;
		if(playerShield3 == 0) playerShield3++;
		// mkay i fixed it so we only load on init file I/O is 
		// expensive bois c:
		try {
			shieldView3.setImage(shieldImages[playerShield3]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void nextShield4(ActionEvent e) throws IOException {
		playerShield4 = (playerShield4 + 1) % shieldImages.length;
		if(playerShield4 == 0) playerShield4++;
		// mkay i fixed it so we only load on init file I/O is 
		// expensive bois c:
		try {
			shieldView4.setImage(shieldImages[playerShield4]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void prevShield1(ActionEvent e) throws IOException {
		if (playerShield1 == 0) { playerShield1 = shieldImages.length-1; } else { playerShield1 -= 1; }
		try {
			shieldView1.setImage(shieldImages[playerShield1]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void prevShield2(ActionEvent e) throws IOException {
		if (playerShield2 == 0) { playerShield2 = shieldImages.length-1; } else { playerShield2 -= 1; }
		try {
			shieldView2.setImage(shieldImages[playerShield2]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void prevShield3(ActionEvent e) throws IOException {
		if (playerShield3 == 0) { playerShield3 = shieldImages.length-1; } else { playerShield3 -= 1; }
		try {
			shieldView3.setImage(shieldImages[playerShield3]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@FXML private void prevShield4(ActionEvent e) throws IOException {
		if (playerShield4 == 0) { playerShield4 = shieldImages.length-1; } else { playerShield4 -= 1; }
		try {
			shieldView4.setImage(shieldImages[playerShield4]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private int getNumPlayers() {
		int num = 0;
		for(int i=0; i<players.length; i++) {
			if(players[i].equals("Human") || players[i].equals("AI")) num++;
		}
		return num;
	}
	
	public void hideMenu() { menuPane.setVisible(false); }
	@FXML public void showPlayerSelect(ActionEvent e) throws IOException { hideMenu(); playerSelect.setVisible(true); }
	
	@FXML
	private void handleButtonAction(ActionEvent e) throws IOException {
		
		for(int i=0;i<players.length;i++) { System.out.println(players[i]); }
	
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("GameBoard.fxml"));
		Scene gameScene = new Scene(fxmlLoader.load());
		
		//scale the game application to full screen
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double scaleX = screenBounds.getMaxX()/1920; //FXML anchor pane width is 1024 and height is 768
		double scaleY = screenBounds.getMaxY()/1080;
//			System.out.println("scale X: " + scaleX);
//			System.out.println("scale y: " + scaleY);
		scaleScene(gameScene,scaleX,scaleY);
		
		//give the GameBoardController the client if we got it
		//in this case the GBC should always have the client when we initialize it
		System.out.println("GameBoardController has reference to Client");
		GameBoardController gbc = fxmlLoader.getController();
		gbc.setClient(client);
		gbc.setUp();
		gbc.setShields(players,
					   shieldImages[playerShield1],
					   shieldImages[playerShield2],
					   shieldImages[playerShield3],
					   shieldImages[playerShield4]);
		client.setGameBoardController(gbc);
		//Change the scene to the gameScene and show it.
		
		Stage stage = (Stage) start.getScene().getWindow();	
		stage.setScene(gameScene);
		stage.show();
		
		//Setup player manager
		gbc.initPlayerManager(getNumPlayers());
		
		//send gameStart message.
		client.send(new GameStartClient(getNumPlayers()));
	}
	
	//Gives the capability to scale the screen based on the scale factor (1.0 = 100%, 0.5 = 50% etc)
	private void scaleScene(Scene scene, double scaleX, double scaleY) {
		Scale scale = new Scale(scaleX,scaleY);
		scale.setPivotX(0);
		scale.setPivotY(0);
		scene.getRoot().getTransforms().setAll(scale);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		players = new String[4];
		for(int i=0; i<players.length;i++) {
			players[i] = "";
		}
		shieldImages = new Image[4];
		for(int i=1; i<=shieldImages.length; i++) {
			try {
				File f = new File("src/main/resources/S"+i+".png");
				Image shieldImg = new Image (new FileInputStream(f));
				shieldImages[i-1] = shieldImg;
			} catch (Exception e) { e.printStackTrace(); }
			System.out.println(shieldImages[i-1]);
		}
		playerShield1=0;
		playerShield2=0;
		playerShield3=0;
		playerShield4=0;
		try {
			shieldView1 = new ImageView();
			shieldView1.setImage(shieldImages[playerShield1]);
			shieldView1.fitWidthProperty().bind(shieldPane1.widthProperty());
			shieldView1.fitHeightProperty().bind(shieldPane1.heightProperty());
			shieldPane1.getChildren().add(shieldView1);
			
			shieldView2 = new ImageView();
			shieldView2.setImage(shieldImages[playerShield2]);
			shieldView2.fitWidthProperty().bind(shieldPane2.widthProperty());
			shieldView2.fitHeightProperty().bind(shieldPane2.heightProperty());
			shieldPane2.getChildren().add(shieldView2);
			
			shieldView3 = new ImageView();
			shieldView3.setImage(shieldImages[playerShield3]);
			shieldView3.fitWidthProperty().bind(shieldPane3.widthProperty());
			shieldView3.fitHeightProperty().bind(shieldPane3.heightProperty());
			shieldPane3.getChildren().add(shieldView3);
			
			shieldView4 = new ImageView();
			shieldView4.setImage(shieldImages[playerShield4]);
			shieldView4.fitWidthProperty().bind(shieldPane4.widthProperty());
			shieldView4.fitHeightProperty().bind(shieldPane4.heightProperty());
			shieldPane4.getChildren().add(shieldView4);
			
			playerSelect.setVisible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
