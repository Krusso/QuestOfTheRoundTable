package src.client;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
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
	public static LinkedBlockingQueue<String> input = new LinkedBlockingQueue<String>();
	public static LinkedBlockingQueue<String> output = new LinkedBlockingQueue<String>();
	public static Client client;
	public TitleScreenController tlc;

	@Override
	public void start(Stage primaryStage) {
		try {
			//Setup client
			Main.client = new Client(input, output);
			new Thread(client).start();

			Parent root = new AnchorPane();
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("TitleScreen.fxml"));
			root = fxmlLoader.load();

			//Get the controller instance
			tlc = fxmlLoader.getController();
			logger.info("Main's TitleScreenController reference:" + tlc);
			//Pass the client to the controller
			tlc.setClient(client);

			try {
				File titlebg = new File("src/main/resources/titlescreen1.jpg");
				Image titleImg = new Image (new FileInputStream(titlebg));
				ImageView titleImgView = new ImageView();
				titleImgView.setImage(titleImg);
				titleImgView.fitWidthProperty().bind(primaryStage.widthProperty());
				titleImgView.fitHeightProperty().bind(primaryStage.heightProperty());
				//				tlc.addImage(imgView);
				tlc.background.getChildren().add(titleImgView);

			} catch (Exception e) {
				e.printStackTrace();
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
					default:
						break;
					}
				}
			});
			//			primaryStage.setMaxHeight(900);
			//			primaryStage.setMaxWidth(1400);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		//Starts server
		Runnable task2 = () -> { new Server(input, output);  };
		// start the thread
		new Thread(task2).start();
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

