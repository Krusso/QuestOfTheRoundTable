package src.game_logic;

import java.io.File;
import java.io.FileInputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import src.client.GameBoardController;

public abstract class Card {
	private String name;
	private Image img;
	private ImageView imgView;
	public GameBoardController gbc;

	private double startX = 0;
	private double startY = 0;
	private double orgStartX = 0;
	private double orgStartY = 0;
	public boolean inPlay = false;

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
			//setDraggableOn();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void returnOriginalPosition() {
		imgView.setX(orgStartX);
		imgView.setY(orgStartY);
	}
	
	public void setDraggableOff() {
		imgView.setOnMouseReleased(null);
		imgView.setOnMousePressed(null);
		imgView.setOnMouseDragged(null);
	}
	
	//Simple drag motion for the card ImageView
	public void setDraggableOn() {
		imgView.setOnMouseReleased((MouseEvent e)->{
			if(gbc != null) {
				gbc.playCard(this, imgView.getX(), imgView.getY());	
			}
		});
		imgView.setOnMousePressed((MouseEvent e) -> {
			orgStartX = imgView.getX();
			orgStartY = imgView.getY();
			startX = e.getX() - imgView.getX();
			startY = e.getY() - imgView.getY();
		});
		imgView.setOnMouseDragged((MouseEvent e)->{
			imgView.setX(e.getX() - startX);
			imgView.setY(e.getY() - startY);
		});
	}
	public ImageView getImageView() {
		return imgView;
	}

	public void hide() {
		imgView.setVisible(false);
	}
	public void show() {
		imgView.setVisible(true);
		//		System.out.println("Set " + name + " visibility to: " + imgView.visibleProperty().getValue().);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}