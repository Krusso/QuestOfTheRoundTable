package src.game_logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.activation.ActivateFailedException;

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
	
	private double startX = 0;
	private double startY = 0;
	
	public Card(String name) {
		this.name = name;
		
	}
	public Card(String name, String path) {
		try {
			this.name = name;
			File file = new File(path);
			img = new Image (new FileInputStream(file));
			imgView = new ImageView();
			imgView.setImage(img);
			imgView.setFitWidth(100);
			imgView.setFitHeight(150);
			setDraggableOn();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Simple drag motion for the card ImageView
	public void setDraggableOn() {
		imgView.setOnMousePressed((MouseEvent e) -> {
			 startX = e.getX() - imgView.getX();
			 startY = e.getY() - imgView.getY();
		});
		imgView.setOnMouseDragged((MouseEvent e)->
		{
			imgView.setX(e.getX() - startX);
			imgView.setY(e.getY() - startY);
		});
	}
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