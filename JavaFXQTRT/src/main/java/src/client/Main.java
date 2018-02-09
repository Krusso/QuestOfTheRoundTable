package src.client;
import java.io.File;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import src.game_logic.AdventureCard;
import src.socket.Server;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
			
			try {
				File file = new File("src/main/resources/titlescreen1.jpg");
				Image img = new Image (new FileInputStream(file));
				ImageView imgView = new ImageView();
				imgView.setImage(img);
				imgView.fitWidthProperty().bind(primaryStage.widthProperty());
				imgView.fitHeightProperty().bind(primaryStage.heightProperty());
//				tlc.addImage(imgView);
				tlc.background.getChildren().add(imgView);
			} catch (Exception e) {
				e.printStackTrace();
			}
						
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

