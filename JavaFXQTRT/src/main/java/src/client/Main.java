package src.client;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.messages.game.GameStartClient.RIGGED;
import src.socket.Server;
import static javafx.concurrent.Worker.State;

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
			
//			WebView webView = new WebView();
//			final WebEngine webEngine = webView.getEngine();
//			webEngine.load("http://www.google.ca");
//			
//			webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>(){
//
//				@Override
//				public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
//					// TODO Auto-generated method stub
//					if(newValue == State.SUCCEEDED) {
//						primaryStage.setTitle(webEngine.getTitle());
//					}
//				}
//				
//			});
			
			
//			Parent root = new AnchorPane();
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getClassLoader().getResource("TitleScreen.fxml"));
			Parent root = fxmlLoader.load();

			//Get the controller instance
			tlc = fxmlLoader.getController();
			logger.info("Main's TitleScreenController reference:" + tlc);
			//Pass the client to the controller
			tlc.setClient(client);

			Scene scene = new Scene(root);
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			double scaleX = screenBounds.getMaxX()/1920; 
			double scaleY = screenBounds.getMaxY()/1080;
			scaleScene(scene,scaleX,scaleY);
			
			
			primaryStage.setScene(scene);
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					switch (event.getCode()) {
					case UP:    
						tlc.setRigged(RIGGED.ONE); break;
					case LEFT:
						tlc.setRigged(RIGGED.TWO); break;
					case DOWN:
						tlc.setRigged(RIGGED.THREE); break;
					case RIGHT:
						tlc.setRigged(RIGGED.LONG); break;
					case Z:
						tlc.setRigged(RIGGED.FOUR); break;
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
	
	private void scaleScene(Scene scene, double scaleX, double scaleY) {
		Scale scale = new Scale(scaleX,scaleY);
		scale.setPivotX(0);
		scale.setPivotY(0);
		scene.getRoot().getTransforms().setAll(scale);
	}
}

