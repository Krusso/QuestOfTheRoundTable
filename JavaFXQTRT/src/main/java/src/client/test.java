package src.client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class test extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = new AnchorPane();
			root = FXMLLoader.load(getClass().getResource("TitleScreen.fxml"));
			
			
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
//
//	    	FXMLLoader fxml = new FXMLLoader();
//	    	fxml.setLocation(getClass().getResource("TitleScreen.fxml"));
//	   
//	    	Parent root = fxml.load();
//
//	    	Scene scene = new Scene(root);
//	    	stage.setTitle("Quest of the Round Table");
//	    	stage.setScene(scene);
//	    	stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

