package src.client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import src.game_logic.AdventureCard;
import src.socket.Server;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Setup client
			Client client = new Client("localhost", 2223);
			new Thread(client).start();
		
			Parent root = new AnchorPane();
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("TitleScreen.fxml"));
			root = fxmlLoader.load();
			
			//Get the controller instance
			TitleScreenController tlc = fxmlLoader.getController();
			//Pass the client to the controller
			tlc.setClient(client);
			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		//Starts server
		Runnable task2 = () -> { Server.main(null); };
		// start the thread
		new Thread(task2).start();
		launch(args);
	}
}

