package src.game_logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import src.client.GameBoardController;

public abstract class Card {
	
	final static Logger logger = LogManager.getLogger(Card.class);
	
	static final AtomicInteger NEXT_ID = new AtomicInteger(0);
	
	public Pane childOf;
	public final int id = NEXT_ID.getAndIncrement();
	public static double DEFAULT_WIDTH = 100;
	public static double DEFAULT_HEIGHT = 150;
	private String name;
	public Image cardBack;
	public Image img;
	public ImageView imgView;
	public GameBoardController gbc;
	public Pane faceDownPane;

	private double startX = 0;
	private double startY = 0;
	private double orgStartX = 0;
	private double orgStartY = 0;
	public boolean inPlay = false;
	
	private int merlinUses = 1;

	public Card(String name) {
		this.name = name;

	}
	public Card(String name, URL path) {
		try {
			this.name = name;
			img = new Image (path.openStream());
			imgView = new ImageView();
			imgView.setImage(img);
			imgView.setFitWidth(100);
			imgView.setFitHeight(150);
			//setDraggableOn();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	public boolean isCardFaceUp() {
		if(imgView.getImage().equals(img)) {
			return true;
		}
		return false;
	}
	
	public void setImgView(URL url) {
		try {
			img = new Image (url.openStream());
			imgView = new ImageView();
			imgView.setImage(img);
			imgView.setFitWidth(100);
			imgView.setFitHeight(150);
			//setDraggableOn();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void setOriginalPosition(double x, double y) {
		orgStartX = x;
		orgStartY = y;
	}
	
	public void setCardBack(URL url) {
		try {
			cardBack = new Image(url.openStream());
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void setImageSize(double width, double height) {
		if(imgView != null) {
			imgView.setFitHeight(height);
			imgView.setFitWidth(width);
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
		imgView.setOnMousePressed(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
    			orgStartX = imgView.getX();
    			orgStartY = imgView.getY();
    			startX = event.getX() - imgView.getX();
    			startY = event.getY() - imgView.getY();
            	event.setDragDetect(true);
            }
        });

		imgView.setOnMouseReleased(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
            	imgView.setMouseTransparent(false);
            	logger.info(event.getX() + " " + event.getY());
            	logger.info(event.getScreenX()+ " " + event.getScreenY());
            	Point2D p = new Point2D(event.getSceneX(), event.getSceneY());
            	logger.info("Moving Card:" + name +" id:" + id + " childOf: " + childOf);
            	gbc.putIntoPane(p, id);
            	event.consume();
            }
        });

		imgView.setOnMouseDragged(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
    			imgView.setX(event.getX() - startX);
    			imgView.setY(event.getY() - startY);
            }
        });
		imgView.setOnDragDetected(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
            	imgView.startFullDrag();
            }
        });
		
	}
	public ImageView getImageView() {
		return imgView;
	}
	public boolean isVisibile() {
		return imgView.isVisible();
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
	/*
	 * copied and edited from
	 * https://stackoverflow.com/questions/37117475/attach-back-cover-to-a-card-image
	 */
	public SequentialTransition flipUp() {
        // first 90  -> show back
        RotateTransition rotator1 = createRotator(180, 270);

        // from 90 to 180 show front
        rotator1.setOnFinished(evt -> this.faceUp());
        RotateTransition rotator2 = createRotator(270, 360);

        SequentialTransition rotator = new SequentialTransition(rotator1, rotator2);
        rotator.setCycleCount(1);
        return rotator;
	} 
	
	public SequentialTransition flipDown() {
        // first 90 -> show front
        RotateTransition rotator1 = createRotator(0, 90);

        // from 90 to 180 show backside
        rotator1.setOnFinished(evt ->this.faceDown());
        RotateTransition rotator2 = createRotator(90, 180);

        SequentialTransition rotator = new SequentialTransition(rotator1, rotator2);
        rotator.setCycleCount(1);
        return rotator;
	}
	
	private RotateTransition createRotator(double fromAngle, double toAngle) {
        // animation length proportional to the rotation angle
        RotateTransition rotator = new RotateTransition(Duration.millis(Math.abs(1000 * (fromAngle - toAngle) / 360) * 5), imgView);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setInterpolator(Interpolator.LINEAR);
        return rotator;
    }
	
	public void revertImagePosition() {
		imgView.setX(orgStartX);
		imgView.setY(orgStartY);
	}
	
	public boolean isMerlin() {
		if(name.equalsIgnoreCase("Merlin")) {
			return true;
		}
		return false;
	}
	
	public boolean isMordred() {
		if(name.equalsIgnoreCase("Mordred")) {
			return true;
		}
		return false;
	}
	
	public boolean tryUseMerlin() {
		logger.info("Merlin uses: " + merlinUses);
		if(merlinUses > 0) {
			merlinUses--;
			return true;
		}
		return false;
	}
	
	public void resetMerlinCharges() {
		merlinUses = 1;
	}
	
	
}