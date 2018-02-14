package src.game_logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import src.client.GameBoardController;

public abstract class Card {
	static final AtomicInteger NEXT_ID = new AtomicInteger(0);
	final int id = NEXT_ID.getAndIncrement();
	public static double DEFAULT_WIDTH = 100;
	public static double DEFAULT_HEIGHT = 150;
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

		
//		imgView.setOnMouseReleased(e->{
//			imgView.setMouseTransparent(false);
//			e.consume();
//		});
//		
//		imgView.setOnMousePressed(e->{
//			System.out.println("img mouse pressed");
//			orgStartX = imgView.getX();
//			orgStartY = imgView.getY();
//			startX = e.getX() - imgView.getX();
//			startY = e.getY() - imgView.getY();
//			imgView.setMouseTransparent(true);
//			e.setDragDetect(true);
//			imgView.setPickOnBounds(false);
//		});
//		
//		imgView.setOnDragDetected(e->{
//			System.out.println("Mouse Drag Entered");
//			Clipboard clipboard = Clipboard.getSystemClipboard();
//			ClipboardContent content = new ClipboardContent();
//			content.putString(id+"");
//			content.putImage(imgView.getImage());
//			clipboard.setContent(content);	
////			imgView.startFullDrag();
//		});
//		imgView.setOnMouseDragged(e->{
//			imgView.setX(e.getX() - startX);
//			imgView.setY(e.getY() - startY);
//			e.setDragDetect(false);
//		});
		
//		imgView.setOnDragDetected(e->{
//			System.out.println("On Drag Detected");
//			Dragboard db = imgView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
////			db.setDragView(new Image());
//			ClipboardContent content = new ClipboardContent();
//			content.putString(id+"");
//			content.putImage(imgView.getImage());
//			db.setContent(content);
////			e.consume();
//		});
		imgView.setOnMousePressed(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
    			orgStartX = imgView.getX();
    			orgStartY = imgView.getY();
    			startX = imgView.getX() - imgView.getX();
    			startY = imgView.getY() - imgView.getY();
            	imgView.setMouseTransparent(true);
            	System.out.println("Event on Source: mouse pressed");
            	event.setDragDetect(true);
            }
        });

		imgView.setOnMouseReleased(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
            	imgView.setMouseTransparent(false);
            	System.out.println("Event on Source: mouse released");
            	Point2D p = new Point2D(event.getSceneX(), event.getSceneY());
            	System.out.println("POINT: " + p.toString());
            	gbc.putIntoPane(p, id);
            	System.out.println(imgView.getX() + " " + imgView.getY());
            	event.consume();
            }
        });

		imgView.setOnMouseDragged(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
            	System.out.println("Event on Source: mouse dragged");
            	event.setDragDetect(false);
            	imgView.setMouseTransparent(true);
    			imgView.setX(event.getX() - startX);
    			imgView.setY(event.getY() - startY);
            }
        });

		imgView.setOnDragDetected(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event)
            {
            	imgView.startFullDrag();
            	System.out.println("Event on Source: drag detected");
            }
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
	/*
	 * copied and edited from
	 * https://stackoverflow.com/questions/37117475/attach-back-cover-to-a-card-image
	 */
	public SequentialTransition flipUp() {
        // first 90  -> show back
        RotateTransition rotator1 = createRotator(0, 90);

        // from 90 to 180 show front
        rotator1.setOnFinished(evt -> this.faceUp());
        RotateTransition rotator2 = createRotator(90, 180);

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
        RotateTransition rotator = new RotateTransition(Duration.millis(Math.abs(1000 * (fromAngle - toAngle) / 360)), imgView);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setInterpolator(Interpolator.LINEAR);
        return rotator;
    }
	
	public int getID() {
		return id;
	}
	public void revertImagePosition() {
		imgView.setX(orgStartX);
		imgView.setY(orgStartY);
		System.out.println(imgView.getX() + " " + imgView.getY());
		System.out.println(imgView.getParent());
	}
	
}