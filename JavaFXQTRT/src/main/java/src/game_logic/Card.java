package src.game_logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import src.client.GameBoardController;

public abstract class Card {
	private String name;
	private Image cardBack;
	private Image img;
	private ImageView imgView;
	public GameBoardController gbc;
	public Pane faceDownPane;

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
	
	public void setCardBack(String path) {
		File file = new File(path);
		try {
			cardBack = new Image(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void setImageSize(double width, double height) {
		imgView.setFitHeight(height);
		imgView.setFitWidth(width);
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
				gbc.playCard(this, this.faceDownPane, imgView.getX(), imgView.getY());
			}
		});
//		imgView.setOnDragDetected(e -> {
//			Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
//	        ClipboardContent content = new ClipboardContent();
//	        content.putString(name);
//	        db.setContent(content);
//	        e.consume();
//		});
//		imgView.setOnMouseDragEntered(e -> {
//			orgStartX = imgView.getX();
//			orgStartY = imgView.getY();
//			startX = e.getX() - imgView.getX();
//			startY = e.getY() - imgView.getY();
//		});
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
		imgView.setImage(img);
		imgView.setVisible(true);
	}
	public void faceDown() {
		imgView.setImage(cardBack);
	}
	public void faceUp() {
		imgView.setImage(img);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}