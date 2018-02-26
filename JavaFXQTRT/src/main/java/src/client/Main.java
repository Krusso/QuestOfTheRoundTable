package src.client;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.messages.game.GameStartClient.RIGGED;
import src.socket.Server;

public class Main extends Application {
	final static Logger logger = LogManager.getLogger(Main.class);
	public static Thread thread;
	public static Thread clientThread;
	public static LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
	public static LinkedBlockingQueue<String> output = new LinkedBlockingQueue<String>();
	public static Client client;
	public TitleScreenController tlc;

	@Override
	public void start(Stage primaryStage) {
		try {
			//Setup client
			Main.client = new Client(input, output);
			Main.clientThread = new Thread(client);
			Main.clientThread.start();

			Parent root = new AnchorPane();
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getClassLoader().getResource("TitleScreen.fxml"));
			root = fxmlLoader.load();

			//Get the controller instance
			tlc = fxmlLoader.getController();
			logger.info("Main's TitleScreenController reference:" + tlc);
			//Pass the client to the controller
			tlc.setClient(client);
			
			
			try {
				Image titleImg = new Image(getClass().getClassLoader().getResource("titlescreen1.jpg").openStream());
				ImageView titleImgView = new ImageView();
				titleImgView.setImage(titleImg);
				titleImgView.fitWidthProperty().bind(primaryStage.widthProperty());
				titleImgView.fitHeightProperty().bind(primaryStage.heightProperty());
				//				tlc.addImage(imgView);
				tlc.background.getChildren().add(titleImgView);

			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					switch (event.getCode()) {
					case UP:    
						tlc.setRigged(RIGGED.ONE); break;
					case LEFT:
						tlc.setRigged(RIGGED.TWO); break;
					case RIGHT:
						tlc.setRigged(RIGGED.LONG); break;
					case A:
						tlc.setRigged(RIGGED.AITOURNAMENT); break;
					case B:
						tlc.setRigged(RIGGED.AIQUEST); break;
					case C:
						tlc.setRigged(RIGGED.AIQUEST1); break;
					case D:
						tlc.setRigged(RIGGED.AIQUEST2); break;
					case E:
						tlc.setRigged(RIGGED.GAMEEND); break;
					default:
						break;
					}
				}
			});
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> {
				Platform.exit(); 
				Main.thread.interrupt();
				Main.clientThread.interrupt();
				System.exit(0);
			});
			tlc.bgMusic	= new AudioPlayer("Main_Title.mp3");
			tlc.bgMusic.play();

		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	public static void main(String[] args) {
		
		//Starts server
		Runnable task2 = () -> { new Server(input, output);  };
		// start the thread
		Main.thread = new Thread(task2);
		thread.start();
		launch(args);
	}
	public TitleScreenController getTitleScreenController() {
		return tlc;
	}
	public GameBoardController getGameBoardController() {
		if(tlc!=null) {
			tlc.getGameBoardController();
		}
		return null;
	}
}

