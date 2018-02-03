package src.game_logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public abstract class Card {
	private String name;
	private Image img;
	private ImageView imgView;
	
	public Card(String name) {
		this.name = name;
		
	}
	public Card(String name, String path) {
		try {
			System.out.println("New Card Path: " + path);
			this.name = name;
			File file = new File(path);
			img = new Image (new FileInputStream(file));
			imgView = new ImageView();
			imgView.setImage(img);
			imgView.setFitWidth(100);
			imgView.setFitHeight(150);
//			imgView.setdrag
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
//	public void setDraggableOn() {
//		if(imgView != null) {
//			imgView.setOnDragDetected(new EventHandler<MouseEvent>()
//					);
//		}
//		
//	}
	public ImageView getImageView() {
		return imgView;
	}
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}