package src.game_logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import src.client.GameBoardController;

public abstract class Card {
	private String name;
	private Image cardBack;
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
	
	public void setCardBack(String path) {
		File file = new File(path);
		try {
			cardBack = new Image(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * copied and edited from
	 * https://stackoverflow.com/questions/37117475/attach-back-cover-to-a-card-image
	 */
	public SequentialTransition flipUp() {
        // first 90° -> show back
        RotateTransition rotator1 = createRotator(0, 90);

        // from 90° to 180° show front
        rotator1.setOnFinished(evt -> this.faceUp());
        RotateTransition rotator2 = createRotator(90, 180);

        SequentialTransition rotator = new SequentialTransition(rotator1, rotator2);
        rotator.setCycleCount(1);
        return rotator;
	} 
	
	public SequentialTransition flipDown() {
        // first 90° -> show front
        RotateTransition rotator1 = createRotator(0, 90);

        // from 90° to 180° show backside
        rotator1.setOnFinished(evt ->this.faceDown());
        RotateTransition rotator2 = createRotator(90, 180);

        SequentialTransition rotator = new SequentialTransition(rotator1, rotator2);
        rotator.setCycleCount(1);
        return rotator;
	}
	
	private RotateTransition createRotator(double fromAngle, double toAngle) {
        // animation length proportional to the rotation angle
        RotateTransition rotator = new RotateTransition(Duration.millis(Math.abs(1000 * (fromAngle - toAngle) / 360)), imgView);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setInterpolator(Interpolator.LINEAR);
        return rotator;
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